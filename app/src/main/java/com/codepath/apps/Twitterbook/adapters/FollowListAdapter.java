package com.codepath.apps.Twitterbook.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.models.UserProfileModel;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by PRAGYA on 11/6/2016.
 */

public class FollowListAdapter extends RecyclerView.Adapter<FollowListViewHolder> {

    private List<UserProfileModel> userProfileModelList;
    private Context context;

    public FollowListAdapter(List<UserProfileModel> userProfileModels, Context context) {
        this.userProfileModelList = userProfileModels;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setUserProfileModelList(List<UserProfileModel> userProfileModelList) {
        this.userProfileModelList = userProfileModelList;
    }

    @Override
    public FollowListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View followView = inflater.inflate(R.layout.follow_list_item, parent, false);

        //Return the a new Holder instance
        FollowListViewHolder viewHolder = new FollowListViewHolder(followView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FollowListViewHolder holder, int position) {
        UserProfileModel userProfileModel = userProfileModelList.get(position);
        holder.tvName.setText(userProfileModel.getCurrentUserName());
        holder.tvUsername.setText(userProfileModel.getCurrentUserId());
        holder.tvDescription.setText(userProfileModel.getUserProfileDescription());
        Glide.with(context).load(userProfileModel.getCurrentUserProfile()).
                bitmapTransform(new RoundedCornersTransformation(context,5,5)).into(holder.ivUserImage);
    }

    @Override
    public int getItemCount() {
        if (userProfileModelList != null) {
            return userProfileModelList.size();
        }
        return 0;
    }

    public void clear() {
        userProfileModelList.clear();
    }

    public void addAll(List<UserProfileModel> userProfileModels) {
        userProfileModelList.addAll(userProfileModels);
    }
}
