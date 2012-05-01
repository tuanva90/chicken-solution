package com.ttc.mShopping.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
/*import android.util.FloatMath;*/
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.LayoutInflater.Factory;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;
import com.ttc.mShopping.models.ListResult;
import com.ttc.mShopping.models.SearchResult;
import com.ttc.mShopping.services.MapService;
import com.ttc.mShopping.utils.CommonConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ViewMapActivity extends MapActivity {
	MapService ms = new MapService();
	// Prepare variable for map
	private MapView map = null;
	private MyLocationOverlay me = null;
	private Button btnEn, btnVi, btnDirection;
	private SitesOverlay overlay = null;
	private SitesOverlay centerOverlay = null;
	private SitesOverlay detailOverlay = null;
	double curLAT, curLNG, fromLat, fromLng;

	// Prepare variable for request direction
	private SearchResult respectLocation;
	private SearchResult fromLocation;

	// Prepare variable for draw direction
	private Paint paint;
	private static int LINE_WIDTH = 2;
	private String directionVI = null;
	private String directionEN = null;
	private List<GeoPoint> listRoutePoint = null;

	// Prepare variable for draw list Location
	private ListResult listLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		try {
			// Prepare components
			map = (MapView) findViewById(R.id.map);
			btnDirection = (Button) findViewById(R.id.btnDirection);

			// Prepare map and overlays
			map.getController().setZoom(16);
			map.setBuiltInZoomControls(true);
			Drawable marker = getResources().getDrawable(R.drawable.marker);
			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight());
			overlay = new SitesOverlay(marker);

			Drawable centermarker = getResources().getDrawable(
					R.drawable.bullet);
			centermarker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight());
			centerOverlay = new SitesOverlay(centermarker);

			Drawable detailmarker = getResources().getDrawable(R.drawable.star);
			detailmarker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight());
			detailOverlay = new SitesOverlay(detailmarker);

			// Get data from intent
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			curLAT = CommonConfiguration.LATITUDE;
			curLNG = CommonConfiguration.LONGITUDE;
			respectLocation = (SearchResult) bundle
					.getSerializable(CommonConfiguration.RESPECT_LOCATION);
			fromLocation = (SearchResult) bundle
					.getSerializable(CommonConfiguration.FROM_LOCATION);
			listLocation = (ListResult) bundle
					.getSerializable(CommonConfiguration.SEARCH_RESULT_LIST);

			// Set the co-ordinate for from-point
			if (fromLocation == null) {
				fromLocation = new SearchResult();
				fromLocation.setLat(CommonConfiguration.LATITUDE);
				fromLocation.setLng(CommonConfiguration.LONGITUDE);
			}

			setButtonCondition();

			// Add remain overlays into map
			centerOverlay.addItem(new OverlayItem(getPoint(curLAT, curLNG),
					"My Location", "My Location\n" + curLAT + " - " + curLNG));
			centerOverlay.completeToPopulate();
			overlay.completeToPopulate();
			map.getOverlays().add(centerOverlay);
			me = new MyLocationOverlay(this, map);
			map.getOverlays().add(me);

			drawPlaceToMap(listLocation, curLAT, curLNG);

		} catch (Exception e) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ViewMapActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage(e.toString());
			alertDialog.setPositiveButton("Ok", null);
			alertDialog.show();
			Log.v("viemap", e.getMessage());
		}
	}

	private void setButtonCondition() {
		if (listLocation == null)
			btnDirection.setVisibility(4);
		else
			btnDirection.setVisibility(0);
		if (respectLocation != null) {
			// Draw position to map
			double detailLAT = respectLocation.getLat();
			double detailLNG = respectLocation.getLng();
			String des = respectLocation.getTitleNoFormatting() + "\n"
					+ respectLocation.getAddressLines().get(0) + "\n"
					+ detailLAT + " - " + detailLNG;
			detailOverlay.addItem(new OverlayItem(
					getPoint(detailLAT, detailLNG), respectLocation
							.getTitleNoFormatting(), des));
			detailOverlay.completeToPopulate();
			map.getOverlays().add(detailOverlay);

			// Get route direction
			fromLat = fromLocation.getLat();
			fromLng = fromLocation.getLng();
			ms.getRoute(respectLocation, fromLat, fromLng, "vi");
			listRoutePoint = ms.getListPathPoint();
			directionVI = ms.getDirection();
			String statusDirection = ms.getStatusDirection();
			if (!statusDirection.equals("OK"))
				Toast.makeText(getApplicationContext(), statusDirection,
						Toast.LENGTH_SHORT).show();
		}
		btnDirection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(CommonConfiguration.SEARCH_RESULT_LIST,
						listLocation);

				Intent intent = new Intent(ViewMapActivity.this,
						GetDirectionActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void drawPlaceToMap(ListResult listLocation, double lat, double lng) {
		try {
			List<MGeoPoint> list = getListPoint(listLocation);
			GeoPoint pt = getPoint(lat, lng);
			GeoPoint resPosition = null;
			if (respectLocation != null && listLocation == null) {
				resPosition = getPoint(respectLocation.getLat(),
						respectLocation.getLng());
				map.getController().animateTo(resPosition);
			} else
				map.getController().animateTo(pt);
			overlay.clear();

			if (list.size() > 0) {
				map.getController().setZoom(16);
				for (int i = 0; i < list.size(); i++) {
					MGeoPoint mpt = list.get(i);
					pt = getPoint(mpt.lat, mpt.lon);

					if (respectLocation != null) {
						double detailLAT = respectLocation.getLat();
						double detailLNG = respectLocation.getLng();
						if (detailLAT != mpt.lat || detailLNG != mpt.lon) {
							overlay.addItem(new OverlayItem(pt, "", mpt.desc
									+ ": " + mpt.lat + "," + mpt.lon));
						}
					} else
						overlay.addItem(new OverlayItem(pt, "", mpt.desc + ": "
								+ mpt.lat + "," + mpt.lon));
				}

				overlay.completeToPopulate();
				map.getOverlays().add(overlay);

			}
			map.invalidate();
		} catch (Exception e) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ViewMapActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage(e.toString());
			alertDialog.setPositiveButton("Ok", null);
			alertDialog.show();
		}
	}

	private List<MGeoPoint> getListPoint(ListResult listLocation) {
		try {
			ArrayList<MGeoPoint> lst = new ArrayList<MGeoPoint>();

			if (listLocation != null) {
				for (int i = 0; i < listLocation.getListSearchResult().size(); i++) {
					SearchResult location = listLocation.getListSearchResult()
							.get(i);
					double lat, lng;
					StringBuilder desc = new StringBuilder();

					lat = location.getLat();
					lng = location.getLng();
					desc.append(location.getTitleNoFormatting() + "\n"
							+ location.getAddressLines().get(0));

					lst.add(new MGeoPoint(lat, lng, desc.toString()));
				}
			}
			return lst;
		} catch (Exception e) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					ViewMapActivity.this);
			alertDialog.setTitle("Error");
			alertDialog.setMessage(e.toString());
			alertDialog.setPositiveButton("Ok", null);
			alertDialog.show();
		}
		return null;
	}

	/*
	 * private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b)
	 * { float pk = (float) (180 / 3.14169); float a1 = lat_a / pk; float a2 =
	 * lng_a / pk; float b1 = lat_b / pk; float b2 = lng_b / pk; float t1 =
	 * FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1)
	 * FloatMath.cos(b2); float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) *
	 * FloatMath.cos(b1) FloatMath.sin(b2); float t3 = FloatMath.sin(a1) *
	 * FloatMath.sin(b1); double tt = Math.acos(t1 + t2 + t3); return 6366000 *
	 * tt; }
	 */

	@Override
	public void onResume() {
		super.onResume();

		me.enableCompass();
	}

	@Override
	public void onPause() {
		super.onPause();

		me.disableCompass();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return (true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_S) {
			map.setSatellite(!map.isSatellite());
			return (true);
		} else if (keyCode == KeyEvent.KEYCODE_Z) {
			map.displayZoomControls(true);
			return (true);
		}

		return (super.onKeyDown(keyCode, event));
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	private class SitesOverlay extends ItemizedOverlay<OverlayItem> {
		private List<OverlayItem> items = new ArrayList<OverlayItem>();
		private Drawable marker = null;

		public SitesOverlay(Drawable marker) {
			super(marker);
			this.marker = marker;
		}

		@Override
		protected OverlayItem createItem(int i) {
			return (items.get(i));
		}

		public void addItem(OverlayItem item) {
			items.add(item);
		}

		public void clear() {
			items.clear();
		}

		public void completeToPopulate() {
			populate();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);

			int zoom = mapView.getZoomLevel();
			if (zoom <= 10) {
				LINE_WIDTH = 1;
			} else if (zoom < 15) {
				LINE_WIDTH = 2;
			} else {
				LINE_WIDTH = 3;
			}
			initPaint();

			try {
				boundCenterBottom(marker);
				if (listRoutePoint != null) {
					Paint paint = new Paint();
					paint.setColor(Color.RED);
					paint.setStyle(Style.STROKE);
					paint.setAntiAlias(true);
					paint.setStrokeWidth(4);
					PathEffect effect = new CornerPathEffect(4);
					paint.setPathEffect(effect);
					Projection projection = mapView.getProjection();
					canvas.drawPath(
							getPath(convertListGeoPointToPoint(listRoutePoint,
									projection)), paint);
				}
			} catch (Throwable e) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ViewMapActivity.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage(e.toString());
				alertDialog.setPositiveButton("Ok", null);
				alertDialog.show();
			}
		}

		public void initPaint() {
			if (paint == null)
				paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Style.STROKE);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(LINE_WIDTH);
			PathEffect effect = new CornerPathEffect(LINE_WIDTH);
			paint.setPathEffect(effect);
		}

		public List<Point> convertListGeoPointToPoint(
				List<GeoPoint> listGeoPoint, Projection project) {
			List<Point> listPoint = new ArrayList<Point>();
			if (listGeoPoint != null && listGeoPoint.size() > 0
					&& project != null) {
				listPoint.clear();
				for (GeoPoint geoPoint : listGeoPoint) {
					Point point = new Point();
					project.toPixels(geoPoint, point);
					listPoint.add(point);
				}
			}
			return listPoint;
		}

		public Path getPath(List<Point> list) {
			Path path = new Path();
			if (list != null && list.size() > 1) {
				path.reset();
				path.moveTo(list.get(0).x, list.get(0).y);
				int size = list.size();
				for (int i = 0; i < size; i++) {
					path.lineTo(list.get(i).x, list.get(i).y);
				}
				path.moveTo(list.get(list.size() - 1).x,
						list.get(list.size() - 1).y);
			}
			return path;
		}

		@Override
		protected boolean onTap(int i) {
			try {
				Toast.makeText(ViewMapActivity.this, items.get(i).getSnippet(),
						Toast.LENGTH_SHORT).show();
				return (true);
			} catch (Exception e) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						ViewMapActivity.this);
				alertDialog.setTitle("Error");
				alertDialog.setMessage(e.toString());
				alertDialog.setPositiveButton("Ok", null);
				alertDialog.show();
				return true;
			}
		}

		@Override
		public int size() {
			return (items.size());
		}
	}

	private static class MGeoPoint {
		private double lat;
		private double lon;
		private String desc = "";

		/*
		 * public MGeoPoint(double lat, double lon) { this.lat = lat; this.lon =
		 * lon; }
		 */

		public MGeoPoint(double lat, double lon, String desctiption) {
			this.lat = lat;
			this.lon = lon;
			desc = desctiption;
		}

		/*
		 * public double getLat() { return lat; }
		 * 
		 * public double getLon() { return lon; }
		 * 
		 * public void setLat(double lat) { this.lat = lat; }
		 * 
		 * public void setLon(double lon) { this.lon = lon; }
		 */

	}

	/* Invoked when the menu button is pressed */

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

}