package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambulance.adapter.HospitalAdapter;
import com.example.ambulance.model.hospital.HospitaItem;
import com.example.ambulance.model.requests.UserRequestItem;
import com.example.ambulance.model.requestpayment.RequestBase;
import com.example.ambulance.model.userlogin.UserLoginBase;
import com.example.ambulance.model.userlogin.UserLoginItem;
import com.example.ambulance.utils.GlobalPreference;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripDetailActivity extends AppCompatActivity {

    private ApiInterface mApiInterface;

    private HospitalAdapter mHospitalAdapter;

    @BindView(R.id.trip_UserAddress_text)
    TextView mUserAddressTextView;

    @BindView(R.id.trip_userVehicle_text)
    TextView mUserVehicleTextView;

    @BindView(R.id.trip_time_text)
    TextView mUserTimeTextView;

    @BindView(R.id.trip_hospitalName_text)
    TextView mHospitalNameTextView;

    @BindView(R.id.trip_hospitalAddress_text)
    TextView mHospitalAddressTextView;

    @BindView(R.id.toolbarTrip)
    Toolbar mToolbar;

    private UserRequestItem mUserRequestItem;
    private HospitaItem mHospitalItem;
    private static final String TAG = "TripDetailActivity";

    GlobalPreference mGlobalPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        ButterKnife.bind(this);
        setUp();
        setData();

    }

    private void setUp() {

        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setTitle("Trip");
        mGlobalPreference = new GlobalPreference(this);

        mToolbar.setNavigationIcon(R.drawable.ic_back); // your drawable
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUserRequestItem = (UserRequestItem) getIntent().getSerializableExtra("request");
        mHospitalItem = (HospitaItem) getIntent().getSerializableExtra("hospital");
    }

    private void setData() {
        mUserAddressTextView.setText(getCompleteAddressString(Double.parseDouble(mUserRequestItem.getLatitude()), Double.parseDouble(mUserRequestItem.getLongitude())).trim());
        mUserVehicleTextView.setText(mUserRequestItem.getVehicleNo());
        mUserTimeTextView.setText(mUserRequestItem.getTimestamp());
        mHospitalAddressTextView.setText(mHospitalItem.getAddress());
        mHospitalNameTextView.setText(mHospitalItem.getName());
    }

    
    public void accept(View view){
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<RequestBase> mRequestBaseCall=mApiInterface.updateRequest(mGlobalPreference.getID(),mHospitalItem.getId(),mUserRequestItem.getId());
        mRequestBaseCall.enqueue(new Callback<RequestBase>() {
            @Override
            public void onResponse(Call<RequestBase> call, Response<RequestBase> response) {
                Log.d(TAG, "onResponse:accept "+response.isSuccessful());
                Toast.makeText(TripDetailActivity.this, "Please wait your request is proccessing", Toast.LENGTH_SHORT).show();

                if(response.isSuccessful()){
                   callUserDetails();
                   mGlobalPreference.setAvailableStatus(false);
               }

              //   callUserDetails();

                //callSmsHospitalDetails();


            }

            @Override
            public void onFailure(Call<RequestBase> call, Throwable t) {
                Toast.makeText(TripDetailActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

   public  void callUserDetails(){
       mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
      Call<UserLoginBase> mLoginBaseCall=mApiInterface.getUser(mUserRequestItem.getUserId());
      mLoginBaseCall.enqueue(new Callback<UserLoginBase>() {
          @Override
          public void onResponse(Call<UserLoginBase> call, Response<UserLoginBase> response) {
              Log.d(TAG, "onResponse: "+response);
              if(response.body().isSuccess()){
                  UserLoginItem mUserLoginItem=response.body().getData().get(0);
                  Log.d(TAG, "onResponse:number "+mUserLoginItem.getEmergencyNo());
                  callSmsHospitalDetails(mUserLoginItem);
              }
          }
          @Override
          public void onFailure(Call<UserLoginBase> call, Throwable t) {

          }
      });




    }

    private void callSmsHospitalDetails(UserLoginItem userLoginItem){


        mGlobalPreference.setEmergencyPhoneNo(userLoginItem.getEmergencyNo());
        mGlobalPreference.setUserPhoneNumber(userLoginItem.getPhone());
//        String hospitalAddress=getCompleteAddressString(Double.parseDouble(mHospitalItem.getLat()),Double.parseDouble(mHospitalItem.getLon()));
        String message="You friend @"+userLoginItem.getPhone()+"met with and accident and hospitalized in "+mHospitalItem.getName() +"hospital location "
                +"https://www.google.com/maps/dir/"+mHospitalItem.getLat()+","+mHospitalItem.getLon();

        String url="http://akrut.in/SMS/gateway.php?mobile="+userLoginItem.getEmergencyNo()+"&msg="+message;

        Log.d(TAG, "callSmsHospitalDetails: "+url);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
                startActivity(new Intent(TripDetailActivity.this,HomeActivity.class));
                Log.d(TAG, "onResponse: "+response);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue mRequestQueue= Volley.newRequestQueue(this);
        mRequestQueue.add(stringRequest);




    }

    private String getCompleteAddressString(double lat, double lng) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
    }

}
