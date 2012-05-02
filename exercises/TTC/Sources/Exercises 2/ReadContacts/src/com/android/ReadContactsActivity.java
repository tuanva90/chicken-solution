package com.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReadContactsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn= (Button) findViewById(R.id.button1);
        btn.setOnClickListener(btn_listen);
    }
    
    private  View.OnClickListener btn_listen = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ReadContactsActivity.this, ContactsListActivity.class);
			ReadContactsActivity.this.startActivity(intent);
		}
	};
}