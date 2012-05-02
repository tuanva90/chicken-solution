package com.android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class ContactsListActivity extends Activity implements OnClickListener {

	ListView lstContacts;

	ContactList myContactList;
	CustomListAdapter adapter;
	ContactsManager myContactsManager;

	ProgressDialog progressDialog;
	Thread threadGetContacts;

	final Handler handler = new Handler();
	final Runnable mUpdateResultGetContacts = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			adapter = new CustomListAdapter(ContactsListActivity.this,
					R.id.lstContacts, myContactList.getContacts());
			lstContacts.setAdapter(adapter);
			progressDialog.dismiss();

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_list);

	
		lstContacts = (ListView) findViewById(R.id.lstContacts);


		threadGetContacts = new Thread() {
			public void run() {

				myContactsManager = new ContactsManager();
				myContactsManager.setCr(ContactsListActivity.this
						.getContentResolver());
				myContactList = myContactsManager.getContactList();
				handler.post(mUpdateResultGetContacts);
			};
		};
		threadGetContacts.start();

		// Create and show ProgressDialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Getting contacts....");
		progressDialog.show();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	

}
