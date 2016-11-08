package com.codepath.apps.Twitterbook.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.Twitterbook.R;
import com.codepath.apps.Twitterbook.models.TweetModel;
import com.codepath.apps.Twitterbook.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
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
    @BindView(R.id.rvReplyView) RelativeLayout rvReplyView;

    private TweetModel tweetDetail;
    private String userId;
    private TwitterClient twitterClient;

    public static final String USER_ID = "user_id";
    public static final String USER_PROFILE = "user_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_detail);

        ButterKnife.bind(TweetsDetailActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tweetDetail = (TweetModel) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        twitterClient = new TwitterClient(this);

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

        etReply.addTextChangedListener(textEditorWatcher);

        if(tweetDetail.isFavorite())
        {
            applyFavLogic();
        }
        if(tweetDetail.isRetweeted())
        {
            applyRetweetLogic();
        }
    }

    @OnClick({R.id.ivReplyTweet,R.id.etReply})
    void onReplyTweet(){
        etReply.setText("@"+tweetDetail.getUserId());
        rvReplyView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.ivReTweet)
    void onRetweetTweet(){
        twitterClient.postReTweet(tweetDetail.getId_str(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                applyRetweetLogic();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @OnClick(R.id.ivFav)
    void onFavTweet(){

        twitterClient.favoriteTweet(tweetDetail.getId_str(),new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                applyFavLogic();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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

    private void applyRetweetLogic()
    {
        ivReTweet.setColorFilter(ContextCompat.getColor(this,android.R.color.holo_green_dark));
    }

    private void applyFavLogic()
    {
        ivFav.setColorFilter(ContextCompat.getColor(this,android.R.color.holo_red_dark));
    }
}
