package com.ttc.mShopping.activities;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.ttc.mShopping.models.ListResult;
import com.ttc.mShopping.models.SearchResult;
import com.ttc.mShopping.services.SearchSevice;
import com.ttc.mShopping.utils.CommonConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultActivity extends TemplateActivity implements LocationListener,
		Runnable {

	/* Define ListView */
	private ListView listResult;
	/* Define TextView */
	private TextView txtViewCategory;
	/* Define LocationManager */
	private LocationManager locationManager;
	/* Define provider string */
	private String provider;
	/* Latitude - Longitude */
	public double latitude;
	public double longitude;

	// Current position
	// true: don't get GPS long & latitude
	// false: using GPS to get location
	private boolean isCustomLocation = false;

	/* Query */
	private String query;
	private int iquery;
	/* Define list of SearchResult */
	private List<SearchResult> searchResultList;
	/* Define ProgressDialog */
	private ProgressDialog progressDialog;

	/* Define List of SearchResult in order to show map */
	ListResult listOfSearchResult;

	/* Define button ShowMap */
	private Button btnShowMap;
	private Button btnDirection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.latitude = CommonConfiguration.LATITUDE;
		this.longitude = CommonConfiguration.LONGITUDE;
		this.isCustomLocation = CommonConfiguration.IS_CHANGE;

		// Get intent and receive data
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		query = bundle.getString(CommonConfiguration.QUERY);
		iquery = bundle.getInt(CommonConfiguration.IQUERY);

		// Create and show ProgressDialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Searching...");
		progressDialog.show();

		// Create thread and start it
		Thread thread = new Thread(this);
		thread.start();
	}

	/** Display category text */
	private void displayText(int iquery) {
	/*	txtViewCategory = (TextView) findViewById(R.id.txtViewCategory);

		switch (iquery) {
		case CommonConfiguration.IGAS_STATION:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.GAS_STATION_FIXED.toUpperCase());
			break;
		case CommonConfiguration.ITAXI:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.TAXI.toUpperCase());
			break;
		case CommonConfiguration.IATM:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.ATM.toUpperCase());
			break;
		case CommonConfiguration.IHOTEL:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.HOTEL.toUpperCase());
			break;
		case CommonConfiguration.IRESTAURANT:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.RESTAURANT.toUpperCase());
			break;
		case CommonConfiguration.IBANK:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.BANK.toUpperCase());
			break;
		case CommonConfiguration.IAIRPORT:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.AIRPORT.toUpperCase());
			break;
		case CommonConfiguration.IPLACE:
			txtViewCategory.setText("CATEGORY "
					+ CommonConfiguration.PLACE.toUpperCase());
			break;
		}
*/	}

	/** Display ListView with parameters */
	private void displayListView() {
		listResult = (ListView) findViewById(R.id.listResult);

		// Prepare for ListView
		int[] to = { R.id.txtViewName, R.id.txtViewAddress,
				R.id.txtViewDistance };
		String[] from = { CommonConfiguration.VIEW_NAME,
				CommonConfiguration.VIEW_ADDRESS,
				CommonConfiguration.VIEW_DISTANCE };
		// Create list of result
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		// Define a result
		HashMap<String, String> resultMap;

		// Define address
		String address;
		// Put data to list of result
		for (int i = 0; i < searchResultList.size(); i++) {
			address = "";
			resultMap = new HashMap<String, String>();
			resultMap.put(CommonConfiguration.VIEW_NAME, searchResultList
					.get(i).getTitleNoFormatting());
			for (int j = 0; j < searchResultList.get(i).getAddressLines()
					.size(); j++) {
				address += searchResultList.get(i).getAddressLines().get(j)
						.toString();
				if (j != searchResultList.get(i).getAddressLines().size() - 1) {
					address += ", ";
				}
			}
			resultMap.put(CommonConfiguration.VIEW_ADDRESS, address);
			resultMap.put(CommonConfiguration.VIEW_DISTANCE,
					String.valueOf(searchResultList.get(i).getDistance())
							+ " km");
			resultMap.put(CommonConfiguration.SEARCH_RESULT_ID,
					String.valueOf(searchResultList.get(i).getId()));
			resultList.add(resultMap);
		}

		// Create a SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, resultList,
				R.layout.row_search_result, from, to);
		// Display list
		Log.v("listview", String.valueOf(resultList.size()));
		listResult.setAdapter(simpleAdapter);

		// Handle item click event
		listResult.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, String> item = (HashMap<String, String>) listResult
						.getItemAtPosition(arg2);
				Bundle bundle = new Bundle();
				int searchResultId = Integer.parseInt(item
						.get(CommonConfiguration.SEARCH_RESULT_ID));
				SearchResult searchResult = searchResultList
						.get(searchResultId);

				bundle.putInt(CommonConfiguration.IQUERY, iquery);
				bundle.putDouble(CommonConfiguration.CURRENT_LATITUDE, latitude);
				bundle.putDouble(CommonConfiguration.CURRENT_LONGITUDE,
						longitude);
				bundle.putSerializable(CommonConfiguration.SEARCH_RESULT,
						searchResult);

				listOfSearchResult = new ListResult(searchResultList);
				bundle.putSerializable(CommonConfiguration.SEARCH_RESULT_LIST,
						listOfSearchResult);

				Intent intent = new Intent(SearchResultActivity.this,
						DetailInfoActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	/** Get list of SearchResults from JSON */
	private void getSearchResultList(double latitude, double longitude,
			String query) {
		// Create SearchService
		SearchSevice searchService = new SearchSevice(latitude, longitude,
				query);
		// Parse JSON
		searchService.runJSONParser();
		// Receive result
		this.searchResultList = searchService.getSearchResultList();
	}

	/** Get latitude and longitude */
	private void getLatitudeLongitude() {
		if (!isCustomLocation) { // if not change location
			// Get the location manager
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Provider is GPS
			provider = LocationManager.GPS_PROVIDER;
			//Location location = locationManager.getLastKnownLocation(provider);
			// Initialize the location fields
			//if (location != null) {
				//this.latitude = location.getLatitude();
			//	this.longitude = location.getLongitude();
			//} else {
				this.latitude = CommonConfiguration.LATITUDE;
				this.longitude = CommonConfiguration.LONGITUDE;
			//}
		}
	}

	/** Method of interface LocationListener */
	@Override
	public void onLocationChanged(Location location) {
		getLatitudeLongitude();
		getSearchResultList(latitude, longitude, query);
		Log.i(CommonConfiguration.TAG, "Change: " + String.valueOf(latitude)
				+ " - " + String.valueOf(longitude));
		displayListView();
	}

	/** Method of interface LocationListener */
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	/** Method of interface LocationListener */
	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Enabled new provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	/** Method of interface LocationListener */
	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider,
				Toast.LENGTH_SHORT).show();
	}

	/** Method of interface Runable */
	@Override
	public void run() {
		// get latitude, longitude and list of SearchResults
		getLatitudeLongitude();
		getSearchResultList(latitude, longitude, query);
		handler.sendEmptyMessage(0);
		Log.v("SUCCESS", "Get result success");
	}

	/** Handler for handling message from method run() */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			// dismiss dialog
			progressDialog.dismiss();

			// set layout
			setContentView(R.layout.search_result);

			// display text and ListView
			displayText(iquery);
			displayListView();

			// Get button ShowMap and define event handler
			btnShowMap = (Button) findViewById(R.id.btnShowMap);
			btnShowMap.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Bundle bundle = new Bundle();
						bundle.putDouble(CommonConfiguration.CURRENT_LATITUDE,
								latitude);
						bundle.putDouble(CommonConfiguration.CURRENT_LONGITUDE,
								longitude);

						// get list of SearchResult and put into bundle
						listOfSearchResult = new ListResult(searchResultList);
						bundle.putSerializable(
								CommonConfiguration.SEARCH_RESULT_LIST,
								listOfSearchResult);

						Intent intent = new Intent(SearchResultActivity.this,
								ViewMapActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					} catch (Exception e) {
						Log.i(CommonConfiguration.TAG, "ShowMap exception: "
								+ e.toString());
					}
				}
			});

			btnDirection = (Button) findViewById(R.id.btnDirection);
			btnDirection.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Bundle bundle = new Bundle();

						// get list of SearchResult and put into bundle
						listOfSearchResult = new ListResult(searchResultList);
						bundle.putSerializable(
								CommonConfiguration.SEARCH_RESULT_LIST,
								listOfSearchResult);

						Intent intent = new Intent(SearchResultActivity.this,
								GetDirectionActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					} catch (Exception e) {
						Log.i(CommonConfiguration.TAG,
								"Get Direction exception: " + e.toString());
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								SearchResultActivity.this);
						alertDialog.setTitle("Error");
						alertDialog.setMessage(e.toString());
						alertDialog.setPositiveButton("Ok", null);
						alertDialog.show();
					}
				}
			});
		}
	};
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
	
	//Create menu	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			if(item.getItemId()==R.id.mnOptionHome)
			{
				Intent intent = new Intent(SearchResultActivity.this, ListCategoriesActivity.class);
				startActivity(intent);			
			}
			if(item.getItemId()==R.id.mnOptionFavourite)
			{
				Intent intent = new Intent(SearchResultActivity.this, MyFavouriteActivity.class);
				startActivity(intent);			
			}
			if(item.getItemId()==R.id.mnOptionBack)
			{
				onBackPressed();
			}
			return super.onOptionsItemSelected(item);
		}
}
