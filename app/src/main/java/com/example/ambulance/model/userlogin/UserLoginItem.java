package com.example.ambulance.model.userlogin;

import com.google.gson.annotations.SerializedName;

public class UserLoginItem {

	@SerializedName("password")
	private String password;

	@SerializedName("address")
	private String address;

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("phone")
	private String phone;

	@SerializedName("vehicle_no")
	private String vehicleNo;

	@SerializedName("emergency_no")
	private String emergencyNo;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private String id;

	@SerializedName("username")
	private String username;

	public String getPassword(){
		return password;
	}

	public String getAddress(){
		return address;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public String getPhone(){
		return phone;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public String getEmergencyNo(){
		return emergencyNo;
	}

	public String getName(){
		return name;
	}

	public String getId(){
		return id;
	}

	public String getUsername(){
		return username;
	}
}