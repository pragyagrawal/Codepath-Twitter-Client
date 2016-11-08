package com.codepath.apps.Twitterbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.codepath.apps.Twitterbook.network.TwitterApplication;
import com.codepath.apps.Twitterbook.network.TwitterClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ComposeTweetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ComposeTweetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComposeTweetFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private UserProfileModel userProfileModel;
    private String userId;

    public static final String USER_ID = "user_id";
    public static final String USER_PROFILE = "user_profile";

    @BindView(R.id.etComposeBody) EditText etComposeTweet;
    @BindView(R.id.tvReplyTo) TextView tvReplyTo;
    @BindView(R.id.tvCharacterCount) TextView tvCharacterCount;
    @BindView(R.id.tvUsername) TextView tvUserName;
    @BindView(R.id.tvName) TextView tvUserId;
    @BindView(R.id.ivUserAvatar) ImageView ivUserImage;

    private TwitterClient twitterClient;

    public ComposeTweetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userID Parameter 1.
     * @return A new instance of fragment ComposeTweetFragment.
     */

    public static ComposeTweetFragment newInstance(String userID, UserProfileModel userProfileModel) {
        ComposeTweetFragment fragment = new ComposeTweetFragment();
        if (userProfileModel != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USER_ID, userID);
            bundle.putParcelable(USER_PROFILE, userProfileModel);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compose_tweet, container, false);

        ButterKnife.bind(this,view);

        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
            userProfileModel = getArguments().getParcelable(USER_PROFILE);
        }


        twitterClient = TwitterApplication.getRestClient();

        if(!TextUtils.isEmpty(userId))
        {
            etComposeTweet.setText(userId);
            tvReplyTo.setVisibility(View.VISIBLE);

            tvReplyTo.setText("In ReplyTo "+userId);
        }

        if (userProfileModel != null) {
            tvUserId.setText(userProfileModel.getCurrentUserName());
            tvUserName.setText(userProfileModel.getCurrentUserId());
            Glide.with(getActivity()).load(Uri.parse(userProfileModel.getCurrentUserProfile()))
                    .bitmapTransform(new RoundedCornersTransformation(getActivity(),5,5)).into(ivUserImage);
        }

        view.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        view.findViewById(R.id.btnTweet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tweet = etComposeTweet.getText().toString();
                if (!TextUtils.isEmpty(tweet) && tweet.length() <= 140) {
                    twitterClient.postTweet(tweet, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            TweetModel tweetModel = new TweetModel();
                            tweetModel.setUserId(userProfileModel.getCurrentUserId());
                            tweetModel.setUserName(userProfileModel.getCurrentUserName());
                            tweetModel.setProfileImage(userProfileModel.getCurrentUserProfile());
                            tweetModel.setBody(tweet);
                            mListener.onTweetSuccess(tweetModel);
                            dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            System.out.print("");
                        }
                    });
                }
            }
        });

        etComposeTweet.addTextChangedListener(textEditorWatcher);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onTweetSuccess(TweetModel tweetModel);
    }

    private final TextWatcher textEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharacterCount.setText(String.valueOf(140 - s.length()));
            if (s.length() > 140) {
                tvCharacterCount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                tvCharacterCount.setTextColor(getResources().getColor(android.R.color.black));
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };
}
