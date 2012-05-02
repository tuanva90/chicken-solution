package com.ttc.mShopping.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import com.ttc.mShopping.adapters.GridViewAdapter;
import com.ttc.mShopping.utils.CommonConfiguration;


public class ListCategoriesActivity extends Activity {

	private GridViewAdapter mAdapter;
	private ArrayList<String> listLabel;
	private ArrayList<Integer> listImage;
	private Button atm;
	

	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories);
		atm =(Button) findViewById(R.id.btntestbabystore);
		atm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString(CommonConfiguration.QUERY,CommonConfiguration.BOOK_STORE);
				bundle.putInt(CommonConfiguration.IQUERY, CommonConfiguration.Ibookstore);
				Intent intent = new Intent(ListCategoriesActivity.this, SearchResultActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				
				
			}
		});
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
		listLabel.add(CommonConfiguration.babystore);
		listLabel.add(CommonConfiguration.bookstore);
		listLabel.add(CommonConfiguration.clothingstore);
		listLabel.add(CommonConfiguration.mallshopping);
		//listLabel.add(CommonConfiguration.atms);
		listLabel.add(CommonConfiguration.favorites);
		listLabel.add(CommonConfiguration.settings);

		listImage = new ArrayList<Integer>();
		listImage.add(R.drawable.iconbaby);
		listImage.add(R.drawable.iconbook);
		listImage.add(R.drawable.iconclothing);
		listImage.add(R.drawable.iconmallshopping);
		//listImage.add(R.drawable.iconatm);
		listImage.add(R.drawable.iconfavorite);
		listImage.add(R.drawable.iconsetting);

	}
}
