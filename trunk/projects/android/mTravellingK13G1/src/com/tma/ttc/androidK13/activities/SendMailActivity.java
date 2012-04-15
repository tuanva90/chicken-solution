package com.tma.ttc.androidK13.activities;

import java.util.List;

import com.tma.ttc.androidK13.models.PhoneNumbers;
import com.tma.ttc.androidK13.models.SearchResult;
import com.tma.ttc.androidK13.utilities.CommonConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendMailActivity extends Activity implements OnClickListener {
	Button btnSend;
	EditText txtAddress;
	EditText txtSubject;
	EditText txtEmailText;

	SearchResult location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.compose_mail);
		btnSend = (Button) findViewById(R.id.btnSendMail);
		txtAddress = (EditText) findViewById(R.id.etEmailAddress);
		txtSubject = (EditText) findViewById(R.id.etEmailSubject);
		txtEmailText = (EditText) findViewById(R.id.etEmailText);
		btnSend.setOnClickListener(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		location = (SearchResult) bundle
				.getSerializable(CommonConfiguration.SEARCH_RESULT);

		// Title
		String title = location.getTitleNoFormatting();

		// Address
		String address = "";
		List<String> addressLines = location.getAddressLines();
		if (addressLines != null) {
			for (int i = 0; i < addressLines.size(); i++) {
				address += location.getAddressLines().get(i) + " - ";
			}
			address = address.substring(0, address.length() - 2);
		}

		// Phone
		String phone = "";
		List<PhoneNumbers> listPhone = location.getPhoneNumbers();
		if (listPhone != null) {
			for (int i = 0; i < listPhone.size(); i++) {
				phone += location.getPhoneNumbers().get(i).getNumber() + " - ";
			}
			phone = phone.substring(0, phone.length() - 2).trim();
		}

		// URL
		String url = location.getUrl();

		StringBuilder template = new StringBuilder();
		template.append("Hi\n");
		template.append("I have just visited an interesting place. ");
		template.append("I would like to share with you. ");
		template.append("You should come here to enjoy someday. ");
		template.append("Here's the information:\n");
		template.append(title.toUpperCase() + "\n");
		template.append("\tAddress: " + address + "\n");
		if (!phone.equals("") && !phone.equals("null"))
			template.append("\tPhone: " + phone + "\n");
		template.append("\n");
		template.append("You could also go to "
				+ url
				+ " to take a look for more information and view some comments\n");
		template.append("Have fun!");

		txtEmailText.setText(template);

	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain"); // use this line for testing in the emulator
		// i.setType("message/rfc822") ; // use from live device
		i.putExtra(Intent.EXTRA_EMAIL, new String[] { txtAddress.getText()
				.toString() });
		i.putExtra(Intent.EXTRA_SUBJECT, txtSubject.getText().toString());
		i.putExtra(Intent.EXTRA_TEXT, txtEmailText.getText().toString());
		startActivity(Intent.createChooser(i, "Send mail..."));

		// Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
		// "mailto", txtAddress.getText().toString(), null));
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = new MenuInflater(getApplicationContext());
		inflater.inflate(R.menu.option, menu);
		setMenuBackground();
		return true;
	}

	protected void setMenuBackground() {
		getLayoutInflater().setFactory(new Factory() {

			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try { // Ask our inflater to create the view
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);
						new Handler().post(new Runnable() {
							public void run() {
								view.setBackgroundResource(R.drawable.menu_bg_480x157);
							}
						});
						return view;
					} catch (Exception e) {
					}
				}
				return null;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.change_location_menu) {
			Intent intent = new Intent(SendMailActivity.this,
					ChangeLocationActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.my_favorite_menu) {
			Intent intent = new Intent(SendMailActivity.this,
					MyFavouriteActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.home_menu) {
			Intent intent = new Intent(SendMailActivity.this,
					ListCategoriesActivity.class);
			startActivity(intent);
		} else if (item.getItemId() == R.id.quit_menu) {
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//			Process.killProcess(Process.myPid());
			Intent intent = new Intent(SendMailActivity.this,
					MainActivity.class);
			CommonConfiguration.IS_QUIT = true;
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
