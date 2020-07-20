package com.example.ambulance.model.updatepayment;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentUpdateListBase {

	@SerializedName("data")
	private List<PaymentUpdateListItem> data;

	@SerializedName("success")
	private boolean success;

	public List<PaymentUpdateListItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}
}