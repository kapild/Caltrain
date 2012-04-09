package caltrain.objects;

import java.text.ParseException;
import java.util.Date;

import caltrain.Utils;

public class Trips {

	private final String tripId;
	private final Calendar serviceId;
	private final String tripHeadSign;
	private final DIRECTION direction;
	
	private final Route ignoreRouteId;
	
//	trip_id,route_id,service_id,trip_headsign,direction_id,block_id,shape_id
//	"101_20110701","ct_local_20110701","WD_20110701","San Francisco (Train 101)","0",,"cal_sj_sf"
	

	public Trips(String tripId, Route routeId, Calendar serviceId, String tripHeadSign, String direction){
		this.tripId = tripId;
		this.ignoreRouteId=routeId;
		this.serviceId = serviceId;
		this.tripHeadSign = tripHeadSign;
		this.direction = direction.equals("0") ? DIRECTION.NORTHBOUND : DIRECTION.SOUTHBOUND;		
	}
	
	
	public boolean isWeekdayTrain(){
		return serviceId.isWeekday();
	}
	
	public boolean isWeekendTrain(){
		return serviceId.isWeekend();
	}
	
	public boolean isNorthBound(){
		return direction == DIRECTION.NORTHBOUND ? true : false;
	}
	
	public boolean isSouthBound(){
		return !isNorthBound();
	}
	public enum DIRECTION {
		NORTHBOUND,SOUTHBOUND
	}
	

	public boolean isTrainRunningToday(Date date) throws ParseException{
		
		boolean dateWeekend = Utils.isWeekend(date);
		boolean dateWeekday = !dateWeekend;
		
		if(isWeekendTrain() == dateWeekend || isWeekdayTrain() == dateWeekday){
			return true;
		}
		return false;
	}


	public String getTripId() {
		return tripId;
	}


	public Calendar getServiceId() {
		return serviceId;
	}


	public String getTripHeadSign() {
		return tripHeadSign;
	}


	public DIRECTION getDirection() {
		return direction;
	}


	public Route getIgnoreRouteId() {
		return ignoreRouteId;
	}
	
	

	public String toString(){
		return tripId;
	}
}
