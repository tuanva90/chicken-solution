package com.tma.ttc.androidK13.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tma.ttc.androidK13.models.SearchResult;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;

public class SearchSevice {

	/** List of SearchResults */
	private List<SearchResult> searchResultList;

	/** Url */
	private String url;	
	
	/** Query for url */
	private String query;	
	/** Latitude and Longitude */
	private double latitude;
	private double longitude;
	
	/** Constructor */
	public SearchSevice(double latitude, double longitude, String query) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.query = query;
		searchResultList = new ArrayList<SearchResult>();
	}
	
	/** Build URL with parameters */
	private String buildURL(double latitude, double longitude, String query, int start) {
		return CommonConfiguration.URL + "&rsz=8" + "&sll=" + String.valueOf(latitude) + 
				"," + String.valueOf(longitude) + "&q=" + query + "&start=" + start;		
	}
	
	/** Get InputStream and return JSON string */
	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";		
		}
	}
	
	/** Get JSON from URL */
	private String getJSONData(String url) {
		InputStream inputStream = null;
		String rsz = null;
		try {
			URL feedUrl = new URL(url);
			inputStream = feedUrl.openConnection().getInputStream();
			rsz = convertStreamToString(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsz;
	}
	
	/** Parse JSON */
	@SuppressWarnings("unchecked")
	public void runJSONParser() {
		try {						
			for (int iStart = 0; iStart < 8; iStart++) {
				this.url = buildURL(this.latitude, this.longitude, this.query, iStart * 8);				
				// Get JSON string
				String strJSON = getJSONData(this.url);
				// Create new JSON object from JSON string
				JSONObject objJSON = new JSONObject(strJSON);
				// Create optional JSON object with key "responseData"
				JSONObject responseData = objJSON.optJSONObject(CommonConfiguration.RESPONSE_DATA);
				
				// If there is no responseData, exit loop
				if (responseData == null) { 
					break;
				}
				
				// Create array of JSON objects with key "results" 
				// from responseData JSON object
				JSONArray arrayObjJSON = responseData.optJSONArray(CommonConfiguration.RESULTS);				
				
				// Define GsonBuilder and Gson			
				GsonBuilder gsonBuilder;
				Gson gson;
		        // Define a SearchResult instance
		        SearchResult rsz;
		        // Create SearchResult from JSONObject
		        // and add to SearchResultList	        
		        for (int i = 0; i < arrayObjJSON.length(); i++) {
		        	// Create GsonBuilder, Gson, SearchResult
		        	gsonBuilder = new GsonBuilder();
		        	gson = new Gson();
		        	gson = gsonBuilder.create();
		        	rsz = new SearchResult();
		        	// Parse JSON to SearchResult
		        	rsz = gson.fromJson(arrayObjJSON.getJSONObject(i).toString(), 
		        			SearchResult.class);
		        	// Set distance
		        	rsz.setDistance(this.latitude, this.longitude);		        	
		        	setSearchResultList(rsz);
		        }
			}			
			
			// Sort list by distance
			Collections.sort(this.searchResultList);
			
			// Remove duplicate objects
			ArrayList<SearchResult> tempList = new ArrayList<SearchResult>();
			boolean check;
			for (SearchResult rs : this.searchResultList) {
				check = true;
				for (SearchResult temp : tempList) {					
					if (rs.equals(temp)) {						
						check = false;
						break;
					}
				}
				if (check) {
					tempList.add(rs);
				}
			}		
			
			this.searchResultList.clear();
			this.searchResultList.addAll(tempList);
			
			// Set id for each SearchResult after sort
			int objectId = 0;
			for (SearchResult obj : this.searchResultList) {
				obj.setId(objectId);
				objectId++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(CommonConfiguration.TAG, "Service: " + e.toString());
		}
	}

	/** Setter for SearchResultList */
	public void setSearchResultList(SearchResult rsz) {
		this.searchResultList.add(rsz);
	}

	/** Getter for SearchResultList */
	public List<SearchResult> getSearchResultList() {
		return searchResultList;
	}
	
}
