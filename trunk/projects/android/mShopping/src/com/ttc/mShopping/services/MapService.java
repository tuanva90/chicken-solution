package com.ttc.mShopping.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.android.maps.GeoPoint;
import com.ttc.mShopping.models.SearchResult;


public class MapService {
	private List<GeoPoint> listPathPoint = null;
	private String direction;
	private String statusDirection;
	
	public String getRoute(SearchResult respectLocation, double curLat,
			double curLng, String lang) {
		String responseBody="";
		try {
			String prefix = "http://routes.cloudmade.com/8ee2a50541944fb9bcedded5165f09d9/api/0.3/";
			String suffix = "/car.js?lang=";
			String respectPosition = respectLocation.getLat() + ","
					+ respectLocation.getLng();
			String currentPosistion = curLat + "," + curLng + ",";

			String url = prefix + currentPosistion + respectPosition + suffix + lang;

			HttpClient client = new DefaultHttpClient();
			HttpGet getMethod = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			responseBody = client.execute(getMethod, responseHandler);
			parseJson(responseBody);
		} catch (Exception e) {
			e.toString();
		}
		return responseBody;
	}
	
	public String getDirection(JSONArray routeJS) {
		String direction = "";
		try {
			StringBuilder directionBuilder = new StringBuilder();
			for (int i = 0; i < routeJS.length(); i++) {
				JSONArray directionApart = routeJS.getJSONArray(i);
				directionBuilder.append("_" + directionApart.getString(0) + " "
						+ directionApart.getString(4) + "\n");
			}
			direction = directionBuilder.toString();
		} catch (Exception e) {
			e.toString();
		}
		return direction;
	}
	
	public List<GeoPoint> getListRoutePoint(JSONArray routeArray) {
		List<GeoPoint> listRoutePoint = new ArrayList<GeoPoint>();
		try {
			for (int i = 0; i < routeArray.length(); i++) {
				JSONArray pointJS = routeArray.getJSONArray(i);
				double lat = pointJS.getDouble(0);
				double lng = pointJS.getDouble(1);
				GeoPoint gp = new GeoPoint((int) (lat * 1000000.0),
						(int) (lng * 1000000.0));
				listRoutePoint.add(gp);
			}
		} catch (Exception e) {
			e.toString();
		}
		return listRoutePoint;
	}
	
	public void parseJson(String dataJson) {
		try {
			JSONObject jsonObject = new JSONObject(dataJson);
			String status = jsonObject.getString("status"); // status
			if (status.equals("0")) {
				statusDirection = "OK";
				// Route summary
				JSONObject route_summary = jsonObject
						.getJSONObject("route_summary");
				String total_distance = route_summary
						.getString("total_distance");
				String start_point = route_summary.getString("start_point");
				String end_point = route_summary.getString("end_point");

				// List route point
				JSONArray route_geometry = jsonObject
						.getJSONArray("route_geometry");
				listPathPoint = getListRoutePoint(route_geometry);

				// Route instruction
				JSONArray route_instructions = jsonObject
						.getJSONArray("route_instructions");
				direction = "TOTAL DISTANCE: " + total_distance + "m\n"
						+ "START POINT: " + start_point + "\n" + "END POINT: "
						+ end_point + "\n\n" + getDirection(route_instructions);

			} else {
				statusDirection = "Can not find the route direction!";
			}
		} catch (Exception e) {
			e.toString();
		}
	}

	public List<GeoPoint> getListPathPoint() {
		return listPathPoint;
	}

	public String getDirection() {
		return direction;
	}
	
	public String getStatusDirection() {
		return statusDirection;
	}

}
