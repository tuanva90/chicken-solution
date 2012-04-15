package com.tma.ttc.androidk13.dataservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.provider.SyncStateContract.Constants;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tma.ttc.androidk13.models.PhoneNumber;
import com.tma.ttc.androidk13.models.PlaceModel;



public class SearchService {
	private String jString = "", url = "", query = "";
	private double myLat, myLng;
	private int STATUS_DONE = 0;	
	public static final List<PlaceModel> placeModel = new ArrayList<PlaceModel>();
	
	private JSONObject jData, responseData;
	private GsonBuilder gsonBuilder;
	private Gson gson;

	public SearchService(String query, double lat, double lng) {
		this.query = query;
		this.myLat = lat;
		this.myLng = lng;
		if (placeModel.size() > 0)
			placeModel.clear();
	}

	public void parseData(int start) {
		try {			
			url = makeURL(start);
			jString = getJSON(url);
			//Log.v(Constants.TAG, jString);
			
			gsonBuilder = new GsonBuilder();
			gson = gsonBuilder.create();

			jData = new JSONObject(jString);
			responseData = jData.optJSONObject("responseData");
			
			if(responseData != null)
				parseJSONToArray();
			
			STATUS_DONE = 1;
			//Log.v(Constants.TAG, String.valueOf(travellingDatas.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getState() {
		return STATUS_DONE;
	}
	
	private String makeURL(int start) {
		String url = "http://ajax.googleapis.com/ajax/services/search/local?q="
			+ query + "&rsz=filtered_cse&sll=" + String.valueOf(myLat) + ","
			+ String.valueOf(myLng) + "&v=1.0&start=" + start;
		return url;
	}
	
	// get json data from Google
	private String getJSON(String url) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(url));
			StatusLine line = response.getStatusLine();
			if (line.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return out.toString();				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			STATUS_DONE = 2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			STATUS_DONE = 2;
		}
		
		return "";
	}
	
	// Parse JSON to Array
	private void parseJSONToArray() {
		try {
			String results = responseData.optString("results");
			JSONArray array = new JSONArray(results);
			
			for (int i = 0; i < array.length(); i++) {
				JSONObject placeDataObj = array.getJSONObject(i);
				PlaceModel placeData = gson.fromJson(placeDataObj.toString(), PlaceModel.class);				
				
				placeData.setDistance(myLat, myLng);				
				placeData.setPlaceId(i);
				
				String address = placeDataObj.optString("addressLines");
				if (address != "") {
					List<String> addressLines = new ArrayList<String>();
					JSONArray addressJson = new JSONArray(address);
					for (int j = 0; j < addressJson.length(); j++) {
						String s = addressJson.optString(j);
						addressLines.add(s);
					}
					placeData.setAddressLines(addressLines);
				}

				String phones = placeDataObj.optString("phoneNumbers");
				if (phones != "") {
					List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
					JSONArray phoneJson = new JSONArray(phones);
					for (int j = 0; j < phoneJson.length(); j++) {
						JSONObject phoneObj = phoneJson.getJSONObject(j);
						PhoneNumber phone = gson.fromJson(phoneObj.toString(),
								PhoneNumber.class);
						phoneNumbers.add(phone);
					}
					placeData.setPhoneNumbers(phoneNumbers);
				}

				placeModel.add(placeData);
			}
			//Log.v(Constants.TAG, "parse ok");

		} catch (JSONException e) {
			e.printStackTrace();
			//Log.v(Constants.TAG_ERROR, e.toString());
			STATUS_DONE = 3;
		}
	}
}
