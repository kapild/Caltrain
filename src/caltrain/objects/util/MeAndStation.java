package caltrain.objects.util;

import caltrain.objects.Stops;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class MeAndStation {

	public LatLng myLocation;
	public Stops station;
	
	public MeAndStation(LatLng myLocation, Stops stations){
		this.myLocation = myLocation;
		this.station = stations;
				
	}
	
	
	public double getDistance(){
		return LatLngTool.distance(myLocation, station.getLatLong(), LengthUnit.MILE);
	}
}
