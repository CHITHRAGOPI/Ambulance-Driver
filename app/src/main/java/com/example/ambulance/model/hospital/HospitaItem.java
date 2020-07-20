package com.example.ambulance.model.hospital;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HospitaItem implements Serializable {

	@SerializedName("address")
	private String address;

	@SerializedName("distance")
	private String distance;

	@SerializedName("name")
	private String name;

	@SerializedName("lon")
	private String lon;

	@SerializedName("id")
	private String id;

	@SerializedName("lat")
	private String lat;

	public String getAddress(){
		return address;
	}

	public String getDistance(){
		return distance;
	}

	public String getName(){
		return name;
	}

	public String getLon(){
		return lon;
	}

	public String getId(){
		return id;
	}

	public String getLat(){
		return lat;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"address = '" + address + '\'' + 
			",distance = '" + distance + '\'' + 
			",name = '" + name + '\'' + 
			",lon = '" + lon + '\'' + 
			",id = '" + id + '\'' + 
			",lat = '" + lat + '\'' + 
			"}";
		}
}