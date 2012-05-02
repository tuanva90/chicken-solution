package com.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

	ArrayList<Contact> listContacts;
	int resource;
	Context context;

	public CustomListAdapter(Context context, int textViewResourceId,
			ArrayList<Contact> objects) {

		this.context = context;
		resource = textViewResourceId;
		listContacts = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = new CustomRowContact(context);
		}

		final Contact item = listContacts.get(position);

		if (convertView != null) {
			TextView contactNameTextView = ((CustomRowContact) convertView)
					.getDisplayName();
			TextView contactPhoneTextView = ((CustomRowContact) convertView)
					.getPhoneNumber();
			ImageView imgPhoto = ((CustomRowContact) convertView)
					.getImagePhoto();

			contactNameTextView.setText(item.getStructuredName()
					.getDisplayName());
			if (!item.getPhone().isEmpty()) {
				contactPhoneTextView
						.setText(item.getPhone().get(0).getNumber());
			} else {
				contactPhoneTextView.setText("no phone number");
			}
			

		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listContacts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listContacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
