package com.tma.ttc.androidK13.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;

public class QuitActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		Process.killProcess(Process.myPid());
	}
}