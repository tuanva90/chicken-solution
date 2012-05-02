package com.exercise.AndroidGoogleFeed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AndroidGoogleFeedActivity extends Activity {
		 
	WebView myWebView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        myWebView = new WebView(this);
        setContentView(myWebView);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.setWebViewClient(new MyWebSettings(this));
        myWebView.loadUrl("file:///android_asset/mypage.html");
                
    }
    public void load(String url)
    {
    	myWebView.loadUrl(url);
    }
}
class MyWebSettings extends WebViewClient
{
	AndroidGoogleFeedActivity sa;
	public MyWebSettings(AndroidGoogleFeedActivity sa)
	{
		this.sa=sa;
		
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView wv,String url)
	{
			sa.load(url);
			return false;
	}
}