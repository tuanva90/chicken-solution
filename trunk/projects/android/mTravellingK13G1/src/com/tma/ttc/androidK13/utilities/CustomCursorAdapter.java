/**
 * 
 */
package com.tma.ttc.androidK13.utilities;

import com.tma.ttc.androidK13.activities.R;
import com.tma.ttc.androidK13.services.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * @author LEHIEU
 * 
 */

public class CustomCursorAdapter extends SimpleCursorAdapter {

	private Cursor currentCursor;
	private final LayoutInflater mInflater;
	private int[] mFrom; // int array contain column-index of column in DB
	private int[] mTo;

	public CustomCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, DBHelper dbHelper) {
		super(context, layout, c, from, to);
		this.currentCursor = c;
		mInflater = LayoutInflater.from(context);
		mTo = to;
		findColumns(from);
	}

	/**
	 * Create a map from an array of strings to an array of column-id (column index) integers
	 * in mCursor. If mCursor is null, the array will be discarded.
	 * 
	 * @param from
	 *            the Strings naming the columns of interest
	 */
	private void findColumns(String[] from) {
		if (currentCursor != null) {
			int i;
			int count = from.length;
			if (mFrom == null || mFrom.length != count) {
				mFrom = new int[count];
			}
			for (i = 0; i < count; i++) {
				mFrom[i] = currentCursor.getColumnIndexOrThrow(from[i]);
			}
		} else {
			mFrom = null;
		}
	}

	// Makes a new view to hold the data pointed to by cursor.

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		final View view = mInflater.inflate(R.layout.row_my_favourite, parent,
				false);

		return view;
	}

	// Bind an existing view to the data pointed to by cursor

	@Override
	public void bindView(View v, Context context, Cursor c) {

		final int count = mTo.length;
		final int[] to = mTo;

		for (int i = 0; i < count; i++) {
			int nameCol = mFrom[i];
			String strValueCol = c.getString(nameCol).replace(";", " - ");
			TextView txtViewName = (TextView) v.findViewById(to[i]);
			if (txtViewName != null) {
				txtViewName.setText(strValueCol);
			}
		}

	}

}
