package edu.berkeley.cs160.congchen.prog3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class TripInfoXmlParser {
	private static final String ns = null;
	
	public List<TripInfo> parse(InputStream in) throws XmlPullParserException, IOException {
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
    
    private List<TripInfo> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<TripInfo> values = new ArrayList<TripInfo>();
        
        parser.require(XmlPullParser.START_TAG, ns, "root");
        while (parser.next() != XmlPullParser.END_TAG) {
        	if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            
            // Starts by looking for the entry tag
            if (name.equals("schedule")) {
            	parser.require(XmlPullParser.START_TAG, ns, "schedule");
            	while (parser.next() != XmlPullParser.END_TAG) {
            		if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
            		String name2 = parser.getName();
                	if (name2.equals("request")) {
                		return readTrip(parser);
                	}
                	else {
                		skip(parser);
                	}
            	}

            } else {
                skip(parser);
            }
        }
        return values;
    }
    
    // Parses the contents of an InfoTrip. If it encounters a the appropriate tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private List<TripInfo> readTrip(XmlPullParser parser) throws XmlPullParserException, IOException {
    	parser.require(XmlPullParser.START_TAG, null, "request");
    	Log.d("asdfasdfasdf: ", "" + XmlPullParser.END_TAG);
    	
    	List<TripInfo> values = new ArrayList<TripInfo>();
    	
        Log.d("getName before loop: ", parser.getName());
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("inside readTrip with name value: ", name);
            if (name.equals("trip")) {
 	
            	String[] r = new String[3];
                r[0] = parser.getAttributeValue(2);
                r[1] = parser.getAttributeValue(3);
                r[2] = parser.getAttributeValue(5);
                
            	values.add(new TripInfo(r[0], r[1], r[2]));
            	skip(parser);
            }
            else {
            	skip(parser);
            }
        }
        return values;
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
