package com.tma.ttc.androidK13.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tma.ttc.androidK13.utilities.CommonConfiguration;

public class ListCategoriesActivity extends Activity {

	/* Define buttons */
	private Button gas_station;
	private Button taxi;
	private Button atm;
	private Button hotel;
	private Button restaurant;
	private Button bank;
	private Button airport;
	private Button place;

	/* Define TextView */
	private TextView txtCurrentLocation;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);

		/* Gas station */
		gas_station = (Button) findViewById(R.id.btnGasStations);
		gas_station.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.GAS_STATION);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IGAS_STATION);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

		/* Taxi */
		taxi = (Button) findViewById(R.id.btnTaxis);
		taxi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.TAXI);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.ITAXI);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Atm */
		atm = (Button) findViewById(R.id.btnAtms);
		atm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.ATM);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IATM);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Hotel */
		hotel = (Button) findViewById(R.id.btnHotels);
		hotel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.HOTEL);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IHOTEL);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Restaurant */
		restaurant = (Button) findViewById(R.id.btnRestaurants);
		restaurant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.RESTAURANT);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IRESTAURANT);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Bank */
		bank = (Button) findViewById(R.id.btnBanks);
		bank.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.BANK);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IBANK);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Airport */
		airport = (Button) findViewById(R.id.btnAirport);
		airport.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.AIRPORT);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IAIRPORT);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		/* Place */
		place = (Button) findViewById(R.id.btnPlaces);
		place.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ListCategoriesActivity.this,
						SearchResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,
						CommonConfiguration.PLACE);
				bundle.putInt(CommonConfiguration.IQUERY,
						CommonConfiguration.IPLACE);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// Display location
		displayLocationName();
	}

	/** Display location */
	private void displayLocationName() {
		txtCurrentLocation = (TextView) findViewById(R.id.txtViewCurrentLocation);
		// Check if user change location or not
		this.isCustomLocation = CommonConfiguration.IS_CHANGE;
		// Get latitude and longitude
		if (this.isCustomLocation) {
			this.latitude = CommonConfiguration.LATITUDE;
			this.longitude = CommonConfiguration.LONGITUDE;
		} else {
			getLatitudeLongitude();
		}
		// Define geocoder and list of addresses
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> addressList = null;
		// Get list of addresses by latitude and longitude
		try {
			addressList = geocoder.getFromLocation(this.latitude,
					this.longitude, 1);
		} catch (IOException e) {
			Log.i(CommonConfiguration.TAG, e.toString());
		}
		// Display text on view
		if (addressList != null && addressList.size() == 1) {
			String name = "";
			for (int i = 1; i <= addressList.get(0).getMaxAddressLineIndex(); i++) {
				name += addressList.get(0).getAddressLine(i);
				if (i != addressList.get(0).getMaxAddressLineIndex()) {
					name += ", ";
				}
			}
			txtCurrentLocation.setText(name);
		} else {
			txtCurrentLocation.setText("Undefinited area");
		}
	}

	/** Get latitude and longitude */
	private void getLatitudeLongitude() {
		if (!isCustomLocation) { // if not change location
			// Get the location manager
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Provider is GPS
			provider = LocationManager.GPS_PROVIDER;
			Location location = locationManager.getLastKnownLocation(provider);
			// Initialize the location fields
			if (location != null) {
				this.latitude = location.getLatitude();
				this.longitude = location.getLongitude();
			} else {
				this.latitude = CommonConfiguration.LATITUDE;
				this.longitude = CommonConfiguration.LONGITUDE;
			}
		}
	}

	/* Invoked when the menu button is pressed */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option1, menu);
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
			Intent intent = new Intent(ListCategoriesActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(ListCategoriesActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// Process.killProcess(Process.myPid());
			Intent intent = new Intent(ListCategoriesActivity.this,
					MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);

		}
		return super.onOptionsItemSelected(item);
	}

}
