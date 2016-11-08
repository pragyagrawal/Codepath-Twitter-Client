package com.codepath.apps.Twitterbook.models;

import com.codepath.apps.Twitterbook.database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by PRAGYA on 10/28/2016.
 */

@Table(database = MyDatabase.class)
@Parcel(analyze={TweetModel.class})   // add Parceler annotation here
public class TweetModel extends BaseModel {

    //Define database columns and associated fields
    @PrimaryKey
    @Column
    Long id;

    @Column
    String userId;
    @Column
    String userName;
    @Column
    String body;
    @Column
    String timeStamp;

    @Column
    String profileImage;

    @Column
    Integer favoriteCount;

    @Column
    Integer retweetCount;

    @Column
    Boolean isFavorite;

    @Column
    Boolean isRetweeted;

    @Column
    String mediaImageURL;

    @Column
    String id_str;

    public TweetModel() {
    }

    public TweetModel(JSONObject object) {
        try {
            JSONObject userObject = object.getJSONObject("user");
            JSONArray mediaArrayObject = object.optJSONObject("entities").optJSONArray("media");

            this.userId = userObject.getString("screen_name");
            this.userName = userObject.getString("name");
            this.profileImage = userObject.getString("profile_image_url_https");
            this.timeStamp = object.getString("created_at");
            this.body = object.getString("text");
            this.isFavorite=object.getBoolean("favorited");
            this.isRetweeted=object.getBoolean("retweeted");
            this.favoriteCount=object.getInt("favorite_count");
            this.retweetCount=object.getInt("retweet_count");
            this.id_str = object.getString("id_str");

            if(mediaArrayObject!=null && mediaArrayObject.length()>0)
            {
                this.mediaImageURL = mediaArrayObject.getJSONObject(0).getString("media_url_https");
            }

        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    public static ArrayList<TweetModel> fromJson(JSONArray jsonArray){
        ArrayList<TweetModel> tweets = new ArrayList<>(jsonArray.length());

        for (int i=0; i< jsonArray.length(); i++){
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
            TweetModel tweetModel = new TweetModel(tweetJson);
            tweetModel.save();
            tweets.add(tweetModel);
        }
        return tweets;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getBody() {
        return body;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Boolean isFavorite() {
        return isFavorite;
    }

    public Boolean isRetweeted() {
        return isRetweeted;
    }

    public String getMediaImageURL() {
        return mediaImageURL;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }
}
