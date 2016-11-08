package com.codepath.apps.Twitterbook.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.adapters.PhotosRecyclerViewAdpater;
import com.codepath.apps.Twitterbook.models.TweetModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosFragment extends Fragment {

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;

    private PhotosRecyclerViewAdpater photosRecyclerViewAdpater;

    public PhotosFragment() {
        // Required empty public constructor
    }
    public static PhotosFragment newInstance() {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_photos, container, false);
        ButterKnife.bind(this, view);

        initializeRecyclerView();

        return view;
    }

    private void initializeRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);

        rvPhotos.setLayoutManager(gridLayoutManager);

        photosRecyclerViewAdpater = new PhotosRecyclerViewAdpater(new ArrayList<String>(), getActivity());

        rvPhotos.setAdapter(photosRecyclerViewAdpater);
    }

    public void setUserMedia(ArrayList<TweetModel> userMedia) {
        if(photosRecyclerViewAdpater!=null) {
            photosRecyclerViewAdpater.addAll(userMedia);
            photosRecyclerViewAdpater.notifyDataSetChanged();
        }
    }
}
