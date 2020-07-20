package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ambulance.adapter.HospitalAdapter;
import com.example.ambulance.model.hospital.HospitaBase;
import com.example.ambulance.model.hospital.HospitaItem;
import com.example.ambulance.model.requests.UserRequestItem;
import com.example.ambulance.utils.GlobalPreference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalActivity extends AppCompatActivity implements HospitalAdapter.OnHospitalClickedListener {

    UserRequestItem mUserRequestItem;
    private static final String TAG = "AmbulanceActivity";

    @BindView(R.id.hospitalRecyclerView)
    RecyclerView mHospitalRecylerView;

    private ApiInterface mApiInterface;

    private HospitalAdapter mHospitalAdapter;

    private ArrayList<HospitaItem> mHosiptalList;
    private Toolbar mToolbar;
    GlobalPreference mGlobalPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        ButterKnife.bind(this);
        setUp();
        getData();
    }

    private void setUp() {
        mHosiptalList=new ArrayList<>();
        mGlobalPreference=new GlobalPreference(this);
        mToolbar=findViewById(R.id.toolbarTrip);
        mToolbar.setNavigationIcon(R.drawable.ic_back); // your drawable
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setTitle("Hospital");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        mHospitalRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mHospitalAdapter=new HospitalAdapter(mHosiptalList,this);
        mHospitalRecylerView.setAdapter(mHospitalAdapter);
        mHospitalAdapter.setOnHospitalClickedListener(this);
        mUserRequestItem= (UserRequestItem) getIntent().getSerializableExtra("userRequest");
        Log.d(TAG, "setUp: "+mUserRequestItem.getVehicleNo());
    }

    public void getData(){
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<HospitaBase> mHospitalBaseCall=mApiInterface.getHospital(mUserRequestItem.getLatitude(),mUserRequestItem.getLongitude());
        mHospitalBaseCall.enqueue(new Callback<HospitaBase>() {
            @Override
            public void onResponse(Call<HospitaBase> call, Response<HospitaBase> response) {
                if(response.body().isSuccess()) {
                    mHosiptalList.clear();
                    mHosiptalList.addAll(response.body().getData());
                    mHospitalAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(HospitalActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HospitaBase> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


    @Override
    public void onClicked(int position) {
        Intent intent=new Intent(this,TripDetailActivity.class);
        intent.putExtra("request",mUserRequestItem);
        intent.putExtra("hospital",mHosiptalList.get(position));
        startActivity(intent);

    }
}