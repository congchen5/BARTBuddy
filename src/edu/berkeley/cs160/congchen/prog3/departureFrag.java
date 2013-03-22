package edu.berkeley.cs160.congchen.prog3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class departureFrag extends ListFragment {
	private String[] all_stations;
	private OnStationSelectedListener sListener;
	
	public interface OnStationSelectedListener {
		public void onStationSelected(String station);
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

//		super.onCreate(savedInstanceState);
//		setListAdapter(new ArrayAdapter(getActivity().getApplicationContext(),
//				android.R.layout.simple_list_item_activated_1, tutorialList));

		 // storing string resources into Array
		 all_stations = getResources().getStringArray(R.array.all_stations);
		
		 // Binding resources Array to ListAdapter
		 this.setListAdapter(new ArrayAdapter<String>(getActivity(),
		 R.layout.all_stations_list_item, all_stations));
	}
	
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// selected item
		String station = ((TextView) v).getText().toString();
		sListener.onStationSelected(station);
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
		View v = (LinearLayout)inflater.inflate(R.layout.departure, container, false);
        return v;
	}
}
