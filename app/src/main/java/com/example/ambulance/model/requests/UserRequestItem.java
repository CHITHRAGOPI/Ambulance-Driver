package com.example.ambulance.model.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserRequestItem  implements Serializable {

	@SerializedName("device_id")
	private String deviceId;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("vehicle_no")
	private String vehicleNo;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("ambulance_id")
	private String ambulanceId;

	@SerializedName("remark")
	private String remark;

	@SerializedName("id")
	private String id;

	@SerializedName("hospital_id")
	private String hospitalId;

	@SerializedName("longitude")
	private String longitude;

	@SerializedName("timestamp")
	private String timestamp;

	@SerializedName("status")
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setVehicleNo(String vehicleNo){
		this.vehicleNo = vehicleNo;
	}

	public String getVehicleNo(){
		return vehicleNo;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setAmbulanceId(String ambulanceId){
		this.ambulanceId = ambulanceId;
	}

	public String getAmbulanceId(){
		return ambulanceId;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setHospitalId(String hospitalId){
		this.hospitalId = hospitalId;
	}

	public String getHospitalId(){
		return hospitalId;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"device_id = '" + deviceId + '\'' + 
			",user_id = '" + userId + '\'' + 
			",vehicle_no = '" + vehicleNo + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",ambulance_id = '" + ambulanceId + '\'' + 
			",remark = '" + remark + '\'' + 
			",id = '" + id + '\'' + 
			",hospital_id = '" + hospitalId + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}
}