package com.android;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Contact {
	private String id;
 	private String displayName;
 	private ArrayList<Phone> phone = new ArrayList<Phone>();
 	private MyStructuredName structuredName;
 	private Bitmap photo;
  	
 	public Contact() {
		// TODO Auto-generated constructor stub
 		super();
	}
 	
 	public Contact(String displayName, String phoneNumber) {
		// TODO Auto-generated constructor stub
 		this.structuredName = new MyStructuredName(displayName, null, null, null, null, null, null, null, null);
 		this.phone = new ArrayList<Phone>();
 		this.phone.add(new Phone(phoneNumber, "sizefile"));
	}
 	
 	public String getId() {
 		return id;
 	}
 	public void setId(String id) {
  		this.id = id;
 	}
 	public String getDisplayName() {
 		return displayName;
 	}
 	public void setDisplayName(String dName) {
 		this.displayName = dName;
 	}
 	public MyStructuredName getStructuredName(){
 		return this.structuredName;
 	}
 	public void setStructuredName(MyStructuredName structuredName){
 		this.structuredName = structuredName;
 	}
 	public ArrayList<Phone> getPhone() {
 		return phone;
 	}
 	public void setPhone(ArrayList<Phone> phone) {
 		this.phone = phone;
 	}
 	public void addPhone(Phone phone) {
 		this.phone.add(phone);
 	}
	public void setPhoto(Bitmap bmPhoto){
		this.photo = bmPhoto;
	}
	public Bitmap getPhoto(){
		return this.photo;
	}
}
