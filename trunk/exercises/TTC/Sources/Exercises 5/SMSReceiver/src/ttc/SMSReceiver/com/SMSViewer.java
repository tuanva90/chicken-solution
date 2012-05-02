package ttc.SMSReceiver.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



import ttc.SMSReceiver.com.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class SMSViewer extends Activity 
{
	private Button btnViewSMS;
	private ListView listSMS;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        btnViewSMS = (Button) findViewById(R.id.btnViewSMS);
        //tvDelSMS = (TextView) findViewById(R.id.tvdelete);
        SharedPreferences settings = getSharedPreferences(SmsReceiver.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
        
        btnViewSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {  
            	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            	SharedPreferences settings = v.getContext().getSharedPreferences(SmsReceiver.PREFS_NAME, 0);
            	Map <String,?> data = settings.getAll(); 
            	Iterator interator = data.keySet().iterator();
            	
            	while(interator.hasNext()) {
            		HashMap<String, String> map = new HashMap<String, String>();
            		String from =(String)interator.next();
            		String sms = (String)data.get(from);
            		map.put("txtFROM", from );
            		map.put("txtSMS", sms);
            		mylist.add(map);
            	}
            	
            	listSMS = (ListView) findViewById(R.id.listSMS);
            	int[] to = { R.id.txtFROM,R.id.txtSMS};
            	String[] from = {"txtFROM","txtSMS"};
            	
            	// Create a SimpleAdapter      	
        		SimpleAdapter simpleAdapter = new SimpleAdapter(v.getContext(), mylist,R.layout.row, from, to);
        		listSMS.setAdapter(simpleAdapter);

            }
        });        

    }
    
    
}