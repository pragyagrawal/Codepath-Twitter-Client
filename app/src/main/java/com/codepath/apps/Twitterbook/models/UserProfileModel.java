package com.codepath.apps.Twitterbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class UserProfileModel implements Parcelable{
	private String currentUserId;
	private String currentUserName;
	private String currentUserProfile;
	private String currentUserProfileBackground;
	private String currentUserFollowersCount;
	private String currentUserFollowing;
	private String userProfileDescription;

	public UserProfileModel(JSONObject jsonObject) {
			this.currentUserId = jsonObject.optString("screen_name");
			this.currentUserName = jsonObject.optString("name");
			this.currentUserProfile = jsonObject.optString("profile_image_url_https");
			this.currentUserProfileBackground = jsonObject.optString("profile_banner_url");
			this.currentUserFollowersCount = jsonObject.optString("followers_count");
			this.currentUserFollowing = jsonObject.optString("friends_count");
			this.userProfileDescription = jsonObject.optString("description");
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getCurrentUserName() {

		return currentUserName;
	}

	public void setCurrentUserName(String currentUserName) {
		this.currentUserName = currentUserName;
	}

	public String getCurrentUserProfile() {
		return currentUserProfile;
	}

	public void setCurrentUserProfile(String currentUserProfile) {
		this.currentUserProfile = currentUserProfile;
	}

	public String getCurrentUserProfileBackground() {
		return currentUserProfileBackground;
	}

	public void setCurrentUserProfileBackground(String currentUserProfileBackground) {
		this.currentUserProfileBackground = currentUserProfileBackground;
	}

	public String getCurrentUserFollowersCount() {
		return currentUserFollowersCount;
	}

	public void setCurrentUserFollowersCount(String currentUserFollowersCount) {
		this.currentUserFollowersCount = currentUserFollowersCount;
	}

	public String getCurrentUserFollowing() {
		return currentUserFollowing;
	}

	public void setCurrentUserFollowing(String currentUserFollowing) {
		this.currentUserFollowing = currentUserFollowing;
	}

	public String getUserProfileDescription() {
		return userProfileDescription;
	}

	public void setUserProfileDescription(String userProfileDescription) {
		this.userProfileDescription = userProfileDescription;
	}

	protected UserProfileModel(Parcel in) {
		currentUserId = in.readString();
		currentUserName = in.readString();
		currentUserProfile = in.readString();
		currentUserProfileBackground = in.readString();
		currentUserFollowing = in.readString();
		currentUserFollowersCount = in.readString();
		userProfileDescription = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(currentUserId);
		dest.writeString(currentUserName);
		dest.writeString(currentUserProfile);
		dest.writeString(currentUserProfileBackground);
		dest.writeString(currentUserFollowing);
		dest.writeString(currentUserFollowersCount);
		dest.writeString(userProfileDescription);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<UserProfileModel> CREATOR = new Parcelable.Creator<UserProfileModel>() {
		@Override
		public UserProfileModel createFromParcel(Parcel in) {
			return new UserProfileModel(in);
		}

		@Override
		public UserProfileModel[] newArray(int size) {
			return new UserProfileModel[size];
		}
	};
}
