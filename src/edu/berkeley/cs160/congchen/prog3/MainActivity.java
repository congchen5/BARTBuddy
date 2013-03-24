package edu.berkeley.cs160.congchen.prog3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import edu.berkeley.cs160.congchen.prog3.departureFrag.OnStationSelectedListener;
import edu.berkeley.cs160.congchen.prog3.tripFrag.OnTripSelectedListener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.TabContentFactory;

public class MainActivity extends Activity implements OnStationSelectedListener, OnTripSelectedListener {
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
	
	private Bitmap decodeFile(File f){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=70;

	        //Find the correct scale value. It should be the power of 2.
	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}

	@Override
	public void onTripSelected(String from, String to, Activity a) {
		Intent launchingIntent = new Intent();
		launchingIntent.setClass(a, TripDetailActivity.class);
		
		Log.d("values: ", from + " to " + to);
		launchingIntent.putExtra("from", from);
		launchingIntent.putExtra("to", to);
		startActivity(launchingIntent);	
	}
}
