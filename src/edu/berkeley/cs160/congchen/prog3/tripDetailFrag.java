package edu.berkeley.cs160.congchen.prog3;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class tripDetailFrag extends Fragment {
	
	public static tripDetailFrag newInstance(String[] trip) {
		tripDetailFrag t = new tripDetailFrag();
		
		Log.d("trip value: ", trip[0] + " to " + trip[1]);
		
		Bundle args = new Bundle();
		args.putString("from", trip[0]);
		args.putString("to", trip[1]);
		t.setArguments(args);
		
		return t;		
	}
	
	public String getFromStation() {
		return getArguments().getString("from");
	}
	
	public String getToStation() {
		return getArguments().getString("to");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            return null;
        }
		
		View v = (LinearLayout) inflater.inflate(R.layout.trip_detail_frag_view, container, false);
		TextView txtTrip = (TextView) v.findViewById(R.id.trip_label);
		txtTrip.setText(getFromStation() + " to " + getToStation());
		
		return v;
	}
}
