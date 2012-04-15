package com.tma.ttc.androidK13.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PhoneNumbers implements Serializable{

	private String type;
	private String number;
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}
	
}
