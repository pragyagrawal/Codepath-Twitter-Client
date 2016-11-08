package com.codepath.apps.Twitterbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.activity.ProfileActivity;
import com.codepath.apps.Twitterbook.activity.TimelineActivity;
import com.codepath.apps.Twitterbook.fragments.ComposeTweetFragment;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.utils.ParseRelativeDate;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by PRAGYA on 10/28/2016.
 */

public class TimelineRecyclerViewAdapter extends RecyclerView.Adapter<TweetViewHolder> {

    private List<TweetModel> tweetsList;
    private Context context;
    private UserProfileModel userProfileModel;

    public TimelineRecyclerViewAdapter(List<TweetModel> tweetsList, Context context) {
        this.tweetsList = tweetsList;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setTweetsList(List<TweetModel> tweetsList) {
        this.tweetsList = tweetsList;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.tweet_item, parent, false);

        //Return the a new Holder instance
        TweetViewHolder viewHolder = new TweetViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        final TweetModel tweet = tweetsList.get(position);

        holder.tvUsername.setText("@" + tweet.getUserId());
        holder.tvName.setText(tweet.getUserName());
        holder.tvBody.setText(tweet.getBody());
        String timeStamp = ParseRelativeDate.getRelativeTimeAgo(tweet.getTimeStamp());
        holder.tvTimeStamp.setText(timeStamp);

        applyRetweetFavoriteLogic(tweet, holder);

        if (!TextUtils.isEmpty(tweet.getProfileImage())) {
            Glide.with(context).load(Uri.parse(tweet.getProfileImage()))
                    .bitmapTransform(new RoundedCornersTransformation(context, 5, 5)).into(holder.ivUserImage);

        }
        if (TextUtils.isEmpty(tweet.getMediaImageURL())) {
            holder.ivMediaURL.setVisibility(View.GONE);
        } else {
            holder.ivMediaURL.setVisibility(View.VISIBLE);
            Glide.with(context).load(Uri.parse(tweet.getMediaImageURL())).into(holder.ivMediaURL);
        }
        holder.ivReplyTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                FragmentManager fragmentManager = ((TimelineActivity) context).getSupportFragmentManager();
                ComposeTweetFragment composeTweetFragment = ComposeTweetFragment.newInstance("@" + tweet.getUserId(), userProfileModel);
                composeTweetFragment.show(fragmentManager, "");
            }
        });

        holder.ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id", tweet.getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tweetsList != null) {
            return tweetsList.size();
        }
        return 0;
    }

    public void clear() {
        tweetsList.clear();
    }

    public void addAll(List<TweetModel> tweetModelList) {
        tweetsList.addAll(tweetModelList);
    }

    public void addTweetAtStart(TweetModel tweetModel){
        tweetsList.add(0,tweetModel);
    }
    public void setUserProfileModel(UserProfileModel userProfileModel) {
        this.userProfileModel = userProfileModel;
    }

    private void applyRetweetFavoriteLogic(TweetModel tweet,TweetViewHolder holder)
    {
        if(tweet!=null) {
            if (tweet.getFavoriteCount()!=null && tweet.getFavoriteCount() > 0) {
                holder.tvFavoriteCount.setText(String.valueOf(tweet.getFavoriteCount()));
            } else {
                holder.tvFavoriteCount.setText("");
            }
            if (tweet.getRetweetCount()!=null && tweet.getRetweetCount() > 0) {
                holder.tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
            } else {
                holder.tvRetweetCount.setText("");
            }
            if (tweet.isFavorite()!=null && tweet.isFavorite()) {
                Drawable drawable = holder.tvFavoriteCount.getCompoundDrawables()[0];
                DrawableCompat.setTint(drawable, context.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                Drawable drawable = holder.tvFavoriteCount.getCompoundDrawables()[0];
                DrawableCompat.setTint(drawable, context.getResources().getColor(android.R.color.black));
            }

            if (tweet.isRetweeted()!=null&&tweet.isRetweeted()) {
                Drawable drawable = holder.tvRetweetCount.getCompoundDrawables()[0];
                DrawableCompat.setTint(drawable, context.getResources().getColor(android.R.color.holo_green_dark));
            } else {
                Drawable drawable = holder.tvRetweetCount.getCompoundDrawables()[0];
                DrawableCompat.setTint(drawable, context.getResources().getColor(android.R.color.black));
            }
        }
    }
}
