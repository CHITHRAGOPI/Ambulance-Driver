package com.example.ambulance.model.requests;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class UserRequestBase{

	@SerializedName("data")
	private List<UserRequestItem> data;

	@SerializedName("success")
	private boolean success;

	public void setData(List<UserRequestItem> data){
		this.data = data;
	}

	public List<UserRequestItem> getData(){
		return data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"UserRequestBase{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}