package com.example.ambulance.model.trip;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TripBase {

	@SerializedName("data")
	private List<TripItem> data;

	@SerializedName("success")
	private boolean success;

	public void setData(List<TripItem> data){
		this.data = data;
	}

	public List<TripItem> getData(){
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
			"TripResponse{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}