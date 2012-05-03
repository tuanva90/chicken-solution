package com.ttc.mShopping.utils;

public class CommonConfiguration {
		
	public static final String RADIUS = "5";


	/* Constant for Log */
	public static final String TAG = "MY INFO";
	
	/* Constant for processing data from google search */	
	public static final String URL = "http://ajax.googleapis.com/ajax/services/search/local?" +
			"v=1.0";
	public static final String RESPONSE_DATA = "responseData";
	public static final String RESULTS = "results";
	public static final String QUERY = "query";
	public static final String IQUERY = "iquery";
	public static final String CURRENT_LATITUDE= "currentLatitude";
	public static final String CURRENT_LONGITUDE= "currentLongitute";
	
	public static final double EARH_RADIUS = 6371;
	public static double LATITUDE = 10.7783;
	public static double LONGITUDE = 106.6962;	
	
	public static final String SEARCH_RESULT = "searchResult";
	public static final String RESPECT_LOCATION = "respectLocation";
	public static final String FROM_LOCATION = "fromLocation";
	public static final String SEARCH_RESULT_LIST = "searchResultList";
	public static final String SEARCH_RESULT_ID = "searchResultId";
	public static final String URL_TO_WEB = "urlToWeb";
		
		
	public static final String babystore = "Baby Stores";
	public static final String bookstore = "Book Stores";
	public static final String clothingstore = "Clothing Stores";
	public static final String mallshopping = "Malls";
	public static final String shoppingcenter = "Shopping Centers";
	public static final String favorites = "Favorites";
	public static final String settings = "Settings";
	public static final String BOOK_STORE = "book+store";
	public static final String BABY_STORE = "baby+store";
	public static final String CLOTHING_STORE = "clothing+store";
	public static final String MALL_SHOPPING = "malls";
	public static final String SHOPPING_CENTER = "shopping+center";
	
	public static final int Ibabystore = 1;
	public static final int Ibookstore = 2;
	public static final int Iatm = 3;
	public static final int Icothingstore = 4;
	public static final int Imallshopping = 6;
	public static final int Ishoppingcenter = 5;

	/* */
	public static final String VIEW_NAME = "viewName";
	public static final String VIEW_ADDRESS = "viewAddress";
	public static final String VIEW_DISTANCE = "ViewDistance";
	
	public static final String VIEW_ICON = "viewIcon";
	public static final String VIEW_TITLE = "viewTitle";
	public static final String VIEW_INFO = "ViewInfo";
	
	public static final int IMG_ADRESS = com.ttc.mShopping.activities.R.drawable.ic_address_50x50;
	public static final int IMG_DISTANCE = com.ttc.mShopping.activities.R.drawable.ic_distance_50x50;
	public static final int IMG_PHONE = com.ttc.mShopping.activities.R.drawable.ic_phone_50x50;
	public static final int IMG_WEB = com.ttc.mShopping.activities.R.drawable.ic_web_50x50;
	public static final int IMG_RATING = com.ttc.mShopping.activities.R.drawable.ic_rating_50x50;

	
		
	public static boolean IS_CHANGE = false;
	public static boolean IS_QUIT = false;
}
