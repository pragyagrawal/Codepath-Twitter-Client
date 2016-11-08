package com.codepath.apps.Twitterbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.Twitterbook.R;

/**
 * Created by PRAGYA on 10/28/2016.
 */

public class TweetMediaViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivUserTweetImage;

    public TweetMediaViewHolder(View itemView) {
        super(itemView);
        ivUserTweetImage = (ImageView) itemView.findViewById(R.id.ivUserTweetImage);
    }
}
