package com.codepath.apps.Twitterbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.TwitterApplication;
import com.codepath.apps.Twitterbook.TwitterClient;
import com.codepath.apps.Twitterbook.models.UserProfileModel;
import com.loopj.android.http.AsyncHttpResponseHandler;

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

    private EditText etComposeTweet;
    private TextView tvCharacterCount;
    private TextView tvUserName;
    private TextView tvUserId;
    private ImageView ivUserImage;
    private Toolbar toolbar;

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

        if (getArguments() != null) {
            userId = getArguments().getString(USER_ID);
            userProfileModel = getArguments().getParcelable(USER_PROFILE);
        }

        toolbar = (Toolbar) (Toolbar) view.findViewById(R.id.toolbar);
        etComposeTweet = (EditText) view.findViewById(R.id.etComposeBody);
        tvCharacterCount = (TextView) view.findViewById(R.id.tvCharacterCount);
        tvUserId = (TextView) view.findViewById(R.id.tvName);
        tvUserName = (TextView) view.findViewById(R.id.tvUsername);
        ivUserImage = (ImageView) view.findViewById(R.id.ivUserAvatar);

        twitterClient = TwitterApplication.getRestClient();

        if(!TextUtils.isEmpty(userId))
        {
            etComposeTweet.setText(userId);
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
                String tweet = etComposeTweet.getText().toString();
                if (!TextUtils.isEmpty(tweet) && tweet.length() <= 140) {
                    twitterClient.postTweet(tweet, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            mListener.onTweetSuccess("Success");
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
        void onTweetSuccess(String message);
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
