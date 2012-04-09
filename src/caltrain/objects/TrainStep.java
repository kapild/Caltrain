package caltrain.objects;

import java.text.ParseException;

import caltrain.Utils;

public class TrainStep  implements Comparable<TrainStep>{
	
	private final int uniqueId;
	private final Trips tripId; 
	private long arrivateDate;
	private final long departDate;
	private final Stops stop;
	private final int sequence;
	
	
	public TrainStep(int unique, Trips tripId, String arrivalDate, String departDate, Stops stop, int sequence) throws ParseException{
		this.uniqueId = unique;
		this.tripId = tripId;
		this.arrivateDate = Utils.getTimeToDate(arrivalDate).getTime() ;
		this.departDate = Utils.getTimeToDate(departDate).getTime() ;
		this.stop = stop;
		this.sequence = sequence;
	}
	
	public String toString(){
		return "\n\tTrip :" + tripId.getTripHeadSign() + " heading:" + tripId.getDirection() +  " " + tripId.getIgnoreRouteId().getLongName() + "\n\tstop:" + stop.toString() + "\n\tarrival:" + Utils.dateToHHMMSSFormat(arrivateDate)
				+ "\n\tdepart:" + Utils.dateToHHMMSSFormat(departDate);
	}
	
	public int getStopSequence(){
		return sequence;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public Trips getTripId() {
		return tripId;
	}

	public long getArrivateDate() {
		return arrivateDate;
	}

	public long getDepartDate() {
		return departDate;
	}

	public Stops getStop() {
		return stop;
	}

	public int getSequence() {
		return sequence;
	}

	public void increaseTravelTime(long penalty){
		arrivateDate = arrivateDate - penalty;
	}
	
	@Override
	public int compareTo(TrainStep that) {
		long cmp = arrivateDate - that.arrivateDate;
		return (int)cmp;
	}	
}
