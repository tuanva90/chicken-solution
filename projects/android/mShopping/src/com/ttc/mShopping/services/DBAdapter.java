package com.ttc.mShopping.services;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author LEHIEU
 * 
 */

public class DBAdapter {

	public static final String DATABASE_TABLE = "Favourite";

	// columns are the same to SearchResult properties
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_RATING = "rating";
	public static final String KEY_URL = "url";
	public static final String KEY_IDC = "idc"; //id of Catefory (iquery)

	public DBHelper mDbHelper;
	public SQLiteDatabase mDb;
	public final Context mCtx;

	/**
	 * Constructor, set the variable context
	 * 
	 * @param context
	 */
	public DBAdapter(Context context) {
		mCtx = context;
		Log.i("FavouriteDB", "DBAdapter - constructor - start");
	}
	
	/**
	 * @return the mDbHelper
	 */
	public DBHelper getmDbHelper() {
		return mDbHelper;
	}

	/**
	 * Open DB, enable to write
	 * 
	 * @return DBAdapter
	 * @throws SQLException
	 */
	public DBAdapter open() throws SQLException {
		mDbHelper = new DBHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		Log.i("FavouriteDB", "DBAdapter - open() - start");
		return this;
	}

	/**
	 * Close DB
	 */
	public void close() {
		mDbHelper.close();
		if(mDb.isOpen())
			mDb.close();
		if(mDb != null)
			mDb = null;
	}
		
	/**
	 * Delete DB
	 */
	public void deleteDatabase() {
		mDbHelper.close();
		try {
			mCtx.deleteDatabase("mFavouriteDB");
			Log.i("FavouriteDB", "DBAdapter - deleteDatabase() - start");
		} catch (Exception e) {
			Log.i("FavouriteDB", "DBAdapter - deleteDatabase() - exception" + e.toString());
		}
	}

	/**
	 * Return a Cursor over the list of all row of the table
	 * 
	 * @return Cursor over all favorites
	 */
	public Cursor getAllItems() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC }, null, null, null, null, KEY_NAME);

	}
	
	/**
	 * Return a Cursor of the row has the itemId
	 * 
	 * @return Cursor of the row has the itemId
	 */
	public Cursor getItem(long itemId) {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC }, KEY_ROWID + "=" + itemId, null, null, null, null);

	}
	
	/**
	 * Return a Cursor over the list of all row of the table which contain the key word
	 * 
	 * @return Cursor over all favorites which contain the key word
	 */
	public Cursor getItemsLikeThis(String keyWord) {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG,KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC },
				KEY_NAME + " like " + "'%" + keyWord + "%'", null, null, null,
				KEY_NAME);

	}
	
	/**
	 * Return a boolean check
	 * 
	 * @return Return a boolean check
	 */
	public boolean checkIfExist(String lat,String lng, String address) {
		Log.i("checkIfExist"," DBAdapter - checkIfExist start");
		/*Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_DISTANCE,
				KEY_URL, KEY_IDC },
				KEY_NAME +"="+ name + " and " + KEY_ADDRESS +"="+ address + " and " + KEY_PHONE +"="+ phone + " and " + KEY_LAT +"="+ lat + " and " + KEY_LNG +"="+ lng + " and " + KEY_URL +"="+ url, null, null, null,
				null);*/
		
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC },
				KEY_LAT +"="+ lat + " and " + KEY_LNG +"="+ lng +" and " + KEY_ADDRESS + " like " + "'%" + address + "%'", null, null, null,
				null);
		Log.i("checkIfExist"," DBAdapter - checkIfExist after Cursor");
		if(c.moveToFirst() == false) {
			Log.i("checkIfExist"," DBAdapter - checkIfExist false");
			return false;
		}
		else{
			Log.i("checkIfExist"," DBAdapter - checkIfExist true"); 
			return true;
		}

	}

	/**
	 * Create a new favorite using the title and body provided. If the favorite
	 * is successfully created return the new rowId for that favorite, otherwise
	 * return -1
	 */
	public long insertItem(String name, String address, String phone,
			String lat, String lng, String rating, String distance, String url, String idc) {
		Log.i("DBAdapter","insertItem" + name + address);
		ContentValues insertedValue = new ContentValues();

		insertedValue.put(KEY_NAME, name);
		insertedValue.put(KEY_ADDRESS, address);
		insertedValue.put(KEY_PHONE, phone);
		insertedValue.put(KEY_LAT, lat);
		insertedValue.put(KEY_LNG, lng);
		insertedValue.put(KEY_RATING, rating);
		insertedValue.put(KEY_DISTANCE, distance);
		insertedValue.put(KEY_URL, url);
		insertedValue.put(KEY_IDC, idc);
		
		long kq = mDb.insert(DATABASE_TABLE, null, insertedValue);
		Log.i("DBAdapter","insertItem - kq =" + kq);
		return kq;
		//return mDb.insert(DATABASE_TABLE, null, insertedValue);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of the row need to delete
	 * @return the number of rows affected if successful, otherwise return 0
	 */
	public long deleteItem(long rowId) {
		// return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null);
	}
	
	public boolean deleteItem(String lat,String lng, String address)
	{
		Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC },
				KEY_LAT +"="+ lat + " and " + KEY_LNG +"="+ lng +" and " + KEY_ADDRESS + " like " + "'%" + address + "%'", null, null, null,
				null);
		Log.i("checkIfExist"," DBAdapter - checkIfExist after Cursor");
		if(c.moveToFirst() == false) {
			Log.i("checkIfExist"," DBAdapter - checkIfExist false");
			return false;
		}
		else{
			long id = Long.parseLong(c.getString(c.getColumnIndexOrThrow(KEY_ROWID)));
			long kq = deleteItem(id);
			Log.i("checkIfExist"," DBAdapter - checkIfExist true"); 
			if(kq != 0)
				return true;
			else
				return false;
		}
	}
	
	public Cursor getItemsFromTo(int from, int count) {
		Log.i("SelectLimit", "DBAdapter - getItemsFromTo = " + from + "and" + count);
		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC }, null, null, null, null, KEY_NAME, from +","+count);

	}
	
	public Cursor getItemsLikeThisFromTo(String keyWord, int from, int count) {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_NAME,
				KEY_ADDRESS, KEY_PHONE, KEY_LAT, KEY_LNG, KEY_RATING, KEY_DISTANCE,
				KEY_URL, KEY_IDC },
				KEY_NAME + " like " + "'%" + keyWord + "%'", null, null, null,
				KEY_NAME,from +","+count);

	}
	
	public int getRowReturnCount(String keyWord) {

		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID}, KEY_NAME + " like " + "'%" + keyWord + "%'", null, null, null, null).getCount();

	}

}
