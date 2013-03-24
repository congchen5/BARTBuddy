package edu.berkeley.cs160.congchen.prog3;

import android.os.Bundle;
import android.app.Activity;

public class TripDetailActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tripDetailFrag t = new tripDetailFrag();
        t.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(android.R.id.content, t).commit();
	}


}

