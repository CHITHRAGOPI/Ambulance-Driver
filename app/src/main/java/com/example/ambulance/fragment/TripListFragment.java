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
import com.example.ambulance.MyTripDetailActivity;
import com.example.ambulance.R;
import com.example.ambulance.TripDetailActivity;
import com.example.ambulance.adapter.TripAdapter;
import com.example.ambulance.model.trip.TripBase;
import com.example.ambulance.model.trip.TripItem;
import com.example.ambulance.utils.GlobalPreference;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripListFragment extends Fragment implements TripAdapter.OnTripClickedListener {


    @BindView(R.id.trip_recyclerview)
    RecyclerView mTripRecyclerView;


    private GlobalPreference mGlobalPreference;
    private ApiInterface mApiInterface;

    private ArrayList<TripItem> mTripItemArrayList;
    private static final String TAG = "TripListFragment";

     private TripAdapter mTripAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        setUp();
        getData();
    }

    private void setUp(){
        mGlobalPreference=new GlobalPreference(Objects.requireNonNull(getActivity()));
        mTripItemArrayList=new ArrayList<>();
        mTripRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTripAdapter=new TripAdapter(mTripItemArrayList,getActivity());
        mTripRecyclerView.setAdapter(mTripAdapter);
        mTripAdapter.setOnTripClickedListener(this);
    }

    private  void getData(){
     mApiInterface= ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
     Call<TripBase> mTripBaseCall=mApiInterface.getTrip(mGlobalPreference.getID());
     mTripBaseCall.enqueue(new Callback<TripBase>() {
         @Override
         public void onResponse(Call<TripBase> call, Response<TripBase> response) {
             mTripItemArrayList.clear();
             mTripItemArrayList.addAll(response.body().getData());
             mTripAdapter.notifyDataSetChanged();
         }

         @Override
         public void onFailure(Call<TripBase> call, Throwable t) {
             Log.d(TAG, "onFailure: "+t.getMessage());
         }
     });

    }

    @Override
    public void onClicked(int position) {
        Intent intent=new Intent(getActivity(), MyTripDetailActivity.class);
        intent.putExtra("request",mTripItemArrayList.get(position));
        startActivity(intent);

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
}
