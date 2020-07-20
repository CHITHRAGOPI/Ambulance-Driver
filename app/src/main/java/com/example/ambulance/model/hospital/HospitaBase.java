package com.example.ambulance.model.hospital;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class HospitaBase{

	@SerializedName("data")
	private List<HospitaItem> data;

	@SerializedName("success")
	private boolean success;

	public List<HospitaItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	@Override
 	public String toString(){
		return 
			"HospitaBase{" + 
			"data = '" + data + '\'' + 
			",success = '" + success + '\'' + 
			"}";
		}
}