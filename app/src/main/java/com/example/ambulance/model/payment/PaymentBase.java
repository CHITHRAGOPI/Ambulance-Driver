package com.example.ambulance.model.payment;


import com.google.gson.annotations.SerializedName;

public class PaymentBase{

    @SerializedName("success")
    private boolean success;

    public void setSuccess(boolean success){
        this.success = success;
    }

    public boolean isSuccess(){
        return success;
    }
}
