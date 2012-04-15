package com.tma.ttc.androidk13.activities;

import com.tma.ttc.androidk13.MainActivity;
import com.tma.ttc.androidk13.R;
import com.tma.ttc.androidk13.R.id;
import com.tma.ttc.androidk13.dataservices.DataService;
import com.tma.ttc.androidk13.dataservices.SearchService;
import com.tma.ttc.androidk13.models.FavoriteModel;
import com.tma.ttc.androidk13.models.PlaceModel;
import com.tma.ttc.androidk13.utilities.CommonConfiguration;
import com.tma.ttc.androidk13.utilities.LoadImage;
import com.tma.ttc.androidk13.utilities.MyLocation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailPlaceActivity extends Activity {
	
	//Button Bar
	private ImageButton btnCall, btnWeb, btnEmail, btnMap, btnFavorite; 
	//Textview
	private TextView txtPlaceName, txtAddress, txtAddressFull, txtPhoneNumber, txtDistance;
	//Image View
	private ImageView imgMap;
	//other
	private Bitmap mBitmap;
	private PlaceModel place;
	private double curLon, curLat, lng,lat;
	private int placeId;
	private Boolean fromFv;
	private DataService dataService;
	private Bundle b;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_place);
	//Get instance from view
		btnWeb = (ImageButton)findViewById(id.btnWeb);
		btnCall = (ImageButton) findViewById(R.id.btnCall);
		btnEmail = (ImageButton) findViewById(R.id.btnEmail);
		btnMap = (ImageButton) findViewById(R.id.btnMap);
		btnFavorite = (ImageButton) findViewById(R.id.btnFavorite);
		imgMap = (ImageView) findViewById(R.id.st_map);
		txtPlaceName = (TextView)findViewById(R.id.place_name);
		txtAddressFull = (TextView)findViewById(R.id.address_full);		
		txtPhoneNumber = (TextView)findViewById(R.id.phone_number);
		txtDistance = (TextView)findViewById(R.id.distance);
		txtAddress = (TextView)findViewById(R.id.address);
	//Set Listener for button		
		btnWeb.setOnClickListener(mClickListener);
		btnCall.setOnClickListener(mClickListener);
		btnEmail.setOnClickListener(mClickListener);
		btnMap.setOnClickListener(mClickListener);
		btnFavorite.setOnClickListener(mClickListener);
		
		b = getIntent().getExtras();
		fromFv = b.getBoolean("fromFv");
		if (fromFv == false){
		placeId = b.getInt("posittion");
		curLat = b.getDouble("curlat");
		curLon = b.getDouble("curlon");
		place = SearchService.placeModel.get(placeId);
		lng = place.getLng();
		lat = place.getLat();
	//Load Place Map
		mBitmap = LoadImage.downloadImage(place.getStaticMapUrl());
		if (mBitmap!=null)
		imgMap.setImageBitmap(mBitmap);
	//Load data to view
		txtPlaceName.setText(place.getTitle());
		txtAddress.setText(place.getAddressLines().get(0).toString());
		String address = "";
    	for (String s : place.getAddressLines()) {
    		address += s + " ";
    	}
		txtAddressFull.setText("Address Full: " + address);
		if(place.getPhoneNumbers()!=null){
			txtPhoneNumber.setText("Phone: "+ place.getPhoneNumbers().get(0).getNumber());
		}
		
		txtDistance.setText("Distance: " + place.getDistance() + " km");
		}
		else {
			curLat = b.getDouble("curlat");
			curLon = b.getDouble("curlon");
			lng = Double.parseDouble(b.getString("lng"));
			lat = Double.parseDouble(b.getString("lat"));
		//Load Place Map
			mBitmap = LoadImage.downloadImage(b.getString("map"));
			if (mBitmap!=null)
			imgMap.setImageBitmap(mBitmap);
		//Load data to view
			txtPlaceName.setText(b.getString("title"));
			txtAddress.setText(b.getString("address"));
			txtAddressFull.setText(b.getString("addressFull"));
			if (b.getString("phone")!=null){
			txtPhoneNumber.setText("Phone: "+ b.getString("phone"));
			}
			FavoriteModel fv = new FavoriteModel(b.getString("lat"), b.getString("lng"));
			txtDistance.setText("Distance: " + fv.getDistance(curLat, curLon) + " km");
			//txtDistance.setText("Distance: " + place.getDistance() + " km");
		}
	}
	
	
	
	//Button Click Listener
		private View.OnClickListener mClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Button Web
				if (btnWeb.isPressed()){
					Bundle mBundle = new Bundle();
					if(fromFv==false){
						mBundle.putString("url", place.getUrl());
					}
					else 
					{
						mBundle.putString("url", b.getString("webUrl"));
					}
				Intent mWebIntent = new Intent(getApplicationContext(), WebInfoActivity.class);
				mWebIntent.putExtras(mBundle);
				startActivity(mWebIntent);
				}
				//Button Call
				if(btnCall.isPressed()){
					try {
					   	Intent intent = new Intent(Intent.ACTION_CALL);
					   	String phoneNumber=null;
					   	if (fromFv==false){
		            	phoneNumber = "tel: " + place.getPhoneNumbers().get(0).getNumber();
					   	}
					   	else {
					   		if (b.getString("phone")!=null){
					   		phoneNumber = "tel: " + b.getString("phone");
					   		}
					   	}
		            	intent.setData(Uri.parse(phoneNumber));
		            	startActivity(intent);
						}
						catch (Exception e) {
		            	Toast.makeText(getApplicationContext(), 
		            			"Maybe this place do not have Phone Number", Toast.LENGTH_SHORT).show();
						}
					}
				//Button Email
				if (btnEmail.isPressed()){
					final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);                
	                emailIntent.setType("plain/text");           
	                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "friends@domainl.com");         
	                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Interseting Place"); 
	                if(fromFv==false){
	                	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi, I got interesting place for you "+ place.getUrl());
					}
					else 
					{
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hi, I got interesting place for you "+ b.getString("webUrl"));
					}
	                 
	                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
				}
				//Button Map
				if(btnMap.isPressed()){
					Bundle mBundle = new Bundle();
					mBundle.putBoolean("showall", false);
					mBundle.putDouble("curlon", curLon);
					mBundle.putDouble("curlat", curLat);
					mBundle.putDouble(CommonConfiguration.KEY_LAT, lat);
					mBundle.putDouble(CommonConfiguration.KEY_LNG, lng);
					Intent mMapIntent = new Intent(getApplicationContext(), ViewOnMapActivity.class);
					mMapIntent.putExtras(mBundle);
					startActivity(mMapIntent);
				}
				//Button Favorite
				if(btnFavorite.isPressed()){
					String pPhone=null;
					if (fromFv==true)
					{Toast.makeText(getApplicationContext(), "It's already in favorite", Toast.LENGTH_LONG).show();
					}
					else {
					String pName = place.getTitle();
					String pMapUrl = place.getStaticMapUrl();
					if (place.getPhoneNumbers()!=null)
					{
						pPhone = place.getPhoneNumbers().get(0).getNumber();
					}
					String pAddress = place.getAddressLines().get(0).toString();
					String lng = ""+place.getLng();
					String lat = ""+place.getLat();
					String address = "";
					String webUrl = place.getUrl();
					for (String s : place.getAddressLines()) {
						address += s + " ";
					}
					dataService = new DataService(getApplicationContext());
					dataService.open();
					
					if(dataService.isExisted(lat, lng, address)){
						Toast.makeText(getApplicationContext(), "This place existed", Toast.LENGTH_LONG).show();
					}else{
						dataService.insertFavorite(pName, pAddress,	address, pPhone, lng, lat, pMapUrl, webUrl);
						Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_LONG).show();
					}
				}
				}
			}
		};
		
		
		//Menu creation
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	// TODO Auto-generated method stub    	
	    	MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.mn_detail, menu);
			return true;
	    }
	    
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId())
			{
			case R.id.mn_home:
				Intent mIntent = new Intent(DetailPlaceActivity.this,CategoriesActivity.class);				
				Bundle mBundle = new Bundle();
                mBundle.putDouble(CommonConfiguration.KEY_LAT,curLat);
                mBundle.putDouble(CommonConfiguration.KEY_LNG, curLon);
                mIntent.putExtras(mBundle);
                DetailPlaceActivity.this.startActivity(mIntent);
				DetailPlaceActivity.this.finish();
				return true;
			case R.id.mn_lstfavorite:
				Intent intent = new Intent(getApplicationContext(), ListFavoriteActivity.class);
				Bundle mBundle1 = new Bundle();
					mBundle1.putDouble("curlat", curLat);			
					mBundle1.putDouble("curlon", curLon);
				intent.putExtras(mBundle1);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
			
		}
}
