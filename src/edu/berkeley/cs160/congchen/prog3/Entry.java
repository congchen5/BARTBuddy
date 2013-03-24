package edu.berkeley.cs160.congchen.prog3;

import java.util.List;

import android.util.Log;

//This class represents a single entry (post) in the XML feed.
// It includes the data members "destination", "minutes", "hexcolor"
public class Entry {
    public final String destination;
    public List<Estimate> estimates;

    public Entry(String destination, List<Estimate> estimates) {
        this.destination = destination;
        this.estimates = estimates;
    }
    
    public String getTimes() {
    	String t = "";
    	for (Estimate e: estimates) {
    		if ((e.toString()).equals("Leaving")) {
    			t = t + " " + e + ",";
    		}
    		else {
        		t = t + " " + e + "min,";	
    		}
    	}
    	return t.substring(0, t.length() - 1);
    }
}

