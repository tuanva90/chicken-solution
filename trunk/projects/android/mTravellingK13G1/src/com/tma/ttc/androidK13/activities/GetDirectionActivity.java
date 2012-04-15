package com.tma.ttc.androidK13.activities;

import java.util.ArrayList;
import java.util.List;

import com.tma.ttc.androidK13.models.ListResult;
import com.tma.ttc.androidK13.models.SearchResult;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;

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

public class GetDirectionActivity extends Activity {
	private ListResult listLocation;
	private Spinner ddlToLocation;
	private Spinner ddlFromLocation;
	private Button btnGetRoute;
	SearchResult respectLocation = null;
	SearchResult fromLocation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_direction);

		btnGetRoute = (Button) findViewById(R.id.btnGetRoute);
		btnGetRoute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
		Bundle bundle = intent.getExtras();
		listLocation = (ListResult) bundle
				.getSerializable(CommonConfiguration.SEARCH_RESULT_LIST);

		int size = listLocation.getListSearchResult().size();
		List<String> itemListTo = new ArrayList<String>();
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
				long arg3) {
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
				long arg3) {
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option, menu);
		setMenuBackground();
		return true;
	}

	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {

			@Override
			public View onCreateView(String name, Context context,AttributeSet attrs) {
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
			Intent intent = new Intent(GetDirectionActivity.this,ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(GetDirectionActivity.this,	MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(GetDirectionActivity.this,	ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			Process.killProcess(Process.myPid());
			Intent intent = new Intent(GetDirectionActivity.this,	MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}