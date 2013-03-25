package edu.berkeley.cs160.congchen.prog3;

public class TripInfo {
	public final String origTime;
	public final String destTime;
	public final String fare;
	
	public TripInfo(String fare, String origTime, String destTime) {
		this.origTime = origTime;
		this.destTime = destTime;
		this.fare = fare;
	}

}
