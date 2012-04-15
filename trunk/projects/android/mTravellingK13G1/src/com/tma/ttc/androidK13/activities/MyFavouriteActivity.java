/**
 * 
 */
package com.tma.ttc.androidK13.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tma.ttc.androidK13.models.PhoneNumbers;
import com.tma.ttc.androidK13.models.SearchResult;
import com.tma.ttc.androidK13.services.DBAdapter;
import com.tma.ttc.androidK13.services.DBHelper;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;
import com.tma.ttc.androidK13.utilities.CustomCursorAdapter;

/**
 * @author LEHIEU
 * 
 */
public class MyFavouriteActivity extends ListActivity {

	private CustomCursorAdapter mCustomCursorAdapter = null;
	private DBAdapter mDbAdapter = null;
	private DBHelper mDbHelper = null;
	private Cursor currentCursor = null;
	private ListView listView = null;

	/* Latitude - Longitude */
	private double currentLatitude;
	private double currentLongitude;

	/* parameter for Select Limit (class DBAdapter - function getItemsFromTo) */
	private int limit_from = 0;
	private int limit_count = 5;
	private int rowReturnsCount = 0;
	private int sumPage = 0;
	private int curPage = 1;
	private String keyWord = "";

	Button btnDelete;
	Button btnPrev;
	Button btnNext;
	Button btnSearch;
	EditText txtSearch;
	CheckBox ckbCheckAll;
	TextView txtViewPage;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_favourite);

		if (this.mDbAdapter == null) {
			this.mDbAdapter = new DBAdapter(this);
			mDbAdapter.open();
		}

		if (this.mDbHelper == null) {
			this.mDbHelper = mDbAdapter.getmDbHelper();
		}

		if (listView == null) {
			listView = getListView();
		}

		Log.i("Favourite", "MyFavouriteActivity - onCreate - start");
		try {
			Search(keyWord, limit_from, limit_count);

			// button search
			btnDelete = (Button) findViewById(R.id.btnSearch);
			btnDelete.setOnClickListener(mSearchListener);

			// textview search
			txtSearch = (EditText) findViewById(R.id.txtSearch);

			// button delete
			btnDelete = (Button) findViewById(R.id.btnDelete);
			btnDelete.setOnClickListener(mDeleteListener);

			// button prev
			btnPrev = (Button) findViewById(R.id.btnPrev);
			btnPrev.setOnClickListener(mPrevListener);

			// button next
			btnNext = (Button) findViewById(R.id.btnNext);
			btnNext.setOnClickListener(mNextListener);

			// textview page
			txtViewPage = (TextView) findViewById(R.id.txtViewPage);
			txtViewPage.setText(curPage + "/" + sumPage);

			// checkbox CheckAll
			ckbCheckAll = (CheckBox) findViewById(R.id.checkboxCheckAll);
			ckbCheckAll.setOnCheckedChangeListener(mCheckAllListener);

			// Handle item click event
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					try {
						long itemId = listView.getItemIdAtPosition(arg2);
						Cursor c = mDbAdapter.getItem(itemId);
						startManagingCursor(c);
						c.moveToFirst(); // move to the 1st row (the only row
											// returns)

						String titleNoFormatting = c.getString(c
								.getColumnIndex("name"));
						Log.i("ParseDB", "titleNoFormatting"
								+ titleNoFormatting);

						List<String> addressLines = new ArrayList<String>();
						String strAddressLines_tmp = c.getString(c
								.getColumnIndex("address"));
						String[] arrAddressLines = strAddressLines_tmp
								.split(";");
						for (int i = 0; i < arrAddressLines.length; i++) {
							Log.i("ParseDB", "arrAddressLines"
									+ arrAddressLines[i]);
							addressLines.add(arrAddressLines[i]);
						}

						List<PhoneNumbers> phoneNumbers = new ArrayList<PhoneNumbers>();
						String phoneNumbers_tmp = c.getString(c
								.getColumnIndex("phone"));
						// EX: phoneNumbers_tmp =
						// "home:(022) 220 1--company:(022) 220 2--fax:(022) 220 3";
						String[] arrObjPhoneNumbers = phoneNumbers_tmp
								.split("--");
						for (int i = 0; i < arrObjPhoneNumbers.length; i++) {
							String[] objPhone = arrObjPhoneNumbers[i]
									.toString().split(":");
							Log.i("ParseDB", "arrObjPhoneNumbers"
									+ arrObjPhoneNumbers[i]);
							PhoneNumbers objPN = new PhoneNumbers();

							for (int j = 0; j < objPhone.length; j++) {
								Log.i("ParseDB", "---objPhone" + objPhone[j]);

								if (j == 0) {
									objPN.setType(objPhone[j]);
								} else if (j == 1) {
									objPN.setNumber(objPhone[j]);
								}
							}
							phoneNumbers.add(objPN);
						}

						double lat = Double.parseDouble(c.getString(c
								.getColumnIndex("lat")));
						Log.i("ParseDB", "lat" + lat);
						double lng = Double.parseDouble(c.getString(c
								.getColumnIndex("lng")));
						Log.i("ParseDB", "lng" + lng);
						String rating = c.getString(c.getColumnIndex("rating"));
						double distance = Double.parseDouble(c.getString(c
								.getColumnIndex("distance")));
						Log.i("ParseDB", "distance" + distance);
						String url = c.getString(c.getColumnIndex("url"));
						Log.i("ParseDB", "url" + url);
						int id = -1;
						int idc = Integer.parseInt(c.getString(c
								.getColumnIndex("idc"))); // id of category
															// (iquery)
						Log.i("ParseDB", "id" + id);
						Log.i("ParseDB", "idc" + idc);

						// create SearchResult obj here
						SearchResult objSR = new SearchResult();
						objSR.setTitleNoFormatting(titleNoFormatting);
						objSR.setAddressLines(addressLines);
						objSR.setPhoneNumbers(phoneNumbers);
						objSR.setLat(lat);
						objSR.setLng(lng);
						objSR.setRating(rating);

						currentLatitude = CommonConfiguration.LATITUDE;
						currentLongitude = CommonConfiguration.LONGITUDE;

						objSR.setDistance(currentLatitude, currentLongitude); // lay
																				// lat,
																				// lng
																				// cua
																				// location
																				// hien
																				// tai
																				// quang
																				// vao
																				// day
						objSR.setUrl(url);
						objSR.setId(id);

						Log.i("ParseDB",
								"objSR.getTitle = "
										+ objSR.getTitleNoFormatting());

						// send them to Details Activity
						Bundle bundle = new Bundle();
						bundle.putInt(CommonConfiguration.IQUERY, idc);
						bundle.putDouble(CommonConfiguration.CURRENT_LATITUDE,
								currentLatitude);
						bundle.putDouble(CommonConfiguration.CURRENT_LONGITUDE,
								currentLongitude);
						bundle.putSerializable(
								CommonConfiguration.SEARCH_RESULT, objSR);
						Intent i = new Intent(MyFavouriteActivity.this,
								DetailInfoActivity.class);
						i.putExtras(bundle);

						startActivity(i);
					} catch (Exception e) {
						Log.i("ItemClick", "Exception" + e.toString());
					}
				}
			});

		} catch (Exception e) {
			Log.i("Favourite",
					"MyFavouriteActivity - onCreate - exception" + e.toString());
		}

		Log.i("Favourite", "MyFavouriteActivity - onCreate - end");
	}

	private CompoundButton.OnCheckedChangeListener mCheckAllListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			try {
				Log.i("Delete",
						"mCheckAllListener - check" + listView.getCount());

				if (ckbCheckAll.isChecked()) {
					for (int i = 0; i < listView.getCount(); ++i) {
						Log.i("Delete",
								"mCheckAllListener checked- before getChildAt i = "
										+ i);
						ViewGroup row = (ViewGroup) listView.getChildAt(i);
						Log.i("Delete",
								"mCheckAllListener checked- before row.findViewById");
						CheckBox check = (CheckBox) row
								.findViewById(R.id.checkboxItem);
						Log.i("Delete",
								"mCheckAllListener checked- before setChecked(true)");
						check.setChecked(true);

					}
				} else {
					for (int i = 0; i < listView.getCount(); ++i) {
						Log.i("Delete",
								"mCheckAllListener unchecked- before getChildAt i = "
										+ i);
						ViewGroup row = (ViewGroup) listView.getChildAt(i);
						Log.i("Delete",
								"mCheckAllListener unchecked- before row.findViewById");
						CheckBox check = (CheckBox) row
								.findViewById(R.id.checkboxItem);
						Log.i("Delete",
								"mCheckAllListener unchecked- before setChecked(false)");
						check.setChecked(false);

					}
				}
			} catch (Exception e) {
				Log.i("Delete",
						" MyFavouriteActivity - mCheckAllListener - Exception"
								+ e.toString());
			}

		}
	};

	private View.OnClickListener mDeleteListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			final ArrayList<Integer> a = new ArrayList<Integer>();
			for (int i = 0; i < listView.getCount(); ++i) {
				ViewGroup row = (ViewGroup) listView.getChildAt(i);
				CheckBox check = (CheckBox) row.findViewById(R.id.checkboxItem);
				// hidden_id is text value of the checkbox
				// check
				int hidden_id = Integer.parseInt(check.getText().toString());
				if (check.isChecked()) {
					Log.i("Delete",
							" MyFavouriteActivity - View.OnClickListener - mDeleteListener - id = "
									+ hidden_id);
					a.add(hidden_id);

				}
			}
			if (!a.isEmpty()) {
				AlertDialog.Builder alertbox = new AlertDialog.Builder(
						MyFavouriteActivity.this);
				alertbox.setMessage("Are you sure want to delete?");

				alertbox.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								try {
									for (int i = 0; i < a.size(); i++) {
										mDbAdapter.deleteItem(a.get(i));
									}

									// uncheck checkbox CheckAll
									CheckBox checkAll = (CheckBox) findViewById(R.id.checkboxCheckAll);
									if (checkAll.isChecked()) {
										checkAll.setChecked(false);
									}
									Log.i("Delete",
											" MyFavouriteActivity - View.OnClickListener - mDeleteListener - before search");
									// fillData
									keyWord = txtSearch.getText().toString();
									limit_from = 0;
									curPage = 1;
									Search(keyWord, limit_from, limit_count);
									txtViewPage
											.setText(curPage + "/" + sumPage);
									Log.i("Delete",
											" MyFavouriteActivity - View.OnClickListener - mDeleteListener - after search");

								} catch (Exception e) {
									Log.i("Favourite",
											" MyFavouriteActivity - View.OnClickListener - mDeleteListener - exception"
													+ e.toString());
								}

							}

						});

				// Set a negative/no button and create a listener

				alertbox.setNegativeButton("No",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// do nothing
							}
						});

				alertbox.show();
				Log.i("Favourite",
						" MyFavouriteActivity - View.OnClickListener - mDeleteListener - end");
			} else {
				AlertDialog.Builder alertbox = new AlertDialog.Builder(
						MyFavouriteActivity.this);
				alertbox.setMessage("No item is selected!");
				alertbox.setNegativeButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								// do nothing
							}
						});
				alertbox.show();
			}

		}
	};

	private View.OnClickListener mPrevListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i("mPrevListener",
					" MyFavouriteActivity - View.OnClickListener - mPrevListener - start");
			Log.i("SelectLimit", "Search - rowReturnsCount = "
					+ rowReturnsCount);
			Log.i("SelectLimit", "Search - limit_from = " + limit_from);
			try {
				if (limit_from - 5 >= 0) {
					limit_from = limit_from - 5;
					Search(keyWord, limit_from, limit_count);
					curPage -= 1;
					txtViewPage.setText(curPage + "/" + sumPage);
					// uncheck checkbox CheckAll
					CheckBox checkAll = (CheckBox) findViewById(R.id.checkboxCheckAll);
					if (checkAll.isChecked()) {
						checkAll.setChecked(false);
					}
				}

			} catch (Exception e) {
				Log.i("mPrevListener",
						" MyFavouriteActivity - View.OnClickListener - mPrevListener - exception"
								+ e.toString());
			}
			Log.i("mPrevListener",
					" MyFavouriteActivity - View.OnClickListener - mPrevListener - end");
		}
	};

	private View.OnClickListener mNextListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i("mPrevListener",
					" MyFavouriteActivity - View.OnClickListener - mPrevListener - start");
			Log.i("SelectLimit", "Search - rowReturnsCount = "
					+ rowReturnsCount);
			Log.i("SelectLimit", "Search - limit_from = " + limit_from);
			try {
				if (limit_from + 5 < rowReturnsCount) {
					limit_from = limit_from + 5;
					Search(keyWord, limit_from, limit_count);
					curPage += 1;
					txtViewPage.setText(curPage + "/" + sumPage);
					// uncheck checkbox CheckAll
					CheckBox checkAll = (CheckBox) findViewById(R.id.checkboxCheckAll);
					if (checkAll.isChecked()) {
						checkAll.setChecked(false);
					}
				}

			} catch (Exception e) {
				Log.i("mPrevListener",
						" MyFavouriteActivity - View.OnClickListener - mPrevListener - exception"
								+ e.toString());
			}
			Log.i("mPrevListener",
					" MyFavouriteActivity - View.OnClickListener - mPrevListener - end");
		}
	};

	private View.OnClickListener mSearchListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i("Favourite",
					" MyFavouriteActivity - View.OnClickListener - mSearchListener - start");
			try {
				keyWord = txtSearch.getText().toString();
				limit_from = 0;
				curPage = 1;
				Search(keyWord, limit_from, limit_count);
				txtViewPage.setText(curPage + "/" + sumPage);

			} catch (Exception e) {
				Log.i("Favourite",
						" MyFavouriteActivity - View.OnClickListener - mSearchListener - exception"
								+ e.toString());
			}
			Log.i("Favourite",
					" MyFavouriteActivity - View.OnClickListener - mSearchListener - end");
		}
	};

	private void Search(String keyWord, int lm_from, int lm_count) {
		Log.i("Favourite", "MyFavouriteActivity - Search - start");
		try {
			this.currentCursor = mDbAdapter.getItemsLikeThisFromTo(keyWord,
					lm_from, lm_count);
			rowReturnsCount = mDbAdapter.getRowReturnCount(keyWord);

			if (rowReturnsCount % limit_count > 0) {
				sumPage = rowReturnsCount / limit_count + 1;
			} else {
				sumPage = rowReturnsCount / limit_count;
			}

			Log.i("SelectLimit", "Search - rowReturnsCount = "
					+ rowReturnsCount);
			Log.i("SelectLimit", "Search - limit_from = " + limit_from);
			startManagingCursor(currentCursor);

			String[] from = new String[] { DBAdapter.KEY_ROWID.toString(),
					DBAdapter.KEY_NAME.toString(), DBAdapter.KEY_ADDRESS };
			int[] to = new int[] { R.id.checkboxItem, R.id.txtViewName,
					R.id.txtViewAddress };

			// Now create an array adapter and set it to display using our row
			this.mCustomCursorAdapter = new CustomCursorAdapter(this,
					R.layout.row_my_favourite, currentCursor, from, to,
					mDbHelper);

			setListAdapter(mCustomCursorAdapter);

		} catch (Exception e) {
			Log.i("Favourite",
					"MyFavouriteActivity - Search - exception" + e.toString());
		}
		Log.i("Favourite", "MyFavouriteActivity - Search - end");
	}

	/** Get current latitude and longitude */
	/*
	 * private void getLatitudeLongitude() { // Get the location manager
	 * locationManager = (LocationManager)
	 * getSystemService(Context.LOCATION_SERVICE); // Provider is GPS provider =
	 * LocationManager.GPS_PROVIDER; Location location =
	 * locationManager.getLastKnownLocation(provider); // Initialize the
	 * location fields if (location != null) { currentLatitude =
	 * location.getLatitude(); currentLongitude = location.getLongitude(); }
	 * else { currentLatitude = CommonConfiguration.LATITUDE; currentLongitude =
	 * CommonConfiguration.LONGITUDE; } }
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option3, menu);
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
			Intent intent = new Intent(MyFavouriteActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(MyFavouriteActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(MyFavouriteActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
			// Intent intent = new Intent(Intent.ACTION_MAIN);
			// intent.addCategory(Intent.CATEGORY_HOME);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// Process.killProcess(Process.myPid());
			Intent intent = new Intent(MyFavouriteActivity.this,
					MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
