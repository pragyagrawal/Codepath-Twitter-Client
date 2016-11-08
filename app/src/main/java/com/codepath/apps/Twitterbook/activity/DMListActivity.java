package com.codepath.apps.Twitterbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.adapters.DMListAdapter;
import com.codepath.apps.Twitterbook.models.UserDMModel;
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

public class DMListActivity extends AppCompatActivity {

    private ArrayList<UserDMModel> userDMModelList;
    private DMListAdapter dmListAdapter;
    private EndlessScrollListener endlessScrollListener;
    private TwitterClient twitterClient;

    private int nextCursor = 0;

    private String userId;
    private int tag;

    @BindView(R.id.rvFollow)
    RecyclerView rvFollow;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.messages);

        twitterClient = new TwitterClient(DMListActivity.this);

        Bundle bundle = getIntent().getExtras();
        userId = bundle.getString("user_id");
        tag = bundle.getInt("tag");

        initializeRecyclerView();

        loadUserFollows(nextCursor, userId, true);
    }


    private void initializeRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DMListActivity.this);

        rvFollow.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                nextCursor++;
                loadUserFollows(nextCursor, userId, false);
            }
        };

        rvFollow.addOnScrollListener(endlessScrollListener);
        dmListAdapter = new DMListAdapter(userDMModelList, DMListActivity.this);
        rvFollow.setAdapter(dmListAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(DMListActivity.this, DividerItemDecoration.VERTICAL_LIST);
        rvFollow.addItemDecoration(itemDecoration);
    }

    private void loadUserFollows(final int page, final String screenName, final boolean isfirstLoad) {
        loadDirectMessages();
    }

    private void loadDirectMessages() {
        twitterClient.getDirectMessageSent(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
                userDMModelList = fromJsonDM(json);
                dmListAdapter.setUserDMModelList(userDMModelList);
                dmListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (statusCode == 429) {
                    loadDirectMessages();
                }
            }
        });
    }

    private ArrayList<UserDMModel> fromJsonDM(JSONArray jsonArray) {
        ArrayList<UserDMModel> dmModelArrayList = new ArrayList<>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject followJson = null;
            try {
                followJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            UserDMModel userProfileModel = new UserDMModel(followJson);
            dmModelArrayList.add(userProfileModel);
        }
        return dmModelArrayList;
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
