package com.ttc.mShopping.adapters;

import java.util.ArrayList;

import com.ttc.mShopping.activities.R;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter{

	private ArrayList<String> listLabel;
	private ArrayList<Integer> listImage;
	private Activity activity;
	
	public GridViewAdapter(Activity activity
			,ArrayList<String> listLabel, ArrayList<Integer> listImage){
		
		super();
		this.listLabel = listLabel;
		this.listImage = listImage;
		this.activity = activity;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listLabel.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return listLabel.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static class ViewHolder{
		public ImageView imgViewImage;
		public TextView txtViewTitle;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder view;
		LayoutInflater inflator = activity.getLayoutInflater();
		
		if(convertView == null){
			view = new ViewHolder();
			convertView = inflator.inflate(R.layout.gridview_row, null);
			
			view.txtViewTitle = (TextView) convertView.findViewById(R.id.grid_item_label);
			view.imgViewImage = (ImageView) convertView.findViewById(R.id.grid_item_image);
			
			convertView.setTag(view);
		}
		else{
			view = (ViewHolder) convertView.getTag();
		}
		
		view.txtViewTitle.setText(listLabel.get(position));
		view.imgViewImage.setImageResource(listImage.get(position));
		
		return convertView;
	}

}
