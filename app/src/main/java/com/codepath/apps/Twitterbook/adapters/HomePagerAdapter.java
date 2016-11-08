package com.codepath.apps.Twitterbook.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.apps.Twitterbook.fragments.TweetsListFragment;

/**
 * Created by PRAGYA on 11/3/2016.
 */

public class HomePagerAdapter extends SmartFragmentStatePagerAdapter{
    private static int PAGE_COUNT = 2;

    private String tabTitles[] = new String[] { "Home", "Mentions"};
    private Context context;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
                return TweetsListFragment.newInstance(0,"");
            case 1:
                return TweetsListFragment.newInstance(1,"");
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
