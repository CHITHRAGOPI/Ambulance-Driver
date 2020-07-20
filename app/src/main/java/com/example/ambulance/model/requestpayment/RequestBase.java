package com.example.ambulance.model.requestpayment;

import com.google.gson.annotations.SerializedName;

public class RequestBase{

    @SerializedName("success")
    private boolean success;

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean isSuccess(){
        return success;
    }


}
