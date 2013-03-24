package edu.berkeley.cs160.congchen.prog3;

import edu.berkeley.cs160.congchen.prog3.departureFrag.OnStationSelectedListener;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class tripFrag extends Fragment {
	
	private OnTripSelectedListener tListener;
	private Spinner spinner1;
	private Spinner spinner2;
	private Button btnSubmit;
	private View v;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		v = inflater.inflate(R.layout.trip, container, false);

		spinner1 = (Spinner) v.findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				getActivity(), R.array.all_stations,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner1.setAdapter(adapter1);

		spinner2 = (Spinner) v.findViewById(R.id.spinner2);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				getActivity(), R.array.all_stations,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner2.setAdapter(adapter2);
		
		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				Toast.makeText(getActivity(),
						"OnClickListener : " + "\nSpinner 1 : "
								+ String.valueOf(spinner1.getSelectedItem())
								+ "\nSpinner 2 : "
								+ String.valueOf(spinner2.getSelectedItem()),
						Toast.LENGTH_SHORT).show();
				
				//Get information from click
				tListener.onTripSelected(MainActivity.getStationAbbr(String.valueOf(spinner1.getSelectedItem())),MainActivity.getStationAbbr(String.valueOf(spinner2.getSelectedItem())), getActivity());
            }
        });

		return v;
	}
	
	public interface OnTripSelectedListener {
		public void onTripSelected(String from, String to, Activity a);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            tListener = (OnTripSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnTripSelectedListener");
        }
	}
}
