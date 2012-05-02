package com.ttc.mShopping.activities;

import com.ttc.mShopping.utils.CommonConfiguration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewWebActivity extends Activity {

	WebView myWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        myWebView = new WebView(this);
        setContentView(myWebView);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String url = bd.getString(CommonConfiguration.URL_TO_WEB);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
	}
}
