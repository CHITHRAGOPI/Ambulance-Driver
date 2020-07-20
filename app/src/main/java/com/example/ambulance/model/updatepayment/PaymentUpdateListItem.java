package com.example.ambulance.model.updatepayment;

import com.google.gson.annotations.SerializedName;

public class PaymentUpdateListItem {

	@SerializedName("ambulance_no")
	private String ambulanceNo;

	@SerializedName("amount")
	private String amount;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("remark")
	private String remark;

	@SerializedName("id")
	private String id;

	@SerializedName("request_id")
	private String requestId;

	@SerializedName("status")
	private String status;

	public String getAmbulanceNo(){
		return ambulanceNo;
	}

	public String getAmount(){
		return amount;
	}

	public String getUserId(){
		return userId;
	}

	public String getRemark(){
		return remark;
	}

	public String getId(){
		return id;
	}

	public String getRequestId(){
		return requestId;
	}

	public String getStatus(){
		return status;
	}
}