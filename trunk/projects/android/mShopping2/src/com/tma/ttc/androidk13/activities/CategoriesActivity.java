package com.tma.ttc.androidk13.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.tma.ttc.androidk13.R;
import com.tma.ttc.androidk13.utilities.CommonConfiguration;
import com.tma.ttc.androidk13.utilities.MyLocation;
import com.tma.ttc.androidk13.utilities.MyLocation.LocationResult;
import com.tma.ttc.androidk13.utilities.PointAddressUtil;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class CategoriesActivity extends Activity {
	
	/** Called when the activity is first created. */
	
	private Button btnRestaurant, btnHotel, btnPlace, btnBank, btnAtm, btnAirport, btnTaxi, btnGas, btnAttraction;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	final int CHECK_SETTING = 1;
	final int WAIT_MSG = 2;
	final int LOC_NOTFOUND= 3;
	private Location currentLoc=null;
	private String providerName;
	private Handler mHandler;
	private SharedPreferences mSharePref;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_categories);
        //Get button 
        btnRestaurant = (Button)findViewById(R.id.btnrestaurant);                
        btnHotel = (Button)findViewById(R.id.btnhotel);       
        btnPlace = (Button)findViewById(R.id.btnplace);        
        btnAtm = (Button)findViewById(R.id.btnatm);        
        btnBank = (Button)findViewById(R.id.btnbanks);        
        btnGas = (Button)findViewById(R.id.btngas);               
        btnTaxi = (Button)findViewById(R.id.btntaxi);        
        btnAirport = (Button)findViewById(R.id.btnairport);                  
        btnAttraction = (Button)findViewById(R.id.btnattraction);
                
        //Click Listener
        btnAirport.setOnClickListener(mCatClickListener);
        btnTaxi.setOnClickListener(mCatClickListener);
        btnGas.setOnClickListener(mCatClickListener);
        btnBank.setOnClickListener(mCatClickListener);
        btnAtm.setOnClickListener(mCatClickListener);
        btnPlace.setOnClickListener(mCatClickListener);
        btnHotel.setOnClickListener(mCatClickListener);
        btnRestaurant.setOnClickListener(mCatClickListener);
		btnAttraction.setOnClickListener(mCatClickListener); 
		
		//Get location if from change location activity
		Bundle mBundle = getIntent().getExtras();
		if (mBundle!=null)
		{
			currentLoc = new Location("network");
			currentLoc.setLatitude(mBundle.getDouble(CommonConfiguration.KEY_LAT));
			currentLoc.setLongitude(mBundle.getDouble(CommonConfiguration.KEY_LNG));
		}
		if (currentLoc ==null)
			getCurrentLocation();
		
    }
    
    View.OnClickListener mCatClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (btnAirport.isPressed()){
				startSearch("airport");
			}
			
			if(btnAtm.isPressed()){
				startSearch("atm");
			} 
			
			if(btnAttraction.isPressed()){
				startSearch("attraction");
			}
			
			if(btnBank.isPressed()){
				startSearch("bank");
			}
			
			if(btnGas.isPressed()){
				startSearch("gas");
			}

			if(btnHotel.isPressed()){
				startSearch("hotel");
			}

			if(btnPlace.isPressed()){
				startSearch("place");
			}

			if(btnRestaurant.isPressed()){
				startSearch("restaurant");
			}

			if(btnTaxi.isPressed()){
				startSearch("taxi");
			}

		}
	};
	
	private void getCurrentLocation(){
		mSharePref = getSharedPreferences(CommonConfiguration.PREF_NAME,0);
		
		try {
			if (mSharePref!=null)
				showDialog(CommonConfiguration.RESTORE_LOC);
			else
			getLocation();
		}
		catch (Exception e) {
				Log.v("An error occurred: '{0}'", e.toString());
			
			}
	}
	
	private void startSearch(String place){
		if (currentLoc!=null){
		String currentAdd;			
		Bundle b = new Bundle();					
		b.putString("search", place);		
			b.putDouble("curlat", currentLoc.getLatitude());			
			b.putDouble("curlon", currentLoc.getLongitude());		
			GeoPoint point = new GeoPoint(
			          (int) (currentLoc.getLatitude() * 1E6), 
			          (int) (currentLoc.getLongitude() * 1E6));
			currentAdd = PointAddressUtil.ConvertPointToAddress(point, getBaseContext());
		if (currentAdd!=null){
			b.putString("currentadd", currentAdd);
		}
		Intent mIntent = new Intent(getApplicationContext(),
				SearchResultActivity.class);
		mIntent.putExtras(b);
		startActivity(mIntent);
		}
		else
			getCurrentLocation();
		}					
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mn_mainscreen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.mn_lstfavorite:
			Intent intent = new Intent(getApplicationContext(), ListFavoriteActivity.class);
			Bundle b = new Bundle();
			if (currentLoc!=null){
				b.putDouble("curlat", currentLoc.getLatitude());			
				b.putDouble("curlon", currentLoc.getLongitude());
			}
			intent.putExtras(b);
			startActivity(intent);
			return true;
		case R.id.mn_changle_location:
			Intent intent2 = new Intent(getApplicationContext(), ChangeLocationActivity.class);
			startActivity(intent2);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	private void getLocation() {
		LocationManager locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		Location lastestLoc;
		criteria.setAccuracy(Criteria.ACCURACY_FINE);		
		MyLocationListener listener = new MyLocationListener();
		providerName = locMgr.getBestProvider(criteria, true);
		Log.v("mylog", providerName);
		if (providerName == null) {
			showDialog(CHECK_SETTING);
			currentLoc = null;
			
		} 
		else
		{
			lastestLoc = locMgr.getLastKnownLocation(providerName);
			//Log.v("mylog", "time: " +  (lastestLoc.getTime()));
			if (lastestLoc !=null && (System.currentTimeMillis() - lastestLoc.getTime() < 36000000) ){
				currentLoc = lastestLoc;	
				}
				else {
				locMgr.requestLocationUpdates(providerName, 0, 0, listener);
				showDialog(WAIT_MSG);
				new Thread(new Runnable() {
					public void run() {
						Looper.prepare();
						long start = System.currentTimeMillis();
						while ((currentLoc == null)
								&& (System.currentTimeMillis() < (start + 10000))) {
						}
						dismissDialog(WAIT_MSG);
						if (currentLoc == null) {
							showDialog(LOC_NOTFOUND);
						}
						Looper.loop();
					}
				}).start();
				if (("network".equals(providerName)) && (currentLoc == null)) {
					locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
							0, listener);
				}
			}
		}
	}
	
	
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case CHECK_SETTING:
			return new AlertDialog.Builder(this).setTitle("Alert").setMessage(
					"Your location setting is disable, please enable it")
					.setPositiveButton("Setting",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent callGPSSettingIntent = new Intent(
											android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(callGPSSettingIntent);
								}
							}).setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									
								}
							}).create();
		case WAIT_MSG:
			return new ProgressDialog(this).show(this, "Working", "Waiting for location", true);
		case LOC_NOTFOUND:
			return new AlertDialog.Builder(this)
					.setMessage(
							"Your current location is temporatory unavailable, please try again")
					.setNegativeButton("Close", null).show();
		case CommonConfiguration.RESTORE_LOC: 
			return new AlertDialog.Builder(this)
            .setIcon(R.drawable.app_icon)
            .setTitle("Do you want use saved location")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	currentLoc = new Location("network");
                    currentLoc.setLatitude(mSharePref.getFloat(CommonConfiguration.KEY_LAT, 0));
                    currentLoc.setLongitude(mSharePref.getFloat(CommonConfiguration.KEY_LNG, 0));
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    try{
                    	getLocation();
                    }
                    catch (Exception e){
                    	e.printStackTrace();
                    }
                }
            })
            .create();
		}
		return null;
	}
	
	class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {

			if (location != null) {
				currentLoc = location;
								
			}

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}

	
}
