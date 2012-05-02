package com.ttc.mShopping.activities;

import java.util.ArrayList;
import java.util.List;

import com.ttc.mShopping.models.ListResult;
import com.ttc.mShopping.models.SearchResult;
import com.ttc.mShopping.utils.CommonConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GetDirectionActivity extends TemplateActivity {
	private ListResult listLocation; // ListREsult
	private Spinner ddlToLocation;//spinner to location ( using to direct from location to another location)
	private Spinner ddlFromLocation;////spinner from location ( using to direct from location to another location)
	private Button btnGetRoute; //button get route from location to another location.
	SearchResult respectLocation = null; //
	SearchResult fromLocation = null; // searchresult fromlocation

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_direction);

		btnGetRoute = (Button) findViewById(R.id.btnGetRoute);//button direction
		btnGetRoute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // set data to intent and open ViewmapActivity
				if (respectLocation != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable(CommonConfiguration.RESPECT_LOCATION,
							respectLocation);
					bundle.putSerializable(CommonConfiguration.FROM_LOCATION,
							fromLocation); 
					bundle.putSerializable(CommonConfiguration.SEARCH_RESULT_LIST, listLocation);

					Intent intent = new Intent(GetDirectionActivity.this,
							ViewMapActivity.class);
					intent.putExtras(bundle);
					startActivity(intent); 
				} else {
					Toast.makeText(getApplicationContext(),
							"Please choose a location", Toast.LENGTH_SHORT).show();
				}
			}
		});
		ddlToLocation = (Spinner) findViewById(R.id.ddlPlaces);
		ddlFromLocation = (Spinner) findViewById(R.id.ddlFrom);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras(); // get data from intent
		listLocation = (ListResult) bundle
				.getSerializable(CommonConfiguration.SEARCH_RESULT_LIST);
     // add data to ddlTolocation and ddlFromlocation. data is list searchresult.
		int size = listLocation.getListSearchResult().size(); // get size of list searchresult
		List<String> itemListTo = new ArrayList<String>();//
		List<String> itemListFrom = new ArrayList<String>();
		itemListFrom.add("Current Position");
		for (int i = 0; i < size; i++) {
			itemListTo.add(listLocation.getListSearchResult().get(i)
					.getTitleNoFormatting());
			itemListFrom.add(listLocation.getListSearchResult().get(i)
					.getTitleNoFormatting());
		}
		String[] itemsTo = itemListTo.toArray(new String[size]);
		String[] fromItems = itemListFrom.toArray(new String[size+1]);

		ArrayAdapter<String> adapterTo = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, itemsTo);
		ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, fromItems);
		adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ddlToLocation.setAdapter(adapterTo);
		ddlFromLocation.setAdapter(adapterFrom);
		ddlToLocation.setOnItemSelectedListener(mListenerTo);
		ddlFromLocation.setOnItemSelectedListener(mListenerFrom);
	}

	private OnItemSelectedListener mListenerTo = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) { // ontitemselected spinner event
			int index = ddlToLocation.getSelectedItemPosition();
			int size = listLocation.getListSearchResult().size();
			for (int i = 0; i < size; i++) {
				if (index == listLocation.getListSearchResult().get(i).getId()) {
					respectLocation = listLocation.getListSearchResult().get(i);
					break;
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	private OnItemSelectedListener mListenerFrom = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long arg3) { // ontitemselected spinner event
			int index = ddlFromLocation.getSelectedItemPosition();

			int size = listLocation.getListSearchResult().size();
			for (int i = 0; i < size; i++) {
				if (index == 0) {
					fromLocation = new SearchResult();
					fromLocation.setLat(CommonConfiguration.LATITUDE);
					fromLocation.setLng(CommonConfiguration.LONGITUDE);
					break;
				}else if (index == listLocation.getListSearchResult().get(i).getId()+1){
					fromLocation = listLocation.getListSearchResult().get(i);
					break;
				}
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};
	
	//Create menu			
			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// TODO Auto-generated method stub
				if(item.getItemId()==R.id.mnOptionHome)
				{
					Intent intent = new Intent(GetDirectionActivity.this, ListCategoriesActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionFavourite)
				{
					Intent intent = new Intent(GetDirectionActivity.this, MyFavouriteActivity.class);
					startActivity(intent);			
				}
				if(item.getItemId()==R.id.mnOptionBack)
				{
					onBackPressed();
				}
				return super.onOptionsItemSelected(item);
			}
}