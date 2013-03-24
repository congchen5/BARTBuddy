package edu.berkeley.cs160.congchen.prog3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class StationInfoXmlParser {
	private static final String ns = null;

    // We don't use namespaces

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }
    
    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
        	if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            
            // Starts by looking for the entry tag
            if (name.equals("station")) {
            	parser.require(XmlPullParser.START_TAG, ns, "station");
            	while (parser.next() != XmlPullParser.END_TAG) {
            		if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
            		String name2 = parser.getName();
                	if (name2.equals("etd")) {
                    	entries.add(readEntry(parser));
                	}
                	else {
                		skip(parser);
                	}
            	}

            } else {
                skip(parser);
            }
        }
        return entries;
    }
    
    // Parses the contents of an entry. If it encounters a the appropriate tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "etd");
        String destination = null;
        List<Estimate> estimates = new ArrayList<Estimate>();
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("name: ", name);
            if (name.equals("destination")) {
            	destination = readDestination(parser);
            } else if (name.equals("estimate")) {
            	estimates.add(readEstimate(parser));
            } else {
                skip(parser);
            }
        }
        return new Entry(destination, estimates);
    }
    
    // Processes title tags in the feed.
    private String readDestination(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "destination");
        String destination = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "destination");
        return destination;
    }
    
    // Parses the contents of an estimate. If it encounters a the appropriate tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Estimate readEstimate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "estimate");
    	String minute = null;
          
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("minutes")) {
            	minute = readMinute(parser);
            	Log.d("minute read: ", minute);
            } else {
                skip(parser);
            }
        }
        return new Estimate(minute);
    }
    
    // Processes minute tags in the feed.
    private String readMinute(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "minutes");
        String minute = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "minutes");
        return minute;
    }
    
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    // Processes summary tags in the feed.
    public String readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "root");
        
        while (parser.next() != XmlPullParser.END_TAG) {
        	if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("time")) {
            	return getTime(parser);
        	}
        	else {
        		skip(parser);
        	}
        }
        return null;
        
    }
    
    // Processes time tags in the feed.
    private String getTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String time = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return time;
    }
    
    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
