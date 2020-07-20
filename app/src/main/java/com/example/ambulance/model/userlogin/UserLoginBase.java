package com.example.ambulance.model.userlogin;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserLoginBase{

	@SerializedName("data")
	private List<UserLoginItem> data;

	@SerializedName("success")
	private boolean success;

	public List<UserLoginItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}