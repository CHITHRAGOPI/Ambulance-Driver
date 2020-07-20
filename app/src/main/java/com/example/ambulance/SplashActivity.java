package com.example.ambulance;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ambulance.utils.GlobalPreference;

public class SplashActivity extends AppCompatActivity {

    GlobalPreference mGlobalPreference;
    private  static  final int SPLASH_TIME_OUT=1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mGlobalPreference=new GlobalPreference(this);
        mGlobalPreference.addIp("myclouddata.space");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mGlobalPreference.getLoginStatus()){
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
                else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }

                SplashActivity.this.finish();
            }
        },SPLASH_TIME_OUT);


    }
}
