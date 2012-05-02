package com.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomRowContact extends LinearLayout {

	private int iconId;
	private TextView txtDisplayName, txtPhoneNumber;
	private ImageView imgPhoto;

	public CustomRowContact(Context context) {
		super(context);

		LayoutInflater li = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.list_row, this, true);

		txtDisplayName = (TextView) findViewById(R.id.txtContactRowName);
		txtPhoneNumber = (TextView) findViewById(R.id.txtContactRowPhone);
		imgPhoto = (ImageView) findViewById(R.id.imgContactRowPhoto);
		
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	public TextView getDisplayName() {
		return this.txtDisplayName;
	}

	public void setDisplayName(TextView txtDisplayName) {
		this.txtDisplayName = txtDisplayName;
	}

	public TextView getPhoneNumber() {
		return this.txtPhoneNumber;
	}

	public void setPhoneNumber(TextView txtPhoneNumber) {
		this.txtPhoneNumber = txtPhoneNumber;
	}
	
	public ImageView getImagePhoto(){
		return imgPhoto;
	}
	
	public void setImagePhoto(ImageView imgPhoto){
		this.imgPhoto = imgPhoto;
	}


}
