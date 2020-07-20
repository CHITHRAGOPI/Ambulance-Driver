package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ambulance.model.location.LocationBase;
import com.example.ambulance.model.payment.PaymentBase;
import com.example.ambulance.model.payment.PaymentItem;
import com.example.ambulance.model.updatepayment.PaymentUpdateListBase;
import com.example.ambulance.model.updatepayment.PaymentUpdateListItem;
import com.example.ambulance.utils.GlobalPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentStatusActivity extends AppCompatActivity {


    private String userId;


    @BindView(R.id.statusChangeButton)
    Button  mStatusPaymentButton;

    private static final String TAG = "PaymentStatusActivity";

    @BindView(R.id.paymentStatus)
    TextView mStatusPaymentTextView;

    private ApiInterface mApiInterface;

    GlobalPreference mGlobalPreference;

    PaymentUpdateListItem paymentUpdateListItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        ButterKnife.bind(this);
        userId=getIntent().getStringExtra("user_id");
        mGlobalPreference=new GlobalPreference(this);
        mApiInterface=ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    callPaymentStatatus();
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

       mStatusPaymentButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d(TAG, "onClick: "+paymentUpdateListItem.getStatus());
               if(paymentUpdateListItem.getStatus().equals("0")){
                   callPaymentUpdateApi("1");
                   finish();
                   Toast.makeText(PaymentStatusActivity.this, "Payment status updated", Toast.LENGTH_SHORT).show();

               }else{
                   callPaymentUpdateApi("0");
                   finish();
                   Toast.makeText(PaymentStatusActivity.this, "Payment status updated", Toast.LENGTH_SHORT).show();

               }
//               if(mStatusPaymentButton.getText().equals("Not Paid")){
//                   callPaymentUpdateApi("1");
//
//               }else{
//                   callPaymentUpdateApi("0");
//
//               }
           }
       });
    }



    public void  callPaymentUpdateApi(String paymentstatus){
        mApiInterface=ApiClient.getRetrofit(mGlobalPreference.RetrieveIp()).create(ApiInterface.class);
        Log.d(TAG, "callPaymentUpdateApi: "+paymentstatus);
        Log.d(TAG, "callPaymentUpdateApi: "+paymentUpdateListItem.getId());
        Call<LocationBase> mPaymentUpdateListBaseCall=mApiInterface.updatePayment(paymentstatus,paymentUpdateListItem.getId());
        mPaymentUpdateListBaseCall.enqueue(new Callback<LocationBase>() {
            @Override
            public void onResponse(Call<LocationBase> call, Response<LocationBase> response) {
                Log.d(TAG, "onResponse: update response"+response);


            }

            @Override
            public void onFailure(Call<LocationBase> call, Throwable t) {

            }
        });
    }


  public void callPaymentStatatus(){
        Call<PaymentUpdateListBase> mPaymentUpdateListBaseCall=mApiInterface.getPayment(userId);
        mPaymentUpdateListBaseCall.enqueue(new Callback<PaymentUpdateListBase>() {
            @Override
            public void onResponse(Call<PaymentUpdateListBase> call, Response<PaymentUpdateListBase> response) {
                Log.d(TAG, "onResponse:payment "+response.body().isSuccess());
                paymentUpdateListItem = response.body().getData().get(0);
                if (paymentUpdateListItem.getStatus().equals("1")) {
                    mStatusPaymentTextView.setText("Paid");
                    mStatusPaymentButton.setText("Not Paid");
                } else {
                    mStatusPaymentTextView.setText("Not Paid");
                    mStatusPaymentButton.setText("Paid");
                }

            }
            @Override
            public void onFailure(Call<PaymentUpdateListBase> call, Throwable t) {

            }
        });

    }
}
