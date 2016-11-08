package com.codepath.apps.Twitterbook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.Twitterbook.R;

/**
 * Created by PRAGYA on 11/6/2016.
 */

public class FollowListViewHolder extends RecyclerView.ViewHolder{

        public TextView tvUsername;
        public TextView tvName;
        public ImageView ivUserImage;
        public TextView tvDescription;

        public FollowListViewHolder(View itemView) {
            super(itemView);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvName = (TextView) itemView.findViewById(R.id.tvUserId);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
    }

}
