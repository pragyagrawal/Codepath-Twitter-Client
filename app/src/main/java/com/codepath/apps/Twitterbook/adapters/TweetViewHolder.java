package com.codepath.apps.Twitterbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Twitterbook.R;

/**
 * Created by PRAGYA on 10/28/2016.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {

    public TextView tvUsername;
    public TextView tvName;
    public TextView tvTimeStamp;
    public ImageView ivUserImage;
    public TextView tvBody;
    public TextView tvRetweetCount;
    public TextView tvFavoriteCount;
    public ImageView ivReplyTweet;
    public ImageView ivMediaURL;

    public TweetViewHolder(View itemView) {
        super(itemView);

        tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        tvBody = (TextView) itemView.findViewById(R.id.tvBody);
        tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimestamp);
        ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
        ivReplyTweet = (ImageView) itemView.findViewById(R.id.replyTweet);
        tvRetweetCount = (TextView) itemView.findViewById(R.id.retweetCount);
        tvFavoriteCount = (TextView) itemView.findViewById(R.id.favCount);
        ivMediaURL = (ImageView) itemView.findViewById(R.id.ivMediaImage);
    }
}
