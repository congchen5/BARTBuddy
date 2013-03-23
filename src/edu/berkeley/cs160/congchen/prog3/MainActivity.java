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
	public static HashMap<String, String> stationAbbr = new HashMap<String, String>();
	static {
		stationAbbr.put("12th St. Oakland City Center", "12th");
		stationAbbr.put("16th St. Mission", "16th");
		stationAbbr.put("19th St. Oakland", "19th");
		stationAbbr.put("24th St. Mission", "24th");
		stationAbbr.put("Ashby", "ashb");
		stationAbbr.put("Balboa Park", "balb");
		stationAbbr.put("Bay Fair", "bayf");
		stationAbbr.put("Castro Valley", "cast");
		stationAbbr.put("Civic Center", "civc");
		stationAbbr.put("Coliseum / Oakland Airport", "cols");
		stationAbbr.put("Colma", "colm");
		stationAbbr.put("Concord", "conc");
		stationAbbr.put("Daly City", "daly");
		stationAbbr.put("Downtown Berkeley", "dbrk");
		stationAbbr.put("Dublin / Pleasanton", "dubl");
		stationAbbr.put("El Cerrito del Norte", "deln");
		stationAbbr.put("El Cerrito Plaza", "plza");
		stationAbbr.put("Embarcadero", "embr");
		stationAbbr.put("Fremont", "frmt");
		stationAbbr.put("Fruitvale", "ftvl");
		stationAbbr.put("Glen Park", "glen");
		stationAbbr.put("Hayward", "hayw");
		stationAbbr.put("Lafayette", "lafy");
		stationAbbr.put("Lake Merritt", "lake");
		stationAbbr.put("MacArthur", "mcar");
		stationAbbr.put("Millbrae", "mlbr");
		stationAbbr.put("Montgomery", "mont");
		stationAbbr.put("North Berkeley", "nbrk");
		stationAbbr.put("North Concord / Martinez", "ncon");
		stationAbbr.put("Orinda", "orin");
		stationAbbr.put("Pittsburg / Bay Point", "pitt");
		stationAbbr.put("Pleasant Hill", "phil");
		stationAbbr.put("Powell", "powl");
		stationAbbr.put("Richmond", "rich");
		stationAbbr.put("Rockridge", "rock");
		stationAbbr.put("San Bruno", "sbrn");
		stationAbbr.put("San Francisco Intl Airport", "sfia");
		stationAbbr.put("San Leandro", "sanl");
		stationAbbr.put("South Hayward", "shay");
		stationAbbr.put("South San Francisco", "ssan");
		stationAbbr.put("Union City", "ucty");
		stationAbbr.put("Walnut Creek", "wcrk");
		stationAbbr.put("West Oakland", "woak");
	}
	
	public static String getStationAbbr(String station_name) {
		return stationAbbr.get(station_name);
	}
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
			Toast.makeText(getApplicationContext(), "Reselected!", Toast.LENGTH_SHORT).show();
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
