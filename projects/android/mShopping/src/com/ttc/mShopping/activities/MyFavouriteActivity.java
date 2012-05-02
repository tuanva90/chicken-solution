package com.ttc.mShopping.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MyFavouriteActivity extends TemplateActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	//Create menu			
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// TODO Auto-generated method stub
				if(item.getItemId()==R.id.mnOptionHome)
				{
					Intent intent = new Intent(MyFavouriteActivity.this, ListCategoriesActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionFavourite)
				{
					Intent intent = new Intent(MyFavouriteActivity.this, MyFavouriteActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionBack)
				{
					onBackPressed();
				}
				return super.onOptionsItemSelected(item);
			}
}
