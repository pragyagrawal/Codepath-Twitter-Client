package com.codepath.apps.Twitterbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class UserDMModel implements Parcelable{
	private String currentUserId;
	private String currentUserName;
	private String currentUserProfileImage;
	private String userMessage;

	public UserDMModel(JSONObject jsonObject) {
		this.currentUserId = jsonObject.optString("recipient_screen_name");
		this.currentUserName = jsonObject.optJSONObject("recipient").optString("name");
		this.currentUserProfileImage = jsonObject.optJSONObject("recipient").optString("profile_image_url_https");
		this.userMessage = jsonObject.optString("text");
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

	public String getCurrentUserProfileImage() {
		return currentUserProfileImage;
	}

	public void setCurrentUserProfileImage(String currentUserProfileImage) {
		this.currentUserProfileImage = currentUserProfileImage;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	protected UserDMModel(Parcel in) {
		currentUserId = in.readString();
		currentUserName = in.readString();
		currentUserProfileImage = in.readString();
		userMessage = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(currentUserId);
		dest.writeString(currentUserName);
		dest.writeString(currentUserProfileImage);
		dest.writeString(userMessage);
	}

	@SuppressWarnings("unused")
	public static final Creator<UserDMModel> CREATOR = new Creator<UserDMModel>() {
		@Override
		public UserDMModel createFromParcel(Parcel in) {
			return new UserDMModel(in);
		}

		@Override
		public UserDMModel[] newArray(int size) {
			return new UserDMModel[size];
		}
	};
}
