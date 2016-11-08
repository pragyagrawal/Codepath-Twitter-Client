package com.codepath.apps.Twitterbook.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.models.UserProfileModel;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TweetsDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.etReply) EditText etReply;
    @BindView(R.id.tvCharacterCount) TextView tvCharacterCount;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvName) TextView tvUserId;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvRetweet) TextView tvRetweet;
    @BindView(R.id.tvFavorites) TextView tvFavorites;
    @BindView(R.id.tvTimestamp) TextView tvTimeStamp;
    @BindView(R.id.ivReplyTweet) ImageView ivReplyTweet;
    @BindView(R.id.ivReTweet) ImageView ivReTweet;
    @BindView(R.id.ivFav) ImageView ivFav;
    @BindView(R.id.ivUserImage) ImageView ivUserImage;
    @BindView(R.id.ivMediaImage) ImageView ivMediaImage;
    @BindView(R.id.btnTweet) Button btnTweet;

    private UserProfileModel userProfileModel;
    private String userId;

    public static final String USER_ID = "user_id";
    public static final String USER_PROFILE = "user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_detail);

        ButterKnife.bind(TweetsDetailActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TweetModel tweetDetail = (TweetModel) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(tweetDetail.getTimeStamp());
            String timestamp = sdf.format(date);
            tvTimeStamp.setText(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvUserName.setText(tweetDetail.getUserName());
        tvUserId.setText(tweetDetail.getUserId());
        tvBody.setText(tweetDetail.getBody());
        tvRetweet.setText(tweetDetail.getRetweetCount().toString());
        tvFavorites.setText(tweetDetail.getFavoriteCount().toString());
        Glide.with(TweetsDetailActivity.this).load(Uri.parse(tweetDetail.getProfileImage())).
                bitmapTransform(new RoundedCornersTransformation(TweetsDetailActivity.this,5,5)).into(ivUserImage);
        if (!TextUtils.isEmpty(tweetDetail.getMediaImageURL())){
            ivMediaImage.setVisibility(View.VISIBLE);
            Glide.with(TweetsDetailActivity.this).load(Uri.parse(tweetDetail.getMediaImageURL())).into(ivMediaImage);
        }else{
            ivMediaImage.setVisibility(View.GONE);
        }
    }
}
