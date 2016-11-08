package com.codepath.apps.Twitterbook.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.fragments.TweetsListFragment;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetSearchActivity extends AppCompatActivity implements TweetsListFragment.OnFragmentInteractionListener {


    private String searchQuery;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle(R.string.search_result);

        Bundle bundle = getIntent().getExtras();
        searchQuery = bundle.getString("search_query");

        TweetsListFragment tweetsListFragment = TweetsListFragment.newInstance(5,searchQuery);

        getSupportFragmentManager().beginTransaction().add(R.id.rvSearchContainer, tweetsListFragment).commit();

    }

    @Override
    public void onUserProfileLoaded(UserProfileModel userProfileModel) {

    }

    @Override
    public void onTweetListLoadedUpdated(ArrayList<TweetModel> tweetModels) {

    }
}
