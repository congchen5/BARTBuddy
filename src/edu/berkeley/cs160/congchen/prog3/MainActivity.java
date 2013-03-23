package edu.berkeley.cs160.congchen.prog3;

import java.util.HashMap;

import edu.berkeley.cs160.congchen.prog3.departureFrag.OnStationSelectedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;

public class MainActivity extends Activity implements OnStationSelectedListener {
	public static Context appContext;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ActionBar gets initiated
		ActionBar actionbar = getActionBar();
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab PlayerTab = actionbar.newTab().setText("About");
		ActionBar.Tab MapTab = actionbar.newTab().setText("Map");
		ActionBar.Tab DepartureTab = actionbar.newTab().setText("Departures");

		// create the two fragments we want to use for display content
		Fragment aboutFragment = new aboutFrag();
		Fragment mapFragment = new mapFrag();
		Fragment departureFragment = new departureFrag();

		// set the Tab listener. Now we can listen for clicks.
		PlayerTab.setTabListener(new MyTabsListener(aboutFragment));
		MapTab.setTabListener(new MyTabsListener(mapFragment));
		DepartureTab.setTabListener(new MyTabsListener(departureFragment));
		
		// add the two tabs to the actionbar
		actionbar.addTab(PlayerTab);
		actionbar.addTab(MapTab);
		actionbar.addTab(DepartureTab);
	}
	
	@Override
	public void onStationSelected(String station, Activity a) {
		Intent launchingIntent = new Intent();
		launchingIntent.setClass(a, StationDetailActivity.class);
		launchingIntent.putExtra("station", station);
		startActivity(launchingIntent);
	}
	
	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			Toast.makeText(getApplicationContext(), "Reselected!", Toast.LENGTH_LONG).show();
		}
		
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
	}
}
