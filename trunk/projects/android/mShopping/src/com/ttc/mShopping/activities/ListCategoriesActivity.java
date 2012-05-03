package com.ttc.mShopping.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ttc.mShopping.adapters.GridViewAdapter;
import com.ttc.mShopping.utils.CommonConfiguration;


public class ListCategoriesActivity extends TemplateActivity {

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
			Bundle bundle = new Bundle();
			TextView txtv = (TextView)v.findViewById(R.id.grid_item_label);
			ImageView igv = (ImageView)v.findViewById(R.id.grid_item_image);
			
			if(txtv.getText().equals(CommonConfiguration.favorites))
			{
				
			}
			else
			{
				if(txtv.getText().equals(CommonConfiguration.settings))
				{
					
				}
			
				else
				{
					if(txtv.getText().equals(CommonConfiguration.babystore))
					{
						bundle.putString(CommonConfiguration.QUERY,CommonConfiguration.BABY_STORE);
						bundle.putInt(CommonConfiguration.IQUERY, CommonConfiguration.Ibabystore);
					}
					else
					{
						if(txtv.getText().equals(CommonConfiguration.bookstore))
						{
							bundle.putString(CommonConfiguration.QUERY,CommonConfiguration.BOOK_STORE);
							bundle.putInt(CommonConfiguration.IQUERY, CommonConfiguration.Ibookstore);
						}
						else
						{
							if(txtv.getText().equals(CommonConfiguration.clothingstore))
							{
								bundle.putString(CommonConfiguration.QUERY,CommonConfiguration.CLOTHING_STORE);
								bundle.putInt(CommonConfiguration.IQUERY, CommonConfiguration.Icothingstore);
							}
							else
							{
								if(txtv.getText().equals(CommonConfiguration.mallshopping))
								{
									bundle.putString(CommonConfiguration.QUERY,CommonConfiguration.MALL_SHOPPING);
									bundle.putInt(CommonConfiguration.IQUERY, CommonConfiguration.Imallshopping);
								}						
							}
						}
					}					
					Intent intent = new Intent(ListCategoriesActivity.this, SearchResultActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				
			}
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
	
	//Create menu	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==R.id.mnOptionHome)
		{
			Intent intent = new Intent(ListCategoriesActivity.this, ListCategoriesActivity.class);
			startActivity(intent);			
		}
		if(item.getItemId()==R.id.mnOptionFavourite)
		{
			Intent intent = new Intent(ListCategoriesActivity.this, MyFavouriteActivity.class);
			startActivity(intent);			
		}
		if(item.getItemId()==R.id.mnOptionBack)
		{
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
