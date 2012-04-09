package caltrain.objects;
import com.javadocmd.simplelatlng.LatLng;


public class Stops {

	

	private  final String stopId ;
	
	private  final String stopName;
	
	private  final String stopDesc;
	
	private  final LatLng latLong;
	
	private  final String zoneId;
	
	public Stops(String stopId, String stopName, String stopDesc, String lat, String lng, String zoneId){
		
		this.stopId= stopId;
		this.stopName = stopName;
		this.stopDesc = stopDesc;
		this.latLong = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
		this.zoneId = zoneId;
	}
	

	@Override public boolean equals(Object otherStop) {
		if(!(otherStop instanceof Stops)){
			return false;
		}
		Stops thisStop = (Stops) otherStop;
		return ( getStopId().equals(thisStop.stopId));
	};
	
	@Override
	public int hashCode() {
		return getStopName().hashCode();
	}
	
	
	public String getStopId() {
		return stopId;
	}


	public String getStopName() {
		return stopName;
	}



	public String getStopDesc() {
		return stopDesc;
	}



	public LatLng getLatLong() {
		return latLong;
	}


	public String getZoneId() {
		return zoneId;
	}
	
	
	@Override
	public String toString(){
		return stopId + " - " + stopName + " - " + stopDesc + " - " + latLong.toString() + " - " + zoneId;
	}
	
	
}
