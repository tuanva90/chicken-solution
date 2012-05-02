package tma.caculator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CaculatorActivity extends Activity {
    /** Called when the activity is first created. */
	public float[]ar;
	public boolean bl = false;
	public boolean bl_new = true;
	public int i = 0, insart = 0, inend = 0;
	@Override    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EditText dtkq = (EditText)findViewById(R.id.editTextkq);
        Button bt0 = (Button)findViewById(R.id.Button0);
        Button bt1 = (Button)findViewById(R.id.Button1);
        Button bt2 = (Button)findViewById(R.id.Button2);
        Button bt3 = (Button)findViewById(R.id.Button3);
        Button bt4 = (Button)findViewById(R.id.Button4);
        Button bt5 = (Button)findViewById(R.id.Button5);
        Button bt6 = (Button)findViewById(R.id.Button6);
        Button bt7 = (Button)findViewById(R.id.Button7);
        Button bt8 = (Button)findViewById(R.id.Button8);
        Button bt9 = (Button)findViewById(R.id.Button9);
        Button btadd = (Button)findViewById(R.id.Buttonadd);
        Button btminus = (Button)findViewById(R.id.Buttonminus);
        Button btmulti = (Button)findViewById(R.id.Buttonmulti);
        Button btdiv = (Button)findViewById(R.id.buttondiv);
        Button btequal = (Button)findViewById(R.id.Buttonequal);
        bt0.setOnClickListener(onclick);
        bt1.setOnClickListener(onclick);
        bt2.setOnClickListener(onclick);
        bt3.setOnClickListener(onclick);
        bt4.setOnClickListener(onclick);
        bt5.setOnClickListener(onclick);
        bt6.setOnClickListener(onclick);
        bt7.setOnClickListener(onclick);
        bt8.setOnClickListener(onclick);
        bt9.setOnClickListener(onclick);
        btadd.setOnClickListener(onclickequals);
        btminus.setOnClickListener(onclickequals);
        btmulti.setOnClickListener(onclickequals);
        btdiv.setOnClickListener(onclickequals);
        btequal.setOnClickListener(onclickeq);
        Log.v("hj tag", "hj msg");
        //onclick.onClick(bt9);
    }
    private View.OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			inend ++;
			EditText dtkq = (EditText)findViewById(R.id.editTextkq);
			Button bt = (Button)v;
			if(bl_new) dtkq.setText("");
			dtkq.setText(dtkq.getText()+bt.getText().toString());
			bl = true;
			bl_new = false;
		}
	}; 
	 private View.OnClickListener onclickeq = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				EditText dtkq = (EditText)findViewById(R.id.editTextkq);
				
				
				String strkq = dtkq.getText().toString();
				dtkq.setText(String.valueOf(kq(strkq)));
				//dtkq.setText(kq(strkq));
				bl_new = true;
				bl = true;
			}
		}; 
private View.OnClickListener onclickequals = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Button bt = (Button)v;	
			EditText dtkq = (EditText)findViewById(R.id.editTextkq);
			String str = dtkq.getText().toString();
			if(!bl)
			{
				String newstr = str.substring(0, str.length()-1);
				dtkq.setText(newstr+bt.getText().toString());
			}
			else
				{
							
				if(str.length()==0)
				{
					if(bt.getText().toString().equals("-"))
						dtkq.setText(dtkq.getText()+bt.getText().toString());
				}
				else
					dtkq.setText(dtkq.getText()+bt.getText().toString());
				}
			bl = false;
		}
	}; 
	private Float kq(String str) //parsing a string to float
	{
		float flkq = 0;
		int leng = 0, lengfisrt = 0;
		for(int i = 0; i < str.length(); i++)
		{
			try
			{
				if(str.substring(i, i+1).equals("+")|| str.substring(i, i+1).equals("-") || str.substring(i, i+1).equals("*") || str.substring(i, i+1).equals("/"))
				{
					if(i>0)
						leng ++;
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplication(), e.getMessage(),Toast.LENGTH_LONG);
				return (float) 0;
			}
			
		}
		
		Toast.makeText(getApplication(), String.valueOf(leng), Toast.LENGTH_LONG);
		String []strAgr, strMath;
		strAgr = new String[leng+1];
		strMath = new String[leng];
		int dem = 0, j = 0;
		try
		{
			for(int i = 0; i < str.length(); i++)
			{
				if(str.substring(i, i+1).equals("+")|| str.substring(i, i+1).equals("-") || str.substring(i, i+1).equals("*") || str.substring(i, i+1).equals("/"))
				{
					if(i>0)
					{
						strAgr[dem] = str.substring(j, i);
						strMath[dem] = str.substring(i, i+1);
						j = i+1;
						dem++;
					}
				}
				if(i+1 >= str.length())
				{
					strAgr[dem] = str.substring(j, i+1);				
					j = i;
					lengfisrt = dem;
				}	
			}
			
			
			for(int i = 0; i < lengfisrt; i++)
			{
				
				if(strMath[i].equals("*") || strMath[i].equals("/"))
				{		
					lengfisrt --;
					if(strMath[i].equals("*"))
					{
						flkq = Float.parseFloat(strAgr[i]) * Float.parseFloat(strAgr[i+1]);
						strAgr[i] = String.valueOf(flkq);
						
					}
					else
					{
						flkq = Float.parseFloat(strAgr[i]) / Float.parseFloat(strAgr[i+1]);
						strAgr[i] = String.valueOf(flkq);
					}
					for(int ii = i; ii < lengfisrt; ii++)
					{
						strMath[ii] = strMath[ii+1];
						strAgr[ii+1] = strAgr[ii+2];
					}
				}
			}
			flkq = Float.parseFloat(strAgr[0]);
			for(int i = 0; i < lengfisrt; i++)
			{
				if(strMath[i].equals("+"))
					flkq += Float.parseFloat(strAgr[i+1]);
				else
					flkq -= Float.parseFloat(strAgr[i+1]);
			}
		}
		catch(Exception e)
		{
			Toast.makeText(getApplication(), e.getMessage(),Toast.LENGTH_LONG);
			return (float) 0;
		}
		
		return flkq;
	}
}