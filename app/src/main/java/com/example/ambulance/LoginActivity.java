package com.example.ambulance;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ambulance.model.login.LoginBase;
import com.example.ambulance.model.login.LoginItem;
import com.example.ambulance.permisions.BaseActivity;
import com.example.ambulance.utils.GlobalPreference;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edt_login_username)
    EditText mUsernameLoginEdit;
    @BindView(R.id.edt_login_password)
    EditText mPasswordLoginEdit;

    @BindView(R.id.login_progressBar)
    ProgressBar mLoginProgress;

    private ApiInterface mApiInterface;
    private GlobalPreference mGlobalPreference;
    private Gson gson;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate: ");
        init();
    }

    private void init() {
        mGlobalPreference = new GlobalPreference(this);
        gson = new Gson();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @OnClick(R.id.btn_login_submit)
    public void login() {
        Log.d(TAG, "login: ");
        String username = mUsernameLoginEdit.getText().toString();
        String password = mPasswordLoginEdit.getText().toString();
        if (!TextUtils.isEmpty(username) || !TextUtils.isEmpty(password)) {
            mLoginProgress.setVisibility(View.VISIBLE);

            Log.d(TAG, "login:vcbvcbc "+mGlobalPreference.RetrieveIp());

            mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
            Log.d(TAG, "login: " + username);
            Log.d(TAG, "login: " + password);
            Call<LoginBase> loginBaseCall = mApiInterface.userLogin(username, password);
            loginBaseCall.enqueue(new Callback<LoginBase>() {
                @Override
                public void onResponse(Call<LoginBase> call, Response<LoginBase> response) {
                    Log.d(TAG, "" + response.body().isSuccess());
                    mLoginProgress.setVisibility(View.GONE);

                    if(response.body().isSuccess()){
                        LoginItem loginItem=response.body().getData().get(0);
                     mGlobalPreference.
                             saveCredentials(loginItem.getName(),loginItem.getUsername(),loginItem.getPassword(),
                                       loginItem.getAddress(),loginItem.getDeviceId(),loginItem.getPhone(),loginItem.getId(),
                                        loginItem.getVehicleNo());
                         mGlobalPreference.setLoginStatus(true);
                         startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginBase> call, Throwable t) {
                    mLoginProgress.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: " + t);
                }
            });

        } else {

            Toast.makeText(this, R.string.login_empty_fields, Toast.LENGTH_SHORT).show();
        }

    }
}
