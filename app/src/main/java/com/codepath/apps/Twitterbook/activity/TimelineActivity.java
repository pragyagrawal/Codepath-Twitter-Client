package com.codepath.apps.Twitterbook.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.TwitterApplication;
import com.codepath.apps.Twitterbook.TwitterClient;
import com.codepath.apps.Twitterbook.adapters.TimelineRecyclerViewAdapter;
import com.codepath.apps.Twitterbook.fragments.ComposeTweetFragment;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.utils.DividerItemDecoration;
import com.codepath.apps.Twitterbook.utils.EndlessScrollListener;
import com.codepath.apps.Twitterbook.utils.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetFragment.OnFragmentInteractionListener {

    private ArrayList<TweetModel> tweets;
    private TimelineRecyclerViewAdapter timelineAdapter;
    private RecyclerView rvTimeline;
    private EndlessScrollListener endlessScrollListener;
    private SwipeRefreshLayout swipeContainer;

    private TwitterClient twitterClient;
    private UserProfileModel userProfileModel;
    private int pageNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tweets = new ArrayList<>();
        twitterClient = TwitterApplication.getRestClient();

        initializeRecyclerView();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTweets(0, true);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCompose);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                FragmentManager fragmentManager = getSupportFragmentManager();
                ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("", userProfileModel);
                composeTweetFragment.show(fragmentManager, "");
            }
        });

        loadUserProfile();

        loadTweets(pageNo, true);
    }

    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TimelineActivity.this);
        rvTimeline = (RecyclerView) findViewById(R.id.rvTimeline);

        rvTimeline.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                pageNo++;
                loadTweets(pageNo, false);
            }
        };

        rvTimeline.addOnScrollListener(endlessScrollListener);
        timelineAdapter = new TimelineRecyclerViewAdapter(tweets, TimelineActivity.this);
        rvTimeline.setAdapter(timelineAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvTimeline.addItemDecoration(itemDecoration);
    }

    private void loadUserProfile() {
        twitterClient.getUsersProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                userProfileModel = new UserProfileModel(json);
                timelineAdapter.setUserProfileModel(userProfileModel);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void loadTweets(final int page, final boolean isfirstLoad) {
        if (Utils.isOnline()) {
            twitterClient.getHomeTimeline(page, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                    tweets = TweetModel.fromJson(json);
                    if (isfirstLoad) {
                        timelineAdapter.setTweetsList(tweets);
                    } else {
                        timelineAdapter.addAll(tweets);
                    }
                    swipeContainer.setRefreshing(false);
                    timelineAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (statusCode == 429) {
                        loadTweets(page, isfirstLoad);
                        swipeContainer.setRefreshing(false);
                    }
                }
            });
        } else {
            Snackbar.make(rvTimeline,"Please check internet connection",Snackbar.LENGTH_SHORT).show();
            timelineAdapter.setTweetsList(getOffLineTweets());
            swipeContainer.setRefreshing(false);
            timelineAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTweetSuccess(String message) {
        loadTweets(0, true);
    }

    private List<TweetModel> getOffLineTweets() {
        return SQLite.select().from(TweetModel.class).queryList();
    }
}
