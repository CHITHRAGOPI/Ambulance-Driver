package com.example.ambulance.model.kmsrate;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class KmsRateBase{

	@SerializedName("data")
	private List<KmsRateItem> data;

	@SerializedName("success")
	private boolean success;

	public List<KmsRateItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}