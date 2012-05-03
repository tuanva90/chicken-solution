package com.ttc.mShopping.models;

import java.io.Serializable;
import java.util.List;

import com.ttc.mShopping.utils.CommonConfiguration;


@SuppressWarnings({ "serial", "rawtypes" })
public class SearchResult implements Serializable, Comparable {
	
	private String titleNoFormatting;
	private List<String> addressLines;
	private List<PhoneNumbers> phoneNumbers;
	private double lat;
	private double lng;
	private double distance;
	private String url;
	private int id;
	private String region;
	private String country;
	private String city;
	private String rating;
	
	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLat() {
		return lat;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLng() {
		return lng;
	}

	/** Return round distance */
	public double getDistance() {
		double d = Math.pow(10, 3) ;
		return Math.round( this.distance * d ) / d;
	}

	/** Compute distance from latitude and longitude */
	public void setDistance(double latitude, double longitude) {
		// Haversine formula
		double deltaLatitude = Math.toRadians(Math.abs(this.lat - latitude));
		double deltaLongitude = Math.toRadians(Math.abs(this.lng - longitude));
		double a = Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2) + 
				   Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(latitude)) * 
				   Math.sin(deltaLongitude / 2) * Math.sin(deltaLongitude / 2);
		double c =  2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		this.distance = CommonConfiguration.EARH_RADIUS * c;
	}
	public void setDistance(double distance)
	{
		this.distance = distance;
	}

	public void setUrl(String url) {
		this.url = url;	
	}

	public String getUrl() {
		return url;
	}

	public String getTitleNoFormatting() {
		return titleNoFormatting;
	}
	
	public void setTitleNoFormatting(String titleNoFormatting) {
		this.titleNoFormatting = titleNoFormatting;
	}
	
	public List<String> getAddressLines() {
		return addressLines;
	}
	
	public void setAddressLines(List<String> addressLines) {
		this.addressLines = addressLines;
	}
	
	public List<PhoneNumbers> getPhoneNumbers() {
		return phoneNumbers;
	}
	
	public void setPhoneNumbers(List<PhoneNumbers> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRegion() {
		return region;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	@Override
	public int compareTo(Object another) {
		SearchResult temp = (SearchResult) another;
		if (this.distance < temp.distance) {
			return -1;
		} else if (this.distance > temp.distance) {
			return 1;
		}
		return 0;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	@Override
	public boolean equals(Object o) {
		SearchResult sr = (SearchResult) o;
		
		if (this.titleNoFormatting.equals(sr.titleNoFormatting) &&
				this.lat == sr.lat && this.lng == sr.lng) {
			return true;
		} else {
			return false;
		}
	}
	
}
