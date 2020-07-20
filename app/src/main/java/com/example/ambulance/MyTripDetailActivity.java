package com.example.ambulance;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ambulance.model.hospital.HospitaBase;
import com.example.ambulance.model.hospital.HospitaItem;
import com.example.ambulance.model.requestpayment.RequestBase;
import com.example.ambulance.model.trip.TripItem;
import com.example.ambulance.utils.GlobalPreference;
import com.example.ambulance.utils.GpsTrackers;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTripDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbarTrip)
    Toolbar mToolbar;

    GlobalPreference mGlobalPreference;

    @BindView(R.id.mytrip_time_text)
    TextView mUserTimeTextView;

    @BindView(R.id.mytrip_hospitalName_text)
    TextView mHospitalNameTextView;


    @BindView(R.id.accidentButtonMap)
    Button mAccidentMapButton;

    public static boolean isTripCompleted=false;


    @BindView(R.id.accidentHospitalButton)
    Button mAccidentHospitalButton;


    @BindView(R.id.trip_complete_button)
    Button mCompleteButton;


    @BindView(R.id.update_payment_button)
    Button mPaymentStatusButton;



    @BindView(R.id.mytrip_hospitalAddress_text)
    TextView mHospitalAddressTextView;
    GpsTrackers mGpsTrackers;

    HospitaItem mHospitaItem;
    TripItem mTripItem;

    ApiInterface mApiInterface;
    private static final String TAG = "MyTripDetailActivity";

    private String requestId;

    private String userId;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrip_detail);
        ButterKnife.bind(this);
        setUp();
        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUp() {
        isTripCompleted=false;
        mGpsTrackers=new GpsTrackers(this);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mToolbar.setTitle("Trip Details");
        mGlobalPreference = new GlobalPreference(this);
        mToolbar.setNavigationIcon(R.drawable.ic_back); // your drawable
        mToolbar.setNavigationOnClickListener(v -> finish());
        mAccidentMapButton.setOnClickListener(v -> {
            loadMap(mTripItem.getLatitude(),mTripItem.getLongitude(),mHospitaItem.getLat(),mHospitaItem.getLon());

        });

        mAccidentHospitalButton.setOnClickListener(v -> {
            loadMap(String.valueOf(mGpsTrackers.getLatitude()),String.valueOf(mGpsTrackers.getLongitude()),mTripItem.getLatitude(),mTripItem.getLongitude());

        });
        mTripItem= (TripItem) getIntent().getSerializableExtra("request");
        if(mTripItem.getStatus().equals("0")){
            mCompleteButton.setVisibility(View.VISIBLE);
            mPaymentStatusButton.setVisibility(View.GONE);

        }else{
            mCompleteButton.setVisibility(View.GONE);
            mPaymentStatusButton.setVisibility(View.VISIBLE);

        }
        mCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });

        mPaymentStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MyTripDetailActivity.this,PaymentStatusActivity.class);
                intent.putExtra("request_id",mTripItem.getId());
                intent.putExtra("user_id",mTripItem.getUserId());
                startActivity(intent);

            }
        });



    }
    public void updateStatus(){
        mApiInterface=ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<RequestBase> mRequestBaseCall=mApiInterface.updateRequestComplete(mTripItem.getId());
        mRequestBaseCall.enqueue(new Callback<RequestBase>() {
            @Override
            public void onResponse(Call<RequestBase> call, Response<RequestBase> response) {
                Log.d(TAG, "onResponse: trip detail"+response.body().isSuccess());

                Intent intent=new Intent(MyTripDetailActivity.this,PaymentActivity.class);
                intent.putExtra("request_id",mTripItem.getId());
                intent.putExtra("user_id",mTripItem.getUserId());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RequestBase> call, Throwable t) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isTripCompleted){
            mCompleteButton.setVisibility(View.GONE);
            mPaymentStatusButton.setVisibility(View.VISIBLE);
        }

    }

    private void getData(){
     mApiInterface=ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
     Call<HospitaBase> mHospitaBaseCall=mApiInterface.getHospitalId(mTripItem.getHospitalId());
        Log.d(TAG, "getData: id"+mTripItem.getId());
        mHospitaBaseCall.enqueue(new Callback<HospitaBase>() {
         @Override
         public void onResponse(Call<HospitaBase> call, Response<HospitaBase> response) {
             Log.d(TAG, "onResponse:trip "+response);
             if(response.isSuccessful()){
                 mHospitaItem=response.body().getData().get(0);
                 mHospitalNameTextView.setText(mHospitaItem.getName());
                 mHospitalAddressTextView.setText(mHospitaItem.getAddress());
                 mUserTimeTextView.setText(mTripItem.getTimestamp());


             }

         }

         @Override
         public void onFailure(Call<HospitaBase> call, Throwable t) {
             Log.d(TAG, "onFailure: "+t.getMessage());
         }
     });


    }



    public void loadMap(String srcLat,String srcLng,String desLat,String desLng){
        final Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(
                        "http://maps.google.com/maps?" +
                                "saddr="+srcLat+","+srcLng+
                                "&daddr="+desLat+","+desLng));

        intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");

        startActivity(intent);
    }
}
