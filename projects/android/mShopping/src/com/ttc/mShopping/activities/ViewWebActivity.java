package com.ttc.mShopping.activities;

import com.ttc.mShopping.utils.CommonConfiguration;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewWebActivity extends TemplateActivity {

	WebView myWebView;
	private static final String html=
			"" +
			"<html><head>" +
			"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"><script language=\"javascript\" type=\"text/javascript\">function show(){ if(this.id==\"contentShow\"){this.id=\"contentHide\";} else {this.id=\"contentShow\";} }function onload(){ var list=document.body.getElementsByTagName(\"div\"); for(i=0;i<list.length;i++)  {   if(list[i].id==\"contentShow\")  { list[i].onclick=show;}  }  }</script><link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\"></head><body onload=\"onload();\"> " ;
				
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        
        myWebView = (WebView)findViewById(R.id.webview);
       // setContentView(myWebView);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String url = bd.getString(CommonConfiguration.URL_TO_WEB);
        myWebView.setWebViewClient(new MyWebSettings(this));
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        myWebView.loadUrl(url);
        //myWebView.loadDataWithBaseURL(url ,html, "text/html", "utf-8", null);
	}
	
	//Create menu		
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// TODO Auto-generated method stub
				if(item.getItemId()==R.id.mnOptionHome)
				{
					Intent intent = new Intent(ViewWebActivity.this, ListCategoriesActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionFavourite)
				{
					Intent intent = new Intent(ViewWebActivity.this, MyFavouriteActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionBack)
				{
					onBackPressed();
				}
				return super.onOptionsItemSelected(item);
			}
			
			public void LookUp(String url)
			{
				myWebView.loadUrl(url);				  
				
			}
			class MyWebSettings extends WebViewClient
			{
				ViewWebActivity sa;
				public MyWebSettings(ViewWebActivity sa)
				{
					this.sa=sa;
					
				}
				@Override
				public boolean shouldOverrideUrlLoading(WebView wv,String url)
				{
						sa.LookUp(url);
						return false;
				}
			}
}
