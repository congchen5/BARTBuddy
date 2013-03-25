package edu.berkeley.cs160.congchen.prog3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class singleStationListItem extends ListFragment {
//	static final String TAG = Fragment.class.getSimpleName();
//	private String station_name;
//	private URL url;
//	private HttpURLConnection urlConnection;
	
	private List<Entry> finalEntries;
	private InfoAdapter e_adapter;
	private Context context;
//	TextView mtext;
	
	public static singleStationListItem newInstance(String station) {
		singleStationListItem s = new singleStationListItem();
		
		Bundle args = new Bundle();
		args.putString("station", station);
		s.setArguments(args);
		
		return s;		
	}
	
	public String getShownStation() {
		return getArguments().getString("station");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Binding resources Array to ListAdapter
		finalEntries = new ArrayList<Entry>();
		
		this.e_adapter = new InfoAdapter(getActivity(), R.layout.station_detail_list_item, finalEntries);
        setListAdapter(this.e_adapter);
        
        context = getActivity();
	}
	
	@Override
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
		
		View v = inflater.inflate(R.layout.single_station_item_view, container, false);
		TextView txtStation = (TextView) v.findViewById(R.id.station_label);
		txtStation.setText(getShownStation());
		String temp = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + MainActivity.getStationAbbr(getShownStation()) + "&key=MEKB-5UEP-ELQU-5SNA";
		
		String t = "";
		new UpdateTrainTime().execute(temp);
		//TextView stationBody = (TextView) getActivity().findViewById(R.id.station_body);
		Log.d("t value: ", t);
		
		//String xmlText = getXMLFromUrl(temp);
		//Log.d("xml printout: \n", xmlText);
		return v;
	}
	
	private class UpdateTrainTime extends AsyncTask<String, Integer, String> {
		
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
	        this.dialog.setMessage("Getting Data...");
	        this.dialog.show();
	    }
		
		protected String doInBackground(String... urlStrings) {
			String responseXml = null;

		    try {
		        // defaultHttpClient
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpResponse response;
		        
		        Log.d("value before HttpGet: ", urlStrings[0]);
		        
		        response = httpClient.execute(new HttpGet(urlStrings[0]));
		        StatusLine statusLine = response.getStatusLine();
		        
		        if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
		        	
		        	responseXml = EntityUtils.toString(response.getEntity());
		        	
//		        	ByteArrayOutputStream out = new ByteArrayOutputStream();
//		        	response.getEntity().writeTo(out);	
//	                out.close();
//	                responseXml = out.toString();
		        }
		        else {
		        	//Closes the connection.
	                response.getEntity().getContent().close();
	                throw new IOException(statusLine.getReasonPhrase());
		        }
		    } catch (ClientProtocolException e) {
		    	Log.d("Client Protocl Excepton: ", e.toString());
	        } catch (IOException e) {
	        	Log.d("IO Exception: ", e.toString());
	        } catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return responseXml;
		}
		
		@Override
	    protected void onPostExecute(String result) {
			if (dialog.isShowing()) {
	            dialog.dismiss();
	        }
			
	        super.onPostExecute(result);
	        Log.d("Post Execute Run, result: ", result);
	        //TextView stationBody = (TextView) getActivity().findViewById(R.id.station_body);
			//stationBody.setText(result);
	        //Do anything with response..
			
			StationInfoXmlParser stationInfoXmlParser = new StationInfoXmlParser();
			List<Entry> entries = null;
			
			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(result.getBytes());
			Log.d("is: ", is.toString());
			try {
				entries = stationInfoXmlParser.parse(is);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Log.d("entries value: ", entries.toString());
			
			String t = "";
			for (Entry a: entries) {
				t = t + a.destination + " " + a.getTimes() + "\n";
			}
			
			finalEntries = entries;
			if (entries != null && entries.size() > 0) {
				TextView h1 = (TextView) getActivity().findViewById(R.id.station_header1);
				TextView h2 = (TextView) getActivity().findViewById(R.id.station_header2);
				h1.setText("Line");
				h2.setText("Estimated Arrival Time");
				
				for (int i = 0; i < entries.size(); i++) {
					e_adapter.add(entries.get(i));
				}
				e_adapter.notifyDataSetChanged();
			}
			else {
				TextView stationBody = (TextView) getActivity().findViewById(R.id.station_body);
				stationBody.setText("No information at this time. \n Please try again later" );
			}
			
			Log.d("finalEntries count: ", "" + finalEntries.size());
			Log.d("e_adapter count: ", "" + e_adapter.getCount());
			//stationBody.setText(t);
	    }
	}
	
	private class InfoAdapter extends ArrayAdapter<Entry> {
		private List<Entry> items;
		private LayoutInflater vi;
		public InfoAdapter(Context context, int textViewResourceId, List<Entry> items) {
			super(context, textViewResourceId, items);
			vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = vi.inflate(
						R.layout.station_detail_list_item, null);
			}
			Entry e = items.get(position);
			if (e != null) {
				TextView tt = (TextView) v.findViewById(R.id.dest_label);
				TextView bt = (TextView) v.findViewById(R.id.time_label);
				if (tt != null) {
					tt.setText(e.destination);
				}
				if (bt != null) {
					bt.setText(e.getTimes());
				}
			}
			return v;
		}
	}
}
	

