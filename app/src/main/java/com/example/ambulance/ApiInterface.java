package com.example.ambulance;


import com.example.ambulance.model.hospital.HospitaBase;
import com.example.ambulance.model.kmsrate.KmsRateBase;
import com.example.ambulance.model.location.LocationBase;
import com.example.ambulance.model.login.LoginBase;
import com.example.ambulance.model.payment.PaymentBase;
import com.example.ambulance.model.requests.UserRequestBase;
import com.example.ambulance.model.requestpayment.RequestBase;
import com.example.ambulance.model.trip.TripBase;
import com.example.ambulance.model.updatepayment.PaymentUpdateListBase;
import com.example.ambulance.model.userlogin.UserLoginBase;

import retrofit2.Call;

import retrofit2.http.GET;

import retrofit2.http.Query;

public interface ApiInterface {

    @GET("login_driver.php")
    Call<LoginBase> userLogin(@Query("username") String username, @Query("password") String password);

//
    @GET("update_location.php")
    Call<LocationBase> updateLocation(@Query("id") String id,
                                     @Query("lat") String longitude,
                                     @Query("lng") String latitude);




    @GET("getPayment.php")
    Call<PaymentUpdateListBase> getPayment(@Query("user_id") String userId);


    @GET("paymentUpdate.php")
    Call<LocationBase> updatePayment(@Query("status") String userId,
                                     @Query("payment_id") String paymentId);


    @GET("update_driver_status.php")
    Call<LocationBase> updateDriverStatus(@Query("status") String status,
                                     @Query("user_id") String userID);





    @GET("getRequest.php")
    Call<UserRequestBase> getRequest(@Query("latitude") String latitude,@Query("longitude")  String longitude);



    @GET("insert_payment.php")
    Call<PaymentBase> insertPayment(@Query("request_id") String requestId,
                                    @Query("amount")  String amount,
                                    @Query("ambulance_no")  String ambulanceNo,
                                    @Query("user_id") String userID);





    @GET("getHospital.php")
    Call<HospitaBase> getHospital(@Query("latitude") String latitude,@Query("longitude") String longitude);

    @GET("update_request.php")
    Call<RequestBase> updateRequest(@Query("ambulance_id") String ambulanceId,
                                    @Query("hospital_id") String hospitalId,
                                    @Query("req_id") String reqId


    );

    @GET("getUser.php")
    Call<UserLoginBase> getUser(@Query("user_id") String reqId);


    @GET("getKmRate.php")
    Call<KmsRateBase> getKmRate();



    @GET("update_request_complete.php")
    Call<RequestBase> updateRequestComplete(@Query("req_id") String reqId);






    @GET("getTrip.php")
    Call<TripBase> getTrip(@Query("id") String id);



    @GET("getHospitalById.php")
    Call<HospitaBase> getHospitalId(@Query("id") String id);

}


