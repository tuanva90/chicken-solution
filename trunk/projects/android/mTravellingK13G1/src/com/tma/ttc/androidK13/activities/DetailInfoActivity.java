package com.tma.ttc.androidK13.activities;

import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tma.ttc.androidK13.models.ListResult;
import com.tma.ttc.androidK13.models.PhoneNumbers;
import com.tma.ttc.androidK13.models.SearchResult;
import com.tma.ttc.androidK13.services.DBAdapter;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;

public class DetailInfoActivity extends Activity implements OnClickListener,
		Runnable {
	// Components on layout
	private ImageView imgCategory;
	private TextView tvCategory;
	private ListView listDetail;
	private Button btnViewWeb;
	private Button btnViewMap;
	private Button btnEmail;
	private Button btnFavourite;

	// Prepare for list view
	private LayoutInflater mInflater;
	private Vector<RowData> rowData;
	RowData rd;

	private SearchResult item;
	private ListResult listItem;
	private int iquery;
	/* private double curLAT, curLNG; */
	private DBAdapter mDbAdapter = null;

	/* Define ProgressDialog */
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create and show ProgressDialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Waiting...");
		progressDialog.show();

		// Create thread and start it
		Thread thread = new Thread(this);
		thread.start();
	}

	private String getRating(SearchResult item, double lat, double lng) {
		String rating = null;
		try {
			// Parse response Places Search
			String preURLPlace = "https://maps.googleapis.com/maps/api/place/search/json?location="
					+ lat + "," + lng + "&radius=500&name=";
			String sufURLPlace = "&sensor=false&key=AIzaSyD1udLPed3DABRug6JxnixmxLn8iX4w8IU";
			String urlRequestPlace = preURLPlace
					+ URLEncoder.encode(item.getTitleNoFormatting())
					+ sufURLPlace;
			String reference = null;

			HttpClient client = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(urlRequestPlace);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responsePlaceInfo = client.execute(getMethod,
					responseHandler);
			JSONObject jsonObject = new JSONObject(responsePlaceInfo);
			JSONArray result = jsonObject.getJSONArray("results");
			if (jsonObject.getString("status").equals("OK")) {
				JSONObject info = result.getJSONObject(0);
				if (responsePlaceInfo.contains("reference"))
					reference = info.getString("reference");
			}

			// Parse response Places Detail Search
			String urlRequestDetail = "https://maps.googleapis.com/maps/api/place/details/json?reference="
					+ reference
					+ "&sensor=true&key=AIzaSyD1udLPed3DABRug6JxnixmxLn8iX4w8IU";
			client = new DefaultHttpClient();
			getMethod = new HttpGet(urlRequestDetail);
			responseHandler = new BasicResponseHandler();
			String responseDetail = client.execute(getMethod, responseHandler);
			JSONObject jsonData = new JSONObject(responseDetail);
			JSONObject info = jsonData.getJSONObject("result");
			if (jsonData.getString("status").equals("OK")) {
				if (responseDetail.contains("rating")) {
					rating = info.getString("rating");
				}
			}

		} catch (Exception e) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					DetailInfoActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage(e.toString());
			alertDialog.setPositiveButton("Ok", null);
			alertDialog.show();
		}
		return rating;
	}

	/** Display view on screen */
	private void displayContent(int iquery) {
		tvCategory = (TextView) findViewById(R.id.txtViewCategoryDetail);
		tvCategory.setText(item.getTitleNoFormatting().toUpperCase());

		imgCategory = (ImageView) findViewById(R.id.imgCategoryDetail);

		btnViewWeb = (Button) findViewById(R.id.btnWeb);
		btnViewWeb.setOnClickListener(this);

		btnViewMap = (Button) findViewById(R.id.btnShowMapDetail);
		btnViewMap.setOnClickListener(this);

		btnEmail = (Button) findViewById(R.id.btnEmail);
		btnEmail.setOnClickListener(this);

		// button add favourite
		btnFavourite = (Button) findViewById(R.id.btnFavourite);
		btnFavourite.setOnClickListener(this);

		switch (iquery) {
		case CommonConfiguration.IGAS_STATION:
			imgCategory.setImageResource(R.drawable.ic_gasstation_96x96);
			break;
		case CommonConfiguration.ITAXI:
			imgCategory.setImageResource(R.drawable.ic_taxi_96x96);
			break;
		case CommonConfiguration.IATM:
			imgCategory.setImageResource(R.drawable.ic_atm_96x96);
			break;
		case CommonConfiguration.IHOTEL:
			imgCategory.setImageResource(R.drawable.ic_hotel_96x96);
			break;
		case CommonConfiguration.IRESTAURANT:
			imgCategory.setImageResource(R.drawable.ic_restaurant_96x96);
			break;
		case CommonConfiguration.IBANK:
			imgCategory.setImageResource(R.drawable.ic_bank_96x96);
			break;
		case CommonConfiguration.IAIRPORT:
			imgCategory.setImageResource(R.drawable.ic_airport_96x96);
			break;
		case CommonConfiguration.IPLACE:
			imgCategory.setImageResource(R.drawable.ic_place_96x96);
			break;
		}
	}

	/** Display detail */
	private void displayListView() {
		listDetail = (ListView) findViewById(R.id.list);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		rowData = new Vector<RowData>();

		// Get Address
		List<String> address = item.getAddressLines();
		if (address != null) {
			rd = new RowData("Address ", item.getAddressLines().get(0),
					CommonConfiguration.IMG_ADRESS);
			rowData.add(rd);
			if (address.size() >= 2) {
				rd = new RowData("Location ", item.getAddressLines().get(1),
						CommonConfiguration.IMG_ADRESS);
				rowData.add(rd);
			}
		}

		// Get Distance
		rd = new RowData("Distance", item.getDistance() + " km",
				CommonConfiguration.IMG_DISTANCE);
		rowData.add(rd);

		// Get Rating
		String rating = item.getRating();
		if (rating != null) {
			rd = new RowData("  Rating", rating, CommonConfiguration.IMG_RATING);
			rowData.add(rd);
		}

		// Get Phone
		List<PhoneNumbers> listPhone = item.getPhoneNumbers();
		if (listPhone != null) {
			for (int i = 0; i < item.getPhoneNumbers().size(); i++) {
				// String phoneType = item.getPhoneNumbers().get(i).getType();
				String phoneNumber = item.getPhoneNumbers().get(i).getNumber();
				if (listPhone.size() == 1) {
					if (phoneNumber != null) {
						rd = new RowData("Phone", phoneNumber,
								CommonConfiguration.IMG_PHONE);
						rowData.add(rd);
					}
				} else {
					int index = i + 1;
					rd = new RowData("Phone " + index, phoneNumber,
							CommonConfiguration.IMG_PHONE);
					rowData.add(rd);
				}
			}
		}

		CustomAdapter adapter = new CustomAdapter(this, R.layout.row_details,
				R.id.txtTitle, rowData);
		listDetail.setAdapter(adapter);

		/** Handle event: click on an item */
		listDetail.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					RowData item = (RowData) listDetail.getItemAtPosition(arg2);
					if (item.getmTitle().contains("Phone")) {
						Intent intent = new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:"
								+ item.getmDetail().trim()));
						startActivity(intent);
					}
				} catch (Exception e) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							DetailInfoActivity.this);
					alertDialog.setTitle("Error");
					alertDialog.setMessage(e.toString());
					alertDialog.setPositiveButton("Ok", null);
					alertDialog.show();
				}
			}
		});
	}

	/** Class RowData */
	private class RowData {

		protected String mTitle;
		protected String mDetail;
		protected int mIconResource;

		RowData(String title, String detail, int iconResource) {
			mTitle = title;
			mDetail = detail;
			mIconResource = iconResource;
		}

		public String getmTitle() {
			return mTitle;
		}

		public String getmDetail() {
			return mDetail;
		}

		@Override
		public String toString() {
			return mTitle + " " + mDetail + " " + mIconResource;
		}

	}

	/** Create class CustomAdapter for display listview */
	private class CustomAdapter extends ArrayAdapter<RowData> {

		public CustomAdapter(Context context, int resource,
				int textViewResourceId, List<RowData> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			TextView title = null;
			TextView detail = null;
			ImageView i11 = null;
			RowData rowData = getItem(position);
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.row_details, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			title = holder.getTitle();
			title.setText(rowData.mTitle);
			detail = holder.getInfo();
			detail.setText(rowData.mDetail);
			i11 = holder.getImage();
			i11.setImageResource(rowData.mIconResource);
			return convertView;
		}

		private class ViewHolder {
			private View mRow;
			private TextView title = null;
			private TextView info = null;
			private ImageView i11 = null;

			public ViewHolder(View row) {
				mRow = row;
			}

			public TextView getTitle() {
				if (null == title) {
					title = (TextView) mRow.findViewById(R.id.txtTitle);
				}
				return title;
			}

			public TextView getInfo() {
				if (null == info) {
					info = (TextView) mRow.findViewById(R.id.txtInfo);
				}
				return info;
			}

			public ImageView getImage() {
				if (null == i11) {
					i11 = (ImageView) mRow.findViewById(R.id.imgIcon);
				}
				return i11;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnViewWeb) { // Click on button ViewWeb
			Bundle bundle = new Bundle();
			bundle.putString(CommonConfiguration.URL_TO_WEB, item.getUrl());
			Intent intent = new Intent(DetailInfoActivity.this,
					ViewWebActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (v == btnViewMap) { // Click on button ViewMap
			Bundle bundle = new Bundle();

			bundle.putSerializable(CommonConfiguration.RESPECT_LOCATION, item);
			bundle.putSerializable(CommonConfiguration.SEARCH_RESULT_LIST,
					listItem);
			Intent intent = new Intent(DetailInfoActivity.this,
					ViewMapActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (v == btnEmail) { // Click on button Email
			Bundle bundle = new Bundle();
			bundle.putSerializable(CommonConfiguration.SEARCH_RESULT, item);
			Intent intent = new Intent(DetailInfoActivity.this,
					SendMailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		} else if (v == btnFavourite) { // Click on button Favourite
			if (item != null) {
				try {
					String strName = item.getTitleNoFormatting();
					String strAddress = "";

					if (item.getAddressLines() != null) {
						int countAddress = item.getAddressLines().size();
						Log.i("Detail",
								" DetailActivity - mClickbtnFavourite - countAddress ="
										+ countAddress);
						for (int i = 0; i < countAddress; i++) {
							String s = item.getAddressLines().get(i);
							if (i == (countAddress - 1)) {
								strAddress += s;
							} else {
								strAddress += s + ";";
							}
						}
					}

					String strPhone = "";
					if (item.getPhoneNumbers() != null) {
						int countPhone = item.getPhoneNumbers().size();
						Log.i("Detail",
								" DetailActivity - mClickbtnFavourite - countPhone ="
										+ countPhone);
						for (int i = 0; i < countPhone; i++) {
							String s = item.getPhoneNumbers().get(i).getType()
									+ ":"
									+ item.getPhoneNumbers().get(i).getNumber();
							if (i == (countPhone - 1)) {
								strPhone += s;
							} else {
								strPhone += s + "--";
							}
						}
					}
					String strRating = null;
					if (item.getRating() != null)
						strRating = item.getRating();

					String strLat = Double.toString(item.getLat());
					String strLng = Double.toString(item.getLng());
					String strDistance = Double.toString(item.getDistance());
					String strUrl = item.getUrl();
					String strIdc = Integer.toString(iquery);

					// check if it exists in db, if not then insert, else do
					// nothing
					if (mDbAdapter.checkIfExist(strLat, strLng, strAddress) == false) {
						// mDBAdapter.insertItem(strName,
						// strAddress,"phone","lat","lng","999","url","0");
						Log.i("checkIfExist", " Details - checkIfExist false");
						mDbAdapter.insertItem(strName, strAddress, strPhone,
								strLat, strLng, strRating, strDistance, strUrl,
								strIdc);

						// Create the alert box
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								DetailInfoActivity.this);

						// Set the message to display
						alertbox.setMessage("Add favourite successfully!");

						// Add a neutral button to the alert box and assign a
						// click listener
						alertbox.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {
									// Click listener on the neutral button of
									// alert box
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								});
						// show alert box
						alertbox.show();
					} else {
						Log.i("checkIfExist", " Details - checkIfExist true");
						// Create the alert box
						AlertDialog.Builder alertbox = new AlertDialog.Builder(
								DetailInfoActivity.this);

						// Set the message to display
						alertbox.setMessage("This place existed in your favourite list!");

						// Add a neutral button to the alert box and assign a
						// click listener
						alertbox.setNeutralButton("Ok",
								new DialogInterface.OnClickListener() {
									// Click listener on the neutral button of
									// alert box
									public void onClick(DialogInterface arg0,
											int arg1) {

									}
								});
						// show alert box
						alertbox.show();
					}
				} catch (Exception e) {
					Log.i("Detail",
							" DetailActivity - mClickbtnFavourite - exception"
									+ e.toString());
				}
			}
		}
	}

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
			Intent intent = new Intent(DetailInfoActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(DetailInfoActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(DetailInfoActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// Process.killProcess(Process.myPid());
			Intent intent = new Intent(DetailInfoActivity.this,
					MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	/** Method of interface Runable */
	@Override
	public void run() {
		try {
			if (this.mDbAdapter == null) {
				this.mDbAdapter = new DBAdapter(this);
				mDbAdapter.open();
			}
			// Get data from intent
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			iquery = bundle.getInt(CommonConfiguration.IQUERY);
			item = (SearchResult) bundle
					.getSerializable(CommonConfiguration.SEARCH_RESULT);
			listItem = (ListResult) bundle
					.getSerializable(CommonConfiguration.SEARCH_RESULT_LIST);

			if (listItem != null && iquery == CommonConfiguration.IHOTEL) {
				double curLAT = CommonConfiguration.LATITUDE;
				double curLNG = CommonConfiguration.LONGITUDE;
				String rating = getRating(item, curLAT, curLNG);
				if (rating != null)
					item.setRating(rating);
			}
		} catch (Exception e) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					DetailInfoActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage(e.toString());
			alertDialog.setPositiveButton("Ok", null);
			alertDialog.show();
		}
		handler.sendEmptyMessage(0);
	}

	/** Handler for handling message from method run() */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			// dismiss dialog
			progressDialog.dismiss();
			// set layout
			setContentView(R.layout.details);

			displayContent(iquery);
			displayListView();
		}
	};

}