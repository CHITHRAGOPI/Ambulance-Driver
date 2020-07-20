package com.example.ambulance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


import com.example.ambulance.adapter.RequestAdapter;
import com.example.ambulance.fragment.RequestFragment;
import com.example.ambulance.fragment.TripListFragment;
import com.example.ambulance.model.location.LocationBase;
import com.example.ambulance.model.requests.UserRequestBase;
import com.example.ambulance.model.requests.UserRequestItem;
import com.example.ambulance.utils.GlobalPreference;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    GlobalPreference mGlobalPreference;
    private static final String TAG = "HomeActivity";
    private ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mGlobalPreference = new GlobalPreference(getApplicationContext());
        startService(new Intent(HomeActivity.this, LocationService.class));
        ButterKnife.bind(this);
        toolbar.setTitle("Requests");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        replaceFragment(new RequestFragment() );
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.recent_request_menu:
                    replaceFragment(new RequestFragment());
                    break;
                case R.id.mytrip_menu:
                    toolbar.setTitle("My Trip");
                    toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
                    replaceFragment(new TripListFragment());
                    break;

            }
            return true;

        });

        bottomNavigationView.getMenu().getItem(0).setChecked(true);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()/*.addToBackStack("")*/;
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }
//



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("location-event"));
        statusApi("Available");
    }

    @Override
    protected void onPause() {
        super.onPause();
        statusApi("UnAvailable");

    }

    public void statusApi(String status){
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<LocationBase> mLocationBaseCall=mApiInterface.updateDriverStatus(status,mGlobalPreference.getID());
        mLocationBaseCall.enqueue(new Callback<LocationBase>() {
            @Override
            public void onResponse(Call<LocationBase> call, Response<LocationBase> response) {


            }

            @Override
            public void onFailure(Call<LocationBase> call, Throwable t) {

            }
        });

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String lat = intent.getStringExtra("lat");
            String lng = intent.getStringExtra("lng");
            updateLocation(lat,lng);
        }
    };

//
    public void updateLocation(String lat, String lng) {
        mGlobalPreference = new GlobalPreference(getApplicationContext());
        Log.d(TAG, "updateLocation: " + mGlobalPreference.RetrieveIp());
        Log.d(TAG, "updateLocation: " + mGlobalPreference.getPassword());
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<LocationBase> mLocationBaseCall=mApiInterface.updateLocation(mGlobalPreference.getID(),lat,lng);
        mLocationBaseCall.enqueue(new Callback<LocationBase>() {
            @Override
            public void onResponse(Call<LocationBase> call, Response<LocationBase> response) {
                Log.d(TAG, "onResponse: "+response.isSuccessful());
            }

            @Override
            public void onFailure(Call<LocationBase> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }
//
    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            mGlobalPreference.setLoginStatus(false);

            startActivity(new Intent(HomeActivity.this,LoginActivity.class ));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
