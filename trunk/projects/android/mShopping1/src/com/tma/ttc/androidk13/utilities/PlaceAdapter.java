package com.tma.ttc.androidk13.utilities;

import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.tma.ttc.androidk13.R;
import com.tma.ttc.androidk13.models.PlaceModel;

public class PlaceAdapter extends ArrayAdapter<PlaceModel> {

	Context context;
	int resourceId;
	List<PlaceModel> listResult;
	
	public PlaceAdapter(Context context, int textViewResourceId, List<PlaceModel> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resourceId = textViewResourceId;
		this.listResult = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view == null) {
			LayoutInflater li = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
			view = li.inflate(resourceId, null);
		}
		
		PlaceModel place = listResult.get(position);
		if(place != null) {
			
			TextView name = (TextView) view.findViewById(R.id.rs_name);
			TextView dis = (TextView) view.findViewById(R.id.rs_distance);	
			TextView add = (TextView) view.findViewById(R.id.rs_address);
			//ImageView img = (ImageView)view.findViewById(R.id.rs_img);
			
			//Bitmap mBitmap = LoadImage.downloadImage(place.getStaticMapUrl());
			//if (mBitmap!=null)
			//img.setImageBitmap(mBitmap);
			name.setText(place.getTitle());
			dis.setText(String.valueOf(place.getDistance()) + "km");
			add.setText(place.getAddressLines().get(0).toString());
		}
		
		return view;
	}
}