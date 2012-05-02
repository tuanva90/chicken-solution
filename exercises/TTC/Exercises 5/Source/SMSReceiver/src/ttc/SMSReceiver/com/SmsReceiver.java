package ttc.SMSReceiver.com;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver
{
	public static final String PREFS_NAME = "MySMS";
	@Override
	public void onReceive(Context context, Intent intent) 
	{
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();        
        SmsMessage[] msgs = null;
        String strSMS = "";            
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            String from = "";
            String sms = "";
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                strSMS += "SMS from " + msgs[i].getOriginatingAddress();
                from = msgs[i].getOriginatingAddress();
                strSMS += " :";
                strSMS += msgs[i].getMessageBody().toString();
                sms += msgs[i].getMessageBody().toString();
                strSMS += "\n"; 
                
            }
            //---display the new SMS message---
            //Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curentTime = sdf.format(new Date());            
            SharedPreferences.Editor editor = settings.edit();
            String keySMS = from + " [" + curentTime+"]";
            Toast.makeText(context, from + " : " +sms, Toast.LENGTH_SHORT).show();
            editor.putString(keySMS, sms);
            editor.commit();
        }                 		
	}
}