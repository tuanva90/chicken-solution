package com.ttc.mShopping.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TemplateActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	//Create menu
			@Override
			public boolean onCreateOptionsMenu(Menu menu) {
				// TODO Auto-generated method stub
				MenuInflater inf=this.getMenuInflater();
				inf.inflate(R.menu.mainmenu ,menu);		
				return true;
			}
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
								
				return super.onOptionsItemSelected(item);
			}
}
