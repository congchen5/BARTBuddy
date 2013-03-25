package edu.berkeley.cs160.congchen.prog3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class departureFrag extends ListFragment{
	private String[] all_stations;
	private OnStationSelectedListener sListener;
	private int mCurCheckPosition = 0;
	private ArrayAdapter<String> adp;
	private ArrayList<String> stations;
	
	public interface OnStationSelectedListener {
		public void onStationSelected(String station, Activity a);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            sListener = (OnStationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnStationSelectedListener");
        }
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// super.onCreate(savedInstanceState);
		// setListAdapter(new
		// ArrayAdapter(getActivity().getApplicationContext(),
		// android.R.layout.simple_list_item_activated_1, tutorialList));

//		LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
//		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		// storing string resources into Array
		
//		all_stations = getResources().getStringArray(R.array.all_stations);
		stations = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.all_stations)));
		// Binding resources Array to ListAdapter
		Collections.sort(stations, comp);
		adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, stations);
		this.setListAdapter(adp); // R.layout.all_stations_list_item
		
		if (savedInstanceState != null) {
			// Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
		}
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// selected item
		String station = ((TextView) v).getText().toString();
		sListener.onStationSelected(station, getActivity());
	}
	
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
		View v = (LinearLayout) inflater.inflate(R.layout.departure, container, false);
		//checkLocation(v);
        return v;
	}
	
	final private Comparator<String> comp = new Comparator<String>() {
	    public int compare(String e1, String e2) {
	    	String t1 = MainActivity.STATION_LOCATION_MAP.get(MainActivity.getStationAbbr(e1));
	    	String t2 = MainActivity.STATION_LOCATION_MAP.get(MainActivity.getStationAbbr(e2));
	    	
	    	String[] temp1 = t1.split(",");
	    	String[] temp2 = t2.split(",");
	    	
	    	double[] d1 = new double[2];
	    	double[] d2 = new double[2];
	    	
	    	d1[0] = Double.parseDouble(temp1[0]); //latitude
	    	d1[1] = Double.parseDouble(temp1[1]); //longitude
	    	
	    	d2[0] = Double.parseDouble(temp2[0]); //latitude
	    	d2[1] = Double.parseDouble(temp2[1]); //longitude	    	
	    	
	    	double lat = ((MainActivity) getActivity()).getLatitude();
	    	double lon = ((MainActivity) getActivity()).getLongitude();
	    	
	    	double a1 = Math.sqrt(Math.pow(d1[0] - lat,2) + Math.pow(d1[1] - lon, 2));
	    	double a2 = Math.sqrt(Math.pow(d2[0] - lat,2) + Math.pow(d2[1] - lon, 2));
	    	
	        if ((a2 - a1) < 0) {
	        	return 1;
	        }
	        else
	        	return -1;
	    }
	};
}
