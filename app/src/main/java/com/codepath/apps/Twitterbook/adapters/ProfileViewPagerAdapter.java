package com.codepath.apps.Twitterbook.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.Twitterbook.fragments.PhotosFragment;
import com.codepath.apps.Twitterbook.fragments.TweetsListFragment;

/**
 * Created by PRAGYA on 11/5/2016.
 */

public class ProfileViewPagerAdapter extends SmartFragmentStatePagerAdapter {
    private static int PAGE_COUNT = 3;

    private String tabTitles[] = new String[] { "Tweets", "Photos", "Favorites"};
    private Context context;
    private String userId;

    public ProfileViewPagerAdapter(FragmentManager fm, Context context, String userId) {
        super(fm);
        this.context = context;
        this.userId = userId;
    }

    //Returns total number of pages
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return TweetsListFragment.newInstance(3, userId);
            case 1:
                return PhotosFragment.newInstance();
            case 2:
                return TweetsListFragment.newInstance(4, userId);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

