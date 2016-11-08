package com.codepath.apps.Twitterbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.adapters.HomePagerAdapter;
import com.codepath.apps.Twitterbook.fragments.ComposeTweetFragment;
import com.codepath.apps.Twitterbook.fragments.TweetsListFragment;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.OnFragmentInteractionListener, ComposeTweetFragment.OnFragmentInteractionListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private UserProfileModel userProfileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.vpPager);
        viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager(), TimelineActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    @Override
    public void onUserProfileLoaded(UserProfileModel userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    @Override
    public void onTweetListLoadedUpdated(ArrayList<TweetModel> tweetModels) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.viewProfileMenu){
            Intent intent = new Intent(TimelineActivity.this, ProfileActivity.class);
            intent.putExtra("user_id", userProfileModel.getCurrentUserId());
            startActivity(intent);
        }else if(item.getItemId() == R.id.messageMenu){
            Intent intent = new Intent(TimelineActivity.this, FollowListActivity.class);
            intent.putExtra("tag", 2);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fabCompose)
    public void clickFab(View v){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("", userProfileModel);
        composeTweetFragment.show(fragmentManager, "");
    }

    @Override
    public void onTweetSuccess(TweetModel tweetModel) {

    }
}
