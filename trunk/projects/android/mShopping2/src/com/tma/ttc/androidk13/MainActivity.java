package com.tma.ttc.androidk13;


import com.tma.ttc.androidk13.R.id;
import com.tma.ttc.androidk13.activities.DetailPlaceActivity;
import com.tma.ttc.androidk13.activities.CategoriesActivity;
import com.tma.ttc.androidk13.activities.ViewOnMapActivity;
import com.tma.ttc.androidk13.activities.WebInfoActivity;
import com.tma.ttc.androidk13.utilities.LoadImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private final int SPLASH_LENGHT = 2000;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);             
        new Handler().postDelayed(new Runnable(){
        	 
            @Override

            public void run() {

                    /* Create an Intent that will start the Menu-Activity. */

                    Intent mainIntent = new Intent(MainActivity.this,CategoriesActivity.class);

                    MainActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    MainActivity.this.finish();

            }

        }, SPLASH_LENGHT);

    }


}
        
   