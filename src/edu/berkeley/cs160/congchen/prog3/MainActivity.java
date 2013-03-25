package edu.berkeley.cs160.congchen.prog3;

import java.util.HashMap;

import edu.berkeley.cs160.congchen.prog3.departureFrag.OnStationSelectedListener;
import edu.berkeley.cs160.congchen.prog3.tripFrag.OnTripSelectedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements OnStationSelectedListener, OnTripSelectedListener, LocationListener {
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
	
	static final HashMap<String, String> STATION_LOCATION_MAP = new HashMap<String, String>(){
		{
			put("12th", "37.803664,-122.271604");put("16th", "37.765062,-122.419694");put("19th", "37.80787,-122.269029");
			put("24th", "37.752254,-122.418466");put("ashb", "37.853024,-122.26978");put("balb", "37.72198087,-122.4474142");
			put("bayf", "37.697185,-122.126871");put("cast", "37.690754,-122.075567");put("civc", "37.779528,-122.413756");
			put("cols", "37.754006,-122.197273");put("colm", "37.684638,-122.466233");put("conc", "37.973737,-122.029095");
			put("daly", "37.70612055,-122.4690807");put("dbrk", "37.869867,-122.268045");put("dubl", "37.701695,-121.900367");
			put("deln", "37.925655,-122.317269");put("plza", "37.9030588,-122.2992715");put("embr", "37.792976,-122.396742");
			put("frmt", "37.557355,-121.9764");put("ftvl", "37.774963,-122.224274");put("glen", "37.732921,-122.434092");
			put("hayw", "37.670399,-122.087967");put("lafy", "37.893394,-122.123801");put("lake", "37.797484,-122.265609");
			put("mcar", "37.828415,-122.267227");put("mlbr", "37.599787,-122.38666");put("mont", "37.789256,-122.401407");
			put("nbrk", "37.87404,-122.283451");put("ncon", "38.003275,-122.024597");put("orin", "37.87836087,-122.1837911");
			put("pitt", "38.018914,-121.945154");put("phil", "37.928403,-122.056013");put("powl", "37.784991,-122.406857");
			put("rich", "37.936887,-122.353165");put("rock", "37.844601,-122.251793");put("sbrn", "37.637753,-122.416038");
			put("sfia", "37.6159,-122.392534");put("sanl", "37.72261921,-122.1613112");put("shay", "37.63479954,-122.0575506");
			put("ssan", "37.664174,-122.444116");put("ucty", "37.591208,-122.017867");put("wcrk", "37.905628,-122.067423");
			put("wdub", "37.699759,-121.928099");put("woak", "37.80467476,-122.2945822");
		}
	};
	
	private LocationManager manager;
	public double lat;
	public double lon;
	
	public static String getStationAbbr(String station_name) {
		return stationAbbr.get(station_name);
	}
	public static Context appContext;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//ImageView iv = (ImageView) findViewById(R.id.bart_station);
		//iv.setImageBitmap(decodeFile((File)getResources().getDrawable(R.drawable.bart_station_2)));
		//Bitmap originalImage= Bitmap.createScaledBitmap (BitmapFactory.decodeResource(getResources(), R.drawable.bart_station_2), 160, 160, true);
		//iv.setImageBitmap(originalImage);
		
//		BitmapFactory.Options options=new BitmapFactory.Options();
//		options.inSampleSize = 1;
//		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bart_station_2);
//		iv.setImageBitmap(bm);
		
		// ActionBar gets initiated
		ActionBar actionbar = getActionBar();
		// Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// initiating both tabs and set text to it.
		ActionBar.Tab PlayerTab = actionbar.newTab().setText("About");
		ActionBar.Tab MapTab = actionbar.newTab().setText("Map");
		ActionBar.Tab DepartureTab = actionbar.newTab().setText("Departures");
		ActionBar.Tab TripTab = actionbar.newTab().setText("Plan Trip");

		// create the two fragments we want to use for display content
		Fragment aboutFragment = new aboutFrag();
		Fragment mapFragment = new mapFrag();
		Fragment departureFragment = new departureFrag();
		Fragment tripFragment = new tripFrag();

		// set the Tab listener. Now we can listen for clicks.
		PlayerTab.setTabListener(new MyTabsListener(aboutFragment));
		MapTab.setTabListener(new MyTabsListener(mapFragment));
		DepartureTab.setTabListener(new MyTabsListener(departureFragment));
		TripTab.setTabListener(new MyTabsListener(tripFragment));
		
		// add the two tabs to the actionbar
		actionbar.addTab(PlayerTab);
		actionbar.addTab(MapTab);
		actionbar.addTab(TripTab);
		actionbar.addTab(DepartureTab);
		
		checkLocation(getWindow().getDecorView().findViewById(android.R.id.content));
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

	@Override
	public void onTripSelected(String srcStation, String destStation, String from, String to, String travelToggle, String timeString, Activity a) {
		Intent launchingIntent = new Intent();
		launchingIntent.setClass(a, TripDetailActivity.class);
		
		Log.d("values: ", from + " to " + to);
		launchingIntent.putExtra("srcStation", srcStation);
		launchingIntent.putExtra("destStation", destStation);
		launchingIntent.putExtra("from", from);
		launchingIntent.putExtra("to", to);
		launchingIntent.putExtra("travelToggle", travelToggle);
		launchingIntent.putExtra("timeString", timeString);
		startActivity(launchingIntent);	
	}
	
	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new tripFrag();
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void checkLocation(View v) {
		Log.d("checkLocation: ", " is called");
		//initialize location manager
		manager =  (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		//check if GPS is enabled
		//if not, notify user with a toast
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	    	Toast.makeText(this, "GPS is disabled.", Toast.LENGTH_SHORT).show();
	    } else {

	    	//get a location provider from location manager
	    	//empty criteria searches through all providers and returns the best one
	    	String providerName = manager.getBestProvider(new Criteria(), true);
	    	Location location = manager.getLastKnownLocation(providerName);
	    	Log.d("right before ", " updatedList");
//	    	TextView tv = (TextView)findViewById(R.id.locationResults);
	    	if (location != null) {
//		    	Toast.makeText(this, location.getLatitude() + " latitude, " + location.getLongitude() + " longitude", Toast.LENGTH_SHORT).show();
		    	Log.d("location info: ", location.getLatitude() + " latitude, " + location.getLongitude() + " longitude");
		    	lat = location.getLatitude();
		    	lon = location.getLongitude();
	    	} else {
//	    		Toast.makeText(this, "Last known location not found. Waiting for updated location...", Toast.LENGTH_SHORT).show();
	    	}
	    	//sign up to be notified of location updates every 15 seconds - for production code this should be at least a minute
	    	manager.requestLocationUpdates(providerName, 15000, 1, this);
	    }
	}

	@Override
	public void onLocationChanged(Location location) {
    	if (location != null) {
//	    	Toast.makeText(this, location.getLatitude() + " latitude, " + location.getLongitude() + " longitude", Toast.LENGTH_SHORT).show();
	    	Log.d("location info: ", location.getLatitude() + " latitude, " + location.getLongitude() + " longitude");
	    	lat = location.getLatitude();
	    	lon = location.getLongitude();
    	} else {
//    		Toast.makeText(this, "Last known location not found. Waiting for updated location...", Toast.LENGTH_SHORT).show();
    	}
	}

	@Override
	public void onProviderDisabled(String arg0) {}

	@Override
	public void onProviderEnabled(String arg0) {}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
	
	public double getLatitude() {
		return lat;
	}
	
	public double getLongitude() {
		return lon;
	}
}
