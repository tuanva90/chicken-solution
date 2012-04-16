package com.ttc.mShopping.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MShoppingActivity extends Activity implements Runnable {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Create a thread and start it
		Thread thread = new Thread(this);
		thread.start();
	}

	/** Method of interface Runable */
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
			handler.sendEmptyMessage(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** Handler for handling message */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Intent intent = new Intent(MShoppingActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			this.finish();
		}
	}

}