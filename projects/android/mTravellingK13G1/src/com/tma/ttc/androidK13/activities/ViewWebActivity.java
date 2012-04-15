package com.tma.ttc.androidK13.activities;

import android.app.Activity;
//import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.webkit.WebView;
//import android.webkit.WebViewClient;

import com.tma.ttc.androidK13.utilities.CommonConfiguration;

public class ViewWebActivity extends Activity {

	/* Define ProgressDialog */
	// private ProgressDialog progressDialog;

	private WebView webView;

	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_web);

		// Create and show ProgressDialog
		// progressDialog = new ProgressDialog(this);
		// progressDialog.setMessage("Waiting...");
		// progressDialog.show();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		url = bundle.getString(CommonConfiguration.URL_TO_WEB);
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		// webView.setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
		//
		// public void onPageFinished(WebView view, String url) {
		// if (progressDialog.isShowing()) {
		// progressDialog.dismiss();
		// }
		// }
		// });
		webView.loadUrl(url);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/* Invoked when the menu button is pressed */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option, menu);
		setMenuBackground();
		return true;
	}

	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {

			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try { // Ask our inflater to create the view
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundResource(R.drawable.menu_bg_480x157);
							}
						});
						return view;
					} catch (Exception e) {
					}
				}
				return null;
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.change_location_menu) {
			Intent intent = new Intent(ViewWebActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(ViewWebActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(ViewWebActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// Process.killProcess(Process.myPid());
			Intent intent = new Intent(ViewWebActivity.this, MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

}