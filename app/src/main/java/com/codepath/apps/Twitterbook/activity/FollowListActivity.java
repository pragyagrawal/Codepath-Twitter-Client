package com.codepath.apps.Twitterbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.adapters.FollowListAdapter;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.network.TwitterClient;
import com.codepath.apps.Twitterbook.utils.DividerItemDecoration;
import com.codepath.apps.Twitterbook.utils.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class FollowListActivity extends AppCompatActivity {

    private static final String USERS = "users";
    private static final String USER_ID = "user_id";
    private static final String TAGS = "tag";
    @BindView(R.id.rvFollow)
    RecyclerView rvFollow;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<UserProfileModel> userProfileModelList;
    private FollowListAdapter followListAdapter;
    private EndlessScrollListener endlessScrollListener;
    private TwitterClient twitterClient;
    private int nextCursor = 0;
    private String userId;
    private int tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        twitterClient = new TwitterClient(FollowListActivity.this);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString(USER_ID);
        tag = bundle.getInt(TAGS);

        initializeRecyclerView();

        loadUserFollows(nextCursor, userId, true);
    }


    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FollowListActivity.this);

        rvFollow.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                nextCursor++;
                loadUserFollows(nextCursor, userId, false);
            }
        };

        rvFollow.addOnScrollListener(endlessScrollListener);
        followListAdapter = new FollowListAdapter(userProfileModelList, FollowListActivity.this);
        rvFollow.setAdapter(followListAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(FollowListActivity.this, DividerItemDecoration.VERTICAL_LIST);
        rvFollow.addItemDecoration(itemDecoration);
    }

    private void loadUserFollows(final int page, final String screenName, final boolean isfirstLoad) {
        switch (tag) {
            case 0:
                loadFollowers(page, screenName, isfirstLoad);
                getSupportActionBar().setTitle(R.string.followers);
                break;
            case 1:
                loadFollowings(page, screenName, isfirstLoad);
                getSupportActionBar().setTitle(R.string.following);
                break;
        }
    }

    private void loadFollowers(final int page, final String screenName, final boolean isfirstLoad) {
        twitterClient.getUsersFollowers(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                userProfileModelList = fromJson(json.optJSONArray("users"));
                if (isfirstLoad) {
                    followListAdapter.setUserProfileModelList(userProfileModelList);
                } else {
                    followListAdapter.addAll(userProfileModelList);
                }
                followListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadFollowers(page, "", isfirstLoad);
                }
            }
        });
    }

    private void loadFollowings(final int page, final String screenName, final boolean isfirstLoad) {
        twitterClient.getUsersFollowing(page, screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                userProfileModelList = fromJson(json.optJSONArray(USERS));
                if (isfirstLoad) {
                    followListAdapter.setUserProfileModelList(userProfileModelList);
                } else {
                    followListAdapter.addAll(userProfileModelList);
                }
                followListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadFollowings(page, "", isfirstLoad);
                }
            }
        });
    }

    private ArrayList<UserProfileModel> fromJson(JSONArray jsonArray) {
        ArrayList<UserProfileModel> followers = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject followJson = null;
            try {
                followJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            UserProfileModel userProfileModel = new UserProfileModel(followJson);
            followers.add(userProfileModel);
        }
        return followers;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
