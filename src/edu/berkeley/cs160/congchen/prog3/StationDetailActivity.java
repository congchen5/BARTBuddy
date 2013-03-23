package edu.berkeley.cs160.congchen.prog3;

import android.app.Activity;
import android.os.Bundle;

public class StationDetailActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_station_detail);
		singleStationListItem s = new singleStationListItem();
        s.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(android.R.id.content, s).commit();
	}
}
