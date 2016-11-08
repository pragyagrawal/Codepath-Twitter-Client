package com.codepath.apps.Twitterbook.network;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "kERvIIisWeRctxbg49NHT9Qor";       // Change this
	public static final String REST_CONSUMER_SECRET = "aEmCSGOBaSoINIfYgwqRxXPQ8d937Ihmjs5W3bpCYDCJmiYHxL"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://codepathtweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

	public void getUsersProfile(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		getClient().get(apiUrl, handler);
	}

	public void getUsersProfileDetails(String screenName,AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("screen_name", screenName);
		params.put("include_entities","true");
		getClient().get(apiUrl,params, handler);
	}

	// DEFINE METHODS for different API endpoints here
	public void getHomeTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	//Mentions timeline method
	public void getMentionsTimeline(int page, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		getClient().get(apiUrl, params, handler);
	}

	//users timeline method
	public void getUsersTweets(int page, String screenName, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		params.put("screen_name", screenName);
		params.put("include_entities","true");
		getClient().get(apiUrl, params, handler);
	}

	//Favorite Tweets method
	public void getFavoriteTweets(int page, String userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(page));
		params.put("screen_name", userId);
		getClient().get(apiUrl, params, handler);
	}

	//User followers method
	public void getUsersFollowers(int page, String userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("next_cursor", String.valueOf(page));
		params.put("screen_name", userId);
		getClient().get(apiUrl, params, handler);
	}


	//User following method
	public void getUsersFollowing(int page, String userId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("followers/list.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("next_cursor", String.valueOf(page));
		params.put("screen_name", userId);
		getClient().get(apiUrl, params, handler);
	}

	//User following method
	public void getDirectMessageSent(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("direct_messages/sent.json");
		getClient().get(apiUrl, handler);
	}
	
	//post tweets method
	public void postTweet(String body, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", body);
		getClient().post(apiUrl, params, handler);
	}
}