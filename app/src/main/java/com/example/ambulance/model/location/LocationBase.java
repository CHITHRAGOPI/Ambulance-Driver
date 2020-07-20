package com.example.ambulance.model.location;

import com.google.gson.annotations.SerializedName;

public class LocationBase {

    @SerializedName("success")
    private boolean success;

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean isSuccess(){
        return success;
    }


}
