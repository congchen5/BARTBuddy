package edu.berkeley.cs160.congchen.prog3;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class tripDetailFrag extends ListFragment {
	private List<TripInfo> finalEntries;
	private TripInfoAdapter t_adapter;
	private Context context;

	public static tripDetailFrag newInstance(String[] trip) {
		tripDetailFrag t = new tripDetailFrag();

		Log.d("trip value: ", trip[0] + " to " + trip[1]);

		Bundle args = new Bundle();
		args.putString("from", trip[0]);
		args.putString("to", trip[1]);
		t.setArguments(args);

		return t;
	}

	public String getFromStation() {
		return getArguments().getString("from");
	}

	public String getToStation() {
		return getArguments().getString("to");
	}

	public String getTravelToggle() {
		return getArguments().getString("travelToggle");
	}

	public String getTimeString() {
		return getArguments().getString("timeString");
	}
	
	public String getSrcStation() {
		return getArguments().getString("srcStation");
	}
	
	public String getDestStation() {
		return getArguments().getString("destStation");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Binding resources Array to ListAdapter
		finalEntries = new ArrayList<TripInfo>();
		
		this.t_adapter = new TripInfoAdapter(getActivity(), R.layout.trip_detail_frag_view, finalEntries);
        setListAdapter(this.t_adapter);
        
        context = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		View v = inflater.inflate(
				R.layout.trip_detail_frag_view, container, false);

		String temp = "http://api.bart.gov/api/sched.aspx?cmd="
				+ getTravelToggle() + "&orig=" + getFromStation() + "&dest="
				+ getToStation() + "&time=" + getTimeString()
				+ "&key=MEKB-5UEP-ELQU-5SNA";
		
		new GetTripInfo().execute(temp);
		
		return v;
	}

	private class GetTripInfo extends AsyncTask<String, Integer, String> {

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
					
				} else {
					// Closes the connection.
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

			TextView tripBody = (TextView)
			getActivity().findViewById(R.id.trip_body);
			//tripBody.setText(result);
			// Do anything with response..

			TripInfoXmlParser tripInfoXmlParser = new TripInfoXmlParser();
			List<TripInfo> entries = null;

			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(result.getBytes());
			Log.d("is: ", is.toString());
			try {
				entries = tripInfoXmlParser.parse(is);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			TextView tripHeader = (TextView) getActivity().findViewById(R.id.trip_label);
			TextView s = (TextView) getActivity().findViewById(R.id.srcS);
			TextView d = (TextView) getActivity().findViewById(R.id.destS);
			
			finalEntries = entries;
			if (entries != null && entries.size() > 0) {
				tripHeader.setText("Trip from " + getSrcStation() + " to " + getDestStation() + "\n One Way Cost: $" + entries.get(0).fare);
				s.setText("Leave Time");
				d.setText("Arrival Time");
				for (int i = 0; i < entries.size(); i++) {
					t_adapter.add(entries.get(i));
				}
				t_adapter.notifyDataSetChanged();
			}
			else {
				tripHeader.setText("No information at this time. \n Try again later");
				s.setText("");
				d.setText("");
			}
			Log.d("finalEntries count: ", "" + finalEntries.size());
		}
	}
	
	private class TripInfoAdapter extends ArrayAdapter<TripInfo> {
		private List<TripInfo> items;
		private LayoutInflater vi;
		public TripInfoAdapter(Context context, int textViewResourceId, List<TripInfo> items) {
			super(context, textViewResourceId, items);
			vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				v = vi.inflate(
						R.layout.trip_detail_list_item, null);
			}
			TripInfo t = items.get(position);
			if (t != null) {
				TextView tt = (TextView) v.findViewById(R.id.orig_time);
				TextView bt = (TextView) v.findViewById(R.id.dest_time);
				if (tt != null) {
					tt.setText(t.origTime);
				}
				if (bt != null) {
					bt.setText(t.destTime);
				}
			}
			return v;
		}
	}
}
