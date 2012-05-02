package com.exercise.AndroidGoogleWeather;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidGoogleWeatherActivity extends Activity implements Runnable{

	class CurrentConditions {
		String condition;
		String temp_f;
		String temp_c;
		String humidity;
		String icon;
		String wind_condition;
	}

	class ForecastConditions {
		String day_of_week;
		String low;
		String high;
		String icon;
		String condition;
	}
	private ProgressDialog progressDialog;
	CurrentConditions currentConditions;
	List<ForecastConditions> forecastConditionsList;
	TextView tvCurrentConditions;
	/* Define ListView */
	private ListView listResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.exercise.AndroidGoogleWeather.R.layout.main);
		tvCurrentConditions = (TextView) findViewById(com.exercise.AndroidGoogleWeather.R.id.currentconditions);
		// Create and show ProgressDialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.show();

		// Create thread and start it
		Thread thread = new Thread(this);
		thread.start();
		// Display Result
		
	}

	private void displayListView() {
		listResult=(ListView) findViewById(com.exercise.AndroidGoogleWeather.R.id.listResult);
		int[] to = { com.exercise.AndroidGoogleWeather.R.id.imgIcon,
				com.exercise.AndroidGoogleWeather.R.id.txtTitle,
				com.exercise.AndroidGoogleWeather.R.id.txtInfo };
		String[] from = { "imgIcon", "title", "info" };
		// Create list of result
		ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		// Define a result
		HashMap<String, String> resultMap;
		for(int i =0;i<4;i++)
		{
			resultMap = new HashMap<String, String>();
			resultMap.put("imgIcon",String.valueOf(forecastConditionsList.get(i).icon));
			resultMap.put("title",String.valueOf(forecastConditionsList.get(i).day_of_week) );
			resultMap.put("info", "Condition : " + String.valueOf(forecastConditionsList.get(i).condition) +"\n Low :"+  String.valueOf(forecastConditionsList.get(i).low )+  " High : " + String.valueOf(forecastConditionsList.get(i).high));
			resultList.add(resultMap);
		}
		
		// Create a SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, resultList,
				com.exercise.AndroidGoogleWeather.R.layout.row_details, from, to);
		// Display list
		listResult.setAdapter(simpleAdapter);
	}

	private void parseGoogleWeather(Document srcDoc) {

		currentConditions = new CurrentConditions();

		// -- Get current_conditions
		NodeList current_conditions = srcDoc
				.getElementsByTagName("current_conditions");
		NodeList currentChilds = current_conditions.item(0).getChildNodes();

		for (int i = 0; i < currentChilds.getLength(); i++) {
			Node n = currentChilds.item(i);

			String nName = n.getNodeName();
			String nValue = n.getAttributes().getNamedItem("data")
					.getNodeValue().toString();
			if (nName.equalsIgnoreCase("condition")) {
				currentConditions.condition = nValue;
			} else if ((nName.equalsIgnoreCase("temp_f"))) {
				currentConditions.temp_f = nValue;
			} else if ((nName.equalsIgnoreCase("temp_c"))) {
				currentConditions.temp_c = nValue;
			} else if ((nName.equalsIgnoreCase("humidity"))) {
				currentConditions.humidity = nValue;
			} else if ((nName.equalsIgnoreCase("icon"))) {
				currentConditions.icon = nValue;
			} else if ((nName.equalsIgnoreCase("wind_condition"))) {
				currentConditions.wind_condition = nValue;
			}
		}

		// -- Get forecast_conditions
		NodeList forecast_conditions = srcDoc
				.getElementsByTagName("forecast_conditions");
		int forecast_conditions_length = forecast_conditions.getLength();

		forecastConditionsList = new ArrayList<ForecastConditions>();

		for (int j = 0; j < forecast_conditions_length; j++) {

			ForecastConditions tmpForecastConditions = new ForecastConditions();

			NodeList forecasrChilds = forecast_conditions.item(j)
					.getChildNodes();

			for (int i = 0; i < forecasrChilds.getLength(); i++) {

				Node n = forecasrChilds.item(i);

				String nName = n.getNodeName();
				String nValue = n.getAttributes().getNamedItem("data")
						.getNodeValue().toString();

				if (nName.equalsIgnoreCase("condition")) {
					tmpForecastConditions.condition = nValue;
				} else if ((nName.equalsIgnoreCase("day_of_week"))) {
					tmpForecastConditions.day_of_week = nValue;
				} else if ((nName.equalsIgnoreCase("low"))) {
					tmpForecastConditions.low = nValue;
				} else if ((nName.equalsIgnoreCase("high"))) {
					tmpForecastConditions.high = nValue;
				} else if ((nName.equalsIgnoreCase("icon"))) {
					tmpForecastConditions.icon = nValue;
				}

			}

			forecastConditionsList.add(tmpForecastConditions);

		}

	}

	private Document convertStringToDocument(String src) {
		Document dest = null;

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
			parser = dbFactory.newDocumentBuilder();
			dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			Toast.makeText(AndroidGoogleWeatherActivity.this, e1.toString(),
					Toast.LENGTH_LONG).show();
		} catch (SAXException e) {
			e.printStackTrace();
			Toast.makeText(AndroidGoogleWeatherActivity.this, e.toString(),
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(AndroidGoogleWeatherActivity.this, e.toString(),
					Toast.LENGTH_LONG).show();
		}

		return dest;
	}

	private String QueryGoogleWeather() {
		String qResult = "";
		String queryString = "http://www.google.com/ig/api?weather=hanoi";

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(queryString);

		try {
			HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

			if (httpEntity != null) {
				InputStream inputStream = httpEntity.getContent();
				Reader in = new InputStreamReader(inputStream);
				BufferedReader bufferedreader = new BufferedReader(in);
				StringBuilder stringBuilder = new StringBuilder();

				String stringReadLine = null;

				while ((stringReadLine = bufferedreader.readLine()) != null) {
					stringBuilder.append(stringReadLine + "\n");
				}

				qResult = stringBuilder.toString();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(AndroidGoogleWeatherActivity.this, e.toString(),
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(AndroidGoogleWeatherActivity.this, e.toString(),
					Toast.LENGTH_LONG).show();
		}

		return qResult;
	}
	/** Method of interface Runable */
	@Override
	public void run() {
		// get latitude, longitude and list of SearchResults
		String weatherString = QueryGoogleWeather();
		Document weatherDoc = convertStringToDocument(weatherString);
		parseGoogleWeather(weatherDoc);
		handler.sendEmptyMessage(0);
	}

	/** Handler for handling message from method run() */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			// dismiss dialog
			progressDialog.dismiss();
			// display text and ListView
			String s = "\n"

			+  "Condition : " + currentConditions.condition + "  High : "+  currentConditions.temp_f + "    Low : " + currentConditions.temp_c + "\n" +  currentConditions.humidity
			+ "\n"+ currentConditions.wind_condition + "\n";
			tvCurrentConditions.setText(s);
			displayListView();
		}
	};

}