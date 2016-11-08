package com.codepath.apps.Twitterbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.adapters.ProfileViewPagerAdapter;
import com.codepath.apps.Twitterbook.fragments.PhotosFragment;
import com.codepath.apps.Twitterbook.fragments.TweetsListFragment;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.OnFragmentInteractionListener {

    private TwitterClient twitterClient;
    private UserProfileModel userProfileModel;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ivCoverImage) ImageView ivCoverImage;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvCountFollowers) TextView tvCountFollowers;
    @BindView(R.id.tvCountFollowing) TextView tvCountFollowing;
    @BindView(R.id.tvFollowers) TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.tvInfo) TextView tvInfo;

    private ArrayList<TweetModel>  userMedia;
    private ProfileViewPagerAdapter profileViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(ProfileActivity.this);

        setSupportActionBar(toolbar);

        twitterClient = new TwitterClient(ProfileActivity.this);

        String userId = getIntent().getStringExtra("user_id");
        loadUserProfile(userId);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
        profileViewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager(), ProfileActivity.this, userId);
        viewPager.setAdapter(profileViewPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void loadUserProfile(String screenName) {
        twitterClient.getUsersProfileDetails(screenName,new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                userProfileModel = new UserProfileModel(json);
                tvName.setText(userProfileModel.getCurrentUserName());
                tvUserName.setText("@"+userProfileModel.getCurrentUserId());
                tvCountFollowers.setText(userProfileModel.getCurrentUserFollowersCount());
                tvCountFollowing.setText(userProfileModel.getCurrentUserFollowing());
                tvInfo.setText(userProfileModel.getUserProfileDescription());
                Glide.with(ProfileActivity.this).load(userProfileModel.getCurrentUserProfile()).
                bitmapTransform(new RoundedCornersTransformation(ProfileActivity.this,5,5)).into(ivProfileImage);
                Glide.with(ProfileActivity.this).load(userProfileModel.getCurrentUserProfileBackground()).into(ivCoverImage);

                tvFollowers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileActivity.this, FollowListActivity.class);
                        intent.putExtra("user_id", userProfileModel.getCurrentUserId());
                        intent.putExtra("tag", 0);
                        startActivity(intent);
                    }
                });

                tvFollowing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProfileActivity.this, FollowListActivity.class);
                        intent.putExtra("user_id", userProfileModel.getCurrentUserId());
                        intent.putExtra("tag", 1);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public void onUserProfileLoaded(UserProfileModel userProfileModel) {

    }

    @Override
    public void onTweetListLoadedUpdated(ArrayList<TweetModel> tweetModels) {
        ((PhotosFragment)profileViewPagerAdapter.getRegisteredFragment(1)).setUserMedia(tweetModels);
    }
}
