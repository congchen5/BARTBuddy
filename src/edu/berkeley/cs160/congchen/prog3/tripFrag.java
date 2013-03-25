package edu.berkeley.cs160.congchen.prog3;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

public class tripFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
	
	private OnTripSelectedListener tListener;
	private Spinner spinner1;
	private Spinner spinner2;
	private Button btnSubmit;
	private View v;
	
	public static String timeString = "";
	public String travelToggle = "depart";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		timeString = "";
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
//		myL = new myOnClickListener(timeString);
//		btnSubmit.setOnClickListener(myL);
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (tripFrag.timeString.isEmpty()) {
            		Toast.makeText(getActivity(), "Please pick a time", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	
            	//Get information from click
            	Log.d("other info: ", tripFrag.timeString + " asdf " + travelToggle);
            	String srcStation = String.valueOf(spinner1.getSelectedItem());
            	String destStation = String.valueOf(spinner2.getSelectedItem());
            	
            	if (srcStation.equals(destStation)) {
            		Toast.makeText(getActivity(), "Please pick different stations", Toast.LENGTH_SHORT).show();
            		return;
            	}
				tListener.onTripSelected(srcStation, destStation, MainActivity.getStationAbbr(srcStation),MainActivity.getStationAbbr(destStation), travelToggle, tripFrag.timeString, getActivity());
            }
        });
		
		Switch s = (Switch) v.findViewById(R.id.toggleButton);
		s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) { //arriving
		            travelToggle = "arrive";
		        } else {
		        	travelToggle = "depart";
		        }
		    }
		});

		return v;
	}
	
	public interface OnTripSelectedListener {
		public void onTripSelected(String srcStation, String destStation, String from, String to, String travelToggle, String timeString, Activity a);
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
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
	
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    	Log.d("time values: ", hourOfDay + ":" + minute);
    	TextView timeVal = (TextView) getActivity().findViewById(R.id.time);
    	String zone = ""; //whether is AM or PM
    	String min = "" + minute;
    	if (minute < 10) {
    		min = "0" + min;
    	}
    	if (hourOfDay > 12) {
    		zone = "pm";
    		hourOfDay -= 12;
    	}
    	else {
    		zone = "am";
    	}
    	timeString = hourOfDay + ":" + minute + "+" + zone;
    	Log.d("timeString: ", getTimeString());
    	timeVal.setText(hourOfDay + ":" + min + " " + zone);
    }
    
    public String getTimeString() {
    	return timeString;
    }
    
    public void setTimeString(String t) {
    	this.timeString = t;
    }
}
