package com.codepath.apps.Twitterbook.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileModel implements Parcelable{
	private String currentUserId;
	private String currentUserName;
	private String currentUserProfile;


	public UserProfileModel(JSONObject jsonObject) {

		try {
			this.currentUserId = jsonObject.getString("screen_name");
			this.currentUserName = jsonObject.getString("name");
			this.currentUserProfile = jsonObject.getString("profile_image_url_https");
		} catch (JSONException e) {
			e.printStackTrace();
		}
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

	protected UserProfileModel(Parcel in) {
		currentUserId = in.readString();
		currentUserName = in.readString();
		currentUserProfile = in.readString();
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
