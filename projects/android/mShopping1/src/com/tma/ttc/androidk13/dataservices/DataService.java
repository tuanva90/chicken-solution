package com.tma.ttc.androidk13.dataservices;

import java.util.ArrayList;

import com.tma.ttc.androidk13.models.FavoriteModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataService {
	 public static final String KEY_LAT = "lat";
	    public static final String KEY_LNG = "lng";
	    public static final String KEY_ADDRESS = "address";
	    public static final String KEY_ADDRESS_LINES = "addresslines";
	    public static final String KEY_PHONENUMBER = "phone";
	    public static final String KEY_MAPURL = "mapurl";
	    public static final String KEY_WEBURL = "weburl";
	    public static final String KEY_ROWID = "_id";
	    
	    public static final String KEY_TITLE = "title";

	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    
	    private static final String DATABASE_NAME = "Favorites";
	    private static final String DATABASE_TABLE = "FavoritesTbl";
	    private static final int DATABASE_VERSION = 1;

	    private final Context mCtx;
	    
	    private static final String DATABASE_CREATE = "create table FavoritesTbl (_id integer primary key autoincrement, "
			+ "title text, address text, addresslines text, phone text, lng text, lat text, mapurl text, weburl text);";

	    
	    private static class DatabaseHelper extends SQLiteOpenHelper{

			public DatabaseHelper(Context context){
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				// TODO Auto-generated method stub
				db.execSQL(DATABASE_CREATE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				// TODO Auto-generated method stub
				
			}
	    }
	    
	    public DataService open() throws SQLException{
	    	mDbHelper = new DatabaseHelper(mCtx);
	    	mDb = mDbHelper.getWritableDatabase();
	    	return this;
	    }
	    
	    public void close(){
	    	mDbHelper.close();
	    }
	    
	    
	    public DataService(Context context){
	    	mCtx = context;
	    }
	    
	  
	    
	    /**
	     * Return a Cursor over the list of all notes in the database
	     * e.g NoteTable has three column: id=KEY_ROWID, title=KEY_TITLE and body=KEY_BODY
	     * Below query is to get all note from this table
	     * @return Cursor over all notes
	     */
	    
	    public Cursor getAllFavorites(){
	    	return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_ADDRESS, KEY_ADDRESS_LINES, KEY_PHONENUMBER, KEY_LNG, KEY_LAT, KEY_MAPURL, KEY_WEBURL}, 
	    			null, null, null, null, null);
	    }
	    
	    //OK
	public ArrayList<FavoriteModel> getListFavorites(){
	    	
	    	Cursor mCursor = mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_ADDRESS, KEY_ADDRESS_LINES, KEY_PHONENUMBER, KEY_LNG, KEY_LAT, KEY_MAPURL, KEY_WEBURL}, 
	    			null, null, null, null, null);
	    	
	    	int rows = mCursor.getCount();
	    	ArrayList<FavoriteModel> mArrayList = new ArrayList<FavoriteModel>();
	    	
	    	if(rows>0){
	    		mCursor.moveToFirst();
		    	
		    	mArrayList.add(new FavoriteModel(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getString(5), mCursor.getString(6), mCursor.getString(7), mCursor.getString(8)));
		    	
		      for(mCursor.moveToFirst(); mCursor.moveToNext(); mCursor.isAfterLast()) {
		          // The Cursor is now set to the right position
		          mArrayList.add(new FavoriteModel(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2), mCursor.getString(3), mCursor.getString(4), mCursor.getString(5), mCursor.getString(6), mCursor.getString(7), mCursor.getString(8)));
		      }
	    	}
	    	
	      return mArrayList;
	    }
	    
	    
	    /**
	     * Create a new note using the title and body provided. If the note is
	     * successfully created return the new rowId for that note, otherwise return
	     * a -1 to indicate failure
	     */
	    public long insertFavorite(String title, String address, String addressLines, String phoneNumber, String lng, String lat, String mapUrl, String webUrl){
	    	ContentValues insertedValue = new ContentValues();
	    	insertedValue.put(KEY_TITLE, title);
	    	insertedValue.put(KEY_ADDRESS, address);
	    	insertedValue.put(KEY_ADDRESS_LINES, addressLines);
	    	insertedValue.put(KEY_PHONENUMBER, phoneNumber);
	    	insertedValue.put(KEY_LNG, lng);
	    	insertedValue.put(KEY_LAT, lat);
	    	insertedValue.put(KEY_MAPURL, mapUrl);
	    	insertedValue.put(KEY_WEBURL, webUrl);
	    	
	    	return mDb.insert(DATABASE_TABLE, null, insertedValue);
	    }
	    
	    
	    public void deleteDatabase() {
			mDbHelper.close();
			try {
				mCtx.deleteDatabase("FavoritesTbl");
			} catch (Exception e) {
			}
		}
	    
	    public boolean isExisted(String lat, String lng, String address) {
			Cursor c = mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_LNG, KEY_LAT, KEY_ADDRESS_LINES},
					KEY_LNG + " like " + lng + " and " + KEY_LAT + " like " + lat + " and " + KEY_ADDRESS_LINES + " like " + "'%" + address + "%'", null, null, null,
					null);
			if(c.moveToFirst() == false) {
				return false;
			}
			else{
				return true;
			}
		}


	    public Cursor getFavoriteById(long id) {

			return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
					KEY_ADDRESS, KEY_ADDRESS_LINES, KEY_PHONENUMBER, KEY_LNG, KEY_LAT,
					KEY_MAPURL, KEY_WEBURL}, KEY_ROWID + "=" + id, null, null, null, null);

		}
		
		/**
		 * Return a Cursor over the list of all row of the table which contain the key word
		 * 
		 * @return Cursor over all favorites which contain the key word
		 */
		public Cursor getFavoriteByAddress(String address) {
			return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
					KEY_ADDRESS, KEY_ADDRESS_LINES, KEY_PHONENUMBER, KEY_LNG, KEY_LAT,
					KEY_MAPURL, KEY_WEBURL},
					KEY_ADDRESS + " like " + "'%" + address + "%'", null, null, null,
					KEY_ADDRESS);

		}

	    
	    /**
	     * Delete the note with the given rowId
	     * 
	     * @param rowId id of note to delete
	     * @return true if deleted, false otherwise
	     */
		//ok
	    public boolean deleteFavorite(long rowId) {
	        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	    }
	    public boolean deleteFavoriteByTitle(String title) {
	        return mDb.delete(DATABASE_TABLE, KEY_TITLE + " like " + title, null) > 0;
	    }
	    public boolean deleteAll() {
	        return mDb.delete(DATABASE_TABLE, null, null) > 0;
	    }
}
