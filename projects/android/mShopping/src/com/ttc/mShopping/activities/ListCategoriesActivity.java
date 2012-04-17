package com.ttc.mShopping.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.ttc.mShopping.adapters.GridViewAdapter;
import com.ttc.mShopping.utils.CommonConfiguration;

public class ListCategoriesActivity extends Activity {

	private GridViewAdapter mAdapter;
	private ArrayList<String> listLabel;
	private ArrayList<Integer> listImage;

	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);

		prepareList();

		// prepared arraylist and passed it to the Adapter class
		mAdapter = new GridViewAdapter(this, listLabel, listImage);

		// set custom adapter to gridview
		gridView = (GridView) findViewById(R.id.gridView1);
		gridView.setAdapter(mAdapter);
		gridView.setOnItemClickListener(gridView_listen);

	}

	private AdapterView.OnItemClickListener gridView_listen = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			// TODO Auto-generated method stub

		}

	};

	public void prepareList() {
		listLabel = new ArrayList<String>();
		listLabel.add(CommonConfiguration.gas_stations);
		listLabel.add(CommonConfiguration.taxis);
		listLabel.add(CommonConfiguration.hotels);
		listLabel.add(CommonConfiguration.banks);
		listLabel.add(CommonConfiguration.restaurants);
		listLabel.add(CommonConfiguration.atms);
		listLabel.add(CommonConfiguration.favorites);
		listLabel.add(CommonConfiguration.settings);

		listImage = new ArrayList<Integer>();
		listImage.add(R.drawable.icongasstation);
		listImage.add(R.drawable.icontaxi);
		listImage.add(R.drawable.iconhotel);
		listImage.add(R.drawable.iconbank);
		listImage.add(R.drawable.iconrestaurant);
		listImage.add(R.drawable.iconatm);
		listImage.add(R.drawable.iconfavorite);
		listImage.add(R.drawable.iconsetting);

	}
}
