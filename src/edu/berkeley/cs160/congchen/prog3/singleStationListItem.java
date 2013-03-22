package edu.berkeley.cs160.congchen.prog3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class singleStationListItem extends Fragment {
	private static final String TAG = Fragment.class.getSimpleName();
	private String station_name;
	TextView mtext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		
		getActivity().setContentView(R.layout.single_station_item_view);

		TextView txtStation = (TextView) getActivity().findViewById(R.id.station_label);

		String station = b.getString("station");
		// displaying selected product name
		Log.d(TAG, "passed in station information: " + station);
		txtStation.setText(station);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = (LinearLayout)inflater.inflate(R.layout.single_station_item_view, container, false);
		return v;
	}

}
	

