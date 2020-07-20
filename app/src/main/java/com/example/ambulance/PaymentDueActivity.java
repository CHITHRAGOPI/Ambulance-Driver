package com.example.ambulance;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ambulance.utils.GlobalPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentDueActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView mWebView;
    GlobalPreference mGlobalPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_due);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(){
        mGlobalPreference=new GlobalPreference(this);
        String url="http://"+mGlobalPreference.RetrieveIp()+"/accident_alert/index.php";

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
    }
}
