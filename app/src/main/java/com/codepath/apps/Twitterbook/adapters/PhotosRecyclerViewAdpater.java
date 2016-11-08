package com.codepath.apps.Twitterbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.models.TweetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PRAGYA on 11/5/2016.
 */

public class PhotosRecyclerViewAdpater extends RecyclerView.Adapter<TweetMediaViewHolder> {

    private List<String> tweetsMediaList;
    private Context context;

    public PhotosRecyclerViewAdpater(List<String> tweetsMediaList, Context context) {
        this.tweetsMediaList = tweetsMediaList;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setTweetsList(List<String> tweetsMediaList) {
        this.tweetsMediaList = tweetsMediaList;
    }

    @Override
    public TweetMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.tweet_photos, parent, false);

        //Return the a new Holder instance
        TweetMediaViewHolder viewHolder = new TweetMediaViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TweetMediaViewHolder holder, int position) {
        final String tweetImageURL = tweetsMediaList.get(position);
        if (!TextUtils.isEmpty(tweetImageURL)) {
            Glide.with(context).load(tweetImageURL).into(holder.ivUserTweetImage);
        }
    }

    @Override
    public int getItemCount() {
        if (tweetsMediaList != null) {
            return tweetsMediaList.size();
        }
        return 0;
    }

    public void clear() {
        tweetsMediaList.clear();
    }

    public void addAll(List<TweetModel> tweetModelList) {
        if(tweetsMediaList==null) {
            tweetsMediaList = new ArrayList<>();
        }
        setTweetMedia(tweetModelList);
    }

    private void setTweetMedia(List<TweetModel> tweetModelList){
        for (TweetModel tweetModel : tweetModelList) {
            if(!TextUtils.isEmpty(tweetModel.getMediaImageURL())){
                tweetsMediaList.add(tweetModel.getMediaImageURL());
            }
        }
    }
}
