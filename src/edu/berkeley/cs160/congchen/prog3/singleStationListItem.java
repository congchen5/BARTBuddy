package edu.berkeley.cs160.congchen.prog3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class singleStationListItem extends Fragment {
//	static final String TAG = Fragment.class.getSimpleName();
	private String station_name;
	private URL url;
	private HttpURLConnection urlConnection;
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
		
		View v = (LinearLayout) inflater.inflate(R.layout.single_station_item_view, container, false);
		TextView txtStation = (TextView) v.findViewById(R.id.station_label);
		txtStation.setText(getShownStation());
		String temp = "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=" + MainActivity.getStationAbbr(getShownStation()) + "&key=MEKB-5UEP-ELQU-5SNA";
		
		String t = "";
		try {
			t = new UpdateTrainTime().execute(temp).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("t value: ", t);
		
		//String xmlText = getXMLFromUrl(temp);
		//Log.d("xml printout: \n", xmlText);
		return v;
	}
	
	private class UpdateTrainTime extends AsyncTask<String, Integer, String> {
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
	        super.onPostExecute(result);
	        Log.d("Post Execute Run, result: ", result);
	        TextView stationBody = (TextView) getActivity().findViewById(R.id.station_body);
			stationBody.setText(result);
	        //Do anything with response..
	    }
	}
}
	

