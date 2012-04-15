package com.tma.ttc.androidK13.utilities;

import com.tma.ttc.androidK13.activities.R;

/**
 * @author LEHIEU
 * 
 */
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
		
	public static final String GAS_STATION_FIXED = "gas station";
	
	public static final String GAS_STATION = "gas+station";	
	public static final String TAXI = "taxi";
	public static final String ATM = "atm";
	public static final String HOTEL = "hotel";
	public static final String RESTAURANT = "restaurant";
	public static final String BANK = "bank";
	public static final String AIRPORT = "airport";
	public static final String PLACE = "place";
	
	public static final int IGAS_STATION = 1;
	public static final int ITAXI = 2;
	public static final int IATM = 3;
	public static final int IHOTEL = 4;
	public static final int IRESTAURANT = 5;
	public static final int IBANK = 6;
	public static final int IAIRPORT = 7;
	public static final int IPLACE = 8;

	/* */
	public static final String VIEW_NAME = "viewName";
	public static final String VIEW_ADDRESS = "viewAddress";
	public static final String VIEW_DISTANCE = "ViewDistance";
	
	public static final String VIEW_ICON = "viewIcon";
	public static final String VIEW_TITLE = "viewTitle";
	public static final String VIEW_INFO = "ViewInfo";
	
	public static final int IMG_ADRESS = R.drawable.ic_address_50x50;
	public static final int IMG_DISTANCE = R.drawable.ic_distance_50x50;
	public static final int IMG_FAX = R.drawable.ic_fax_50x50;
	public static final int IMG_PHONE = R.drawable.ic_phone_50x50;
	public static final int IMG_WEB = R.drawable.ic_web_50x50;
	public static final int IMG_RATING = R.drawable.ic_rating_50x50;
	
	public static boolean IS_CHANGE = false;
	public static boolean IS_QUIT = false;
}
