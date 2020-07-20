package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ambulance.model.kmsrate.KmsRateBase;
import com.example.ambulance.model.payment.PaymentBase;
import com.example.ambulance.model.userlogin.UserLoginBase;
import com.example.ambulance.model.userlogin.UserLoginItem;
import com.example.ambulance.utils.GlobalPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  PaymentActivity extends AppCompatActivity {

    @BindView(R.id.toolbarTrip)
    Toolbar mToolbar;


    @BindView(R.id.kmPaymentEditText)
    EditText mKmPaymentEditText;


    @BindView(R.id.trip_paymentsubmit_button)
    Button mSubmitPaymentButton;

    @BindView(R.id.amountPaymentTextView)
    TextView mAmountPaymentTextView;


    @BindView(R.id.perKmPaymentTextView)
    TextView mKmsRatePaymentTextView;


    private String requestId;
    private String userId;
    int amount=0;
    int fixedrate=0;
    GlobalPreference mGlobalPreference;
    private static final String TAG = "PaymentActivity";
private ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestId=getIntent().getStringExtra("request_id");
        userId=getIntent().getStringExtra("user_id");
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        mGlobalPreference = new GlobalPreference(getApplicationContext());

        mGlobalPreference=new GlobalPreference(this);
        mToolbar.setTitle("Payment Details");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        mKmPaymentEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {



                if(mKmPaymentEditText.getText().equals("")){
                    mAmountPaymentTextView.setText("");
                    amount=0;
                }else {
                    if(!s.equals("")) {
                        try {
                            int sum = Integer.parseInt(mKmPaymentEditText.getText().toString()) * fixedrate;
                            mAmountPaymentTextView.setText("Amount: " + sum);
                            amount=sum;
                        }catch (Exception e){
                            mAmountPaymentTextView.setText("");
                            amount=0;
                        }


                    }
                }
            }
        });

        mSubmitPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mKmPaymentEditText.getText().toString().equals("")){
                    Toast.makeText(PaymentActivity.this, "Please enter Kms", Toast.LENGTH_SHORT).show();
                }else {
                    addpaymentDetails();
                }
            }
        });
        getKmrate();

    }


    public  void callUserDetails(){
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<UserLoginBase> mLoginBaseCall=mApiInterface.getUser(userId);
        mLoginBaseCall.enqueue(new Callback<UserLoginBase>() {
            @Override
            public void onResponse(Call<UserLoginBase> call, Response<UserLoginBase> response) {
                Log.d(TAG, "onResponse: "+response);
                if(response.body().isSuccess()){
                    UserLoginItem mUserLoginItem=response.body().getData().get(0);
                    Log.d(TAG, "onResponse:number "+mUserLoginItem.getEmergencyNo());
                    mGlobalPreference.setEmergencyPhoneNo(mUserLoginItem.getEmergencyNo());
                    mGlobalPreference.setUserPhoneNumber(mUserLoginItem.getPhone());
                }
            }
            @Override
            public void onFailure(Call<UserLoginBase> call, Throwable t) {

            }
        });

    }
    private void sendSmsPayment(){

        String sendPhone=mGlobalPreference.getEmergencyPhoneNumber();
        Log.d(TAG, "sendSmsPayment: no "+sendPhone);
        String userPhone=mGlobalPreference.getUserPhoneNumber();

        String message="Your friends @"+userPhone+"ambulance charge  "+mAmountPaymentTextView.getText().toString();

        String url="http://akrut.in/SMS/gateway.php?mobile="+sendPhone+"&msg="+message;

        Log.d(TAG, "callSmsHospitalDetails: "+url);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
                MyTripDetailActivity.isTripCompleted=true;
                mGlobalPreference.setAvailableStatus(true);

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


    public void  getKmrate() {
        Log.d(TAG, "getKmrate: ");
        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Call<KmsRateBase> mKmsRateBaseCall = mApiInterface.getKmRate();
        mKmsRateBaseCall.enqueue(new Callback<KmsRateBase>() {
            @Override
            public void onResponse(Call<KmsRateBase> call, Response<KmsRateBase> response) {
                System.out.println("getKmsrate" + response);
                if (response.body().isSuccess()) {
                    fixedrate = Integer.parseInt(response.body().getData().get(0).getAmount());
                    mKmsRatePaymentTextView.setText(String.valueOf(fixedrate));
                    callUserDetails();
                }

            }

            @Override
            public void onFailure(Call<KmsRateBase> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }



    public void addpaymentDetails(){


        mApiInterface = ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);

        Call<PaymentBase> mPaymentBase=mApiInterface.insertPayment(
                   requestId,
                    String.valueOf(amount),
                mGlobalPreference.getVehicleNo(),
                userId);


        mPaymentBase.enqueue(new Callback<PaymentBase>() {
            @Override
            public void onResponse(Call<PaymentBase> call, Response<PaymentBase> response) {
                Log.d(TAG, "onResponse: payment"+response);
                if(response.isSuccessful()){
                    Toast.makeText(PaymentActivity.this, "payment details added", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse:payment "+response.isSuccessful());
                     sendSmsPayment();

                }
            }

            @Override
            public void onFailure(Call<PaymentBase> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });



    }
}
