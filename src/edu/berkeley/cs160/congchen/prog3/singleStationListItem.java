package edu.berkeley.cs160.congchen.prog3;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class singleStationListItem extends Fragment {
//	static final String TAG = Fragment.class.getSimpleName();
//	String station_name;
//	TextView mtext;
	
	public static singleStationListItem newInstance(String station) {
		singleStationListItem s = new singleStationListItem();
		
		Bundle args = new Bundle();
		args.putString("station", station);
		s.setArguments(args);
		
		return s;		
	}
	
	public String getShownStation() {
		return getArguments().getString("station");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		
		View v = (LinearLayout) inflater.inflate(R.layout.single_station_item_view, container, false);
		TextView txtStation = (TextView) v.findViewById(R.id.station_label);
		txtStation.setText(getShownStation());
		return v;
	}

}
	

