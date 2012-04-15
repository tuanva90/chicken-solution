package com.tma.ttc.androidK13.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.maps.MapActivity;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;

public class ChangeLocationActivity extends MapActivity implements
		OnClickListener {

	Geocoder geocoder = null;
	AutoCompleteTextView txtLocation;
	Button btnSearch;
	Button btnCurrentLocation;
	AlertDialog.Builder builder;
	ListView lvLocationList;

	List<Address> addressList;

	private ArrayList<String> listcity;

	@Override
	protected boolean isLocationDisplayed() {
		return false;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	static final String[] COUNTRIES = new String[] { "Afghanistan", "Albania",
			"Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
			"Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
			"Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
			"Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
			"Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
			"Botswana", "Bouvet Island", "Brazil",
			"British Indian Ocean Territory", "British Virgin Islands",
			"Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
			"Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
			"Central African Republic", "Chad", "Chile", "China",
			"Christmas Island", "Cocos (Keeling) Islands", "Colombia",
			"Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
			"Cuba", "Cyprus", "Czech Republic",
			"Democratic Republic of the Congo", "Denmark", "Djibouti",
			"Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
			"El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
			"Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
			"Finland", "Former Yugoslav Republic of Macedonia", "France",
			"French Guiana", "French Polynesia", "French Southern Territories",
			"Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
			"Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
			"Guinea", "Guinea-Bissau", "Guyana", "Haiti",
			"Heard Island and McDonald Islands", "Honduras", "Hong Kong",
			"Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
			"Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
			"Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
			"Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
			"Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
			"Malawi", "Malaysia", "Maldives", "Mali", "Malta",
			"Marshall Islands", "Martinique", "Mauritania", "Mauritius",
			"Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
			"Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
			"Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
			"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
			"Niue", "Norfolk Island", "North Korea", "Northern Marianas",
			"Norway", "Oman", "Pakistan", "Palau", "Panama",
			"Papua New Guinea", "Paraguay", "Peru", "Philippines",
			"Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
			"Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
			"Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
			"Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
			"Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
			"Sierra Leone", "Singapore", "Slovakia", "Slovenia",
			"Solomon Islands", "Somalia", "South Africa",
			"South Georgia and the South Sandwich Islands", "South Korea",
			"Spain", "Sri Lanka", "Sudan", "Suriname",
			"Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
			"Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
			"The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
			"Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
			"Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
			"Ukraine", "United Arab Emirates", "United Kingdom",
			"United States", "United States Minor Outlying Islands", "Uruguay",
			"Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
			"Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
			"Zambia", "Zimbabwe" };
	int j;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_location);

		builder = new AlertDialog.Builder(this);

		listcity = new ArrayList<String>();

		txtLocation = (AutoCompleteTextView) findViewById(R.id.txtSearch);
		ArrayAdapter<String> adapter_AutoComplete = new ArrayAdapter<String>(
				this, R.layout.list_item, COUNTRIES);
		txtLocation.setAdapter(adapter_AutoComplete);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnCurrentLocation = (Button) findViewById(R.id.btnBackToCurrent);
		lvLocationList = (ListView) findViewById(R.id.listResult);

		geocoder = new Geocoder(this, Locale.getDefault());
		btnSearch.setOnClickListener(this);
		btnCurrentLocation.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSearch: {
			String locationName = "";
			locationName = txtLocation.getText().toString();
			Log.i(CommonConfiguration.TAG, "locationName = " + locationName);
			if ((locationName != "") || locationName.equals(" ")) {
				try {
					// lvLocationList.afterTextChanged(null);
					// lvLocationList.clearTextFilter();
					addressList = geocoder.getFromLocationName(locationName, 5);

					if (addressList.size() > 0) {
						addressList = geocoder.getFromLocationName(
								locationName, 5);
					}
					if (addressList != null && addressList.size() > 0) {
						for (int i = 0; i < addressList.size(); i++) {
							listcity.add(addressList.get(i).getAddressLine(i)
									+ " " + addressList.get(i).getCountryName());
						}

						int[] to = { R.id.txtViewAddress };
						String[] from = { CommonConfiguration.VIEW_ADDRESS };
						ArrayList<HashMap<String, String>> resultAddressList = new ArrayList<HashMap<String, String>>();
						HashMap<String, String> resultMap;

						for (int i = 0; i < addressList.size(); i++) {
							resultMap = new HashMap<String, String>();
							resultMap.put(CommonConfiguration.VIEW_ADDRESS,
									addressList.get(i).getAddressLine(i)
											+ ", "
											+ addressList.get(i)
													.getCountryName());
							resultAddressList.add(resultMap);
						}

						SimpleAdapter simpleAdapter = new SimpleAdapter(this,
								resultAddressList,
								R.layout.row_change_location, from, to);
						lvLocationList.setAdapter(simpleAdapter);

						lvLocationList
								.setOnItemClickListener(new AdapterView.OnItemClickListener() {
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										j = position;
										for (int i = 0; i < addressList.size(); i++) {

											builder.setMessage("Do you want to change location?");
											builder.setPositiveButton(
													"Yes",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															try {
																double lat = (addressList
																		.get(j)
																		.getLatitude());
																double lng = (addressList
																		.get(j)
																		.getLongitude());

																CommonConfiguration.LATITUDE = lat;
																CommonConfiguration.LONGITUDE = lng;
																CommonConfiguration.IS_CHANGE = true;

																Intent changeLocationIntent = new Intent(
																		ChangeLocationActivity.this,
																		ListCategoriesActivity.class);
																startActivity(changeLocationIntent);
															} catch (Exception e) {
															}
														}
													});
											builder.setNegativeButton("No",
													null);
											builder.show();
										}
									}
								});

					} else {

						builder.setMessage("No searh result. \nPlease try again !");
						builder.setPositiveButton("OK", null);
						builder.show();
					}

				} catch (Exception e) {
					Log.i(CommonConfiguration.TAG,
							"true exception: " + e.toString());
				}
			} else {
				builder.setTitle("Alert !");
				builder.setMessage("Please enter location to search !");
				builder.setPositiveButton("OK", null);
				builder.show();
			}
			break;
		}
		case R.id.btnBackToCurrent: {
			// Get the location manager
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			// Provider is GPS
			String provider = LocationManager.GPS_PROVIDER;
			Location location = locationManager.getLastKnownLocation(provider);
			// Initialize the location fields
			if (location != null) {
				CommonConfiguration.LATITUDE = location.getLatitude();
				CommonConfiguration.LONGITUDE = location.getLongitude();
			} else {
				CommonConfiguration.LATITUDE = 10.7783;
				CommonConfiguration.LONGITUDE = 106.6962;
			}
			Intent changeLocationIntent = new Intent(
					ChangeLocationActivity.this, ListCategoriesActivity.class);
			startActivity(changeLocationIntent);
		}
		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option2, menu);
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
			Intent intent = new Intent(ChangeLocationActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(ChangeLocationActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(ChangeLocationActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// Process.killProcess(Process.myPid());
			Intent intent = new Intent(ChangeLocationActivity.this,
					MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}