package com.example.ambulance.model.kmsrate;

import com.google.gson.annotations.SerializedName;

public class KmsRateItem {

	@SerializedName("amount")
	private String amount;

	@SerializedName("id")
	private String id;

	public String getAmount(){
		return amount;
	}

	public String getId(){
		return id;
	}
}