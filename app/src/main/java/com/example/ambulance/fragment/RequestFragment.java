package com.example.ambulance.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ambulance.ApiClient;
import com.example.ambulance.ApiInterface;
import com.example.ambulance.HomeActivity;
import com.example.ambulance.HospitalActivity;
import com.example.ambulance.R;
import com.example.ambulance.adapter.RequestAdapter;
import com.example.ambulance.model.requests.UserRequestBase;
import com.example.ambulance.model.requests.UserRequestItem;
import com.example.ambulance.utils.GlobalPreference;
import com.example.ambulance.utils.GpsTrackers;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment implements RequestAdapter.OnRequestClickedListener  {

    @BindView(R.id.request_recyclerview)
    RecyclerView mRequestRecyclerView;


    private GlobalPreference mGlobalPreference;
    private ApiInterface mApiInterface;
    public static final String TAG = HomeActivity.class.getSimpleName();

    private ArrayList<UserRequestItem> mUserRequestItems=new ArrayList<>();

    private RequestAdapter mRequestAdapter;




GpsTrackers mGpsTrackers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setUp();



    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                  //  if(mGlobalPreference.getAvailableCheck()){
                        getData();
                    //}
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private  void setUp(){
        mGpsTrackers=new GpsTrackers(getActivity());
        mGlobalPreference=new GlobalPreference(Objects.requireNonNull(getActivity()));
        mRequestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRequestAdapter=new RequestAdapter(mUserRequestItems,getActivity());
        mRequestRecyclerView.setAdapter(mRequestAdapter);
        mRequestAdapter.setOnRequestClickedListener(this);
    }

    public  void getData(){
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<UserRequestBase> mLocationBaseCall=mApiInterface.
                getRequest(String.valueOf(mGpsTrackers.getLatitude()),String.valueOf(mGpsTrackers.getLongitude()));
        mLocationBaseCall.enqueue(new Callback<UserRequestBase>() {
            @Override
            public void onResponse(Call<UserRequestBase> call, Response<UserRequestBase> response) {
                mUserRequestItems.clear();
                mUserRequestItems.addAll(response.body().getData());
                mRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserRequestBase> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });


    }

    @Override
    public void onClicked(int position) {
        UserRequestItem userRequestItem=mUserRequestItems.get(position);
        Intent intent=new Intent(getActivity(), HospitalActivity.class);
        intent.putExtra("userRequest",userRequestItem);
        startActivity(intent);
    }

}
