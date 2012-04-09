package caltrain.travel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.javadocmd.simplelatlng.LatLng;

import comparators.FastestTimeComparator;
import comparators.MeStationDistanceComparator;

import caltrain.StringConstants;
import caltrain.Utils;
import caltrain.objects.GTFSData;
import caltrain.objects.TrainStep;
import caltrain.objects.Stops;
import caltrain.objects.TrainPath;
import caltrain.objects.Trips;
import caltrain.objects.util.MeAndStation;
import caltrain.objects.util.TrainStationIBoard;

public class CaltrainTravel {


	GTFSData gtfs ;
	
	public CaltrainTravel(GTFSData gtfs){
		this.gtfs = gtfs;
	}
	
	/*
	 * get the fastest train
	 */
	public List<TrainStationIBoard> getFastestTrainToDestination(LatLng currentLocation, Date time, Stops destination, int MAX) throws ParseException{

		if(currentLocation == null || time == null || destination == null){
			new Exception("Invalid null parameters Location:" + currentLocation +
					" time:"  + time + " destination:" + destination);
		}

		//get all the trains running today
		Map<String, TrainPath> trainsRunningToday = getTrainsRunningToday(time);
		//if there are no trains, then we are at the end of day, set the time to next day 4 am
		if(trainsRunningToday.size()==0){
			time = Utils.resetTimeTo4am(time);
			trainsRunningToday = getTrainsRunningToday(time);
		}
		
		
		//list to hold my location and station location
		List<MeAndStation> closestStationList = null;
		//get stations closest to
		closestStationList = getStationsClosestToMe(currentLocation);

		//my time in milli seconds from last night 12 am
		long myTime = Utils.getTimeToDate(Utils.dateToHHmmss(time)).getTime();
		
		//list of station and their time when I can board a train t destination
		List<TrainStationIBoard>  stationsICanBaord = new ArrayList<TrainStationIBoard>();
		
		//add 5 minutes of timePenalty for second, third, closest station
		long timePenalty=0;

		//consider the 3 closest stations, ignore the rest
		for(int i = 0 ; i < closestStationList.size()   && i < 3;i++){

			//get the closest station first
			Stops closestStop = closestStationList.get(i).station;
			
			//add the penalty for 2 and third station
			if(i!=0){
				if(i==1)
					timePenalty = Utils.TIME_PENALTY;
				else{
					timePenalty = (long) (Utils.TIME_PENALTY * i);
				}
			}
			
			//get all the steps when the trains stops at this station 
			List<TrainStep> list  = gtfs.allStopStep.get(closestStop);
			
			//to be on safer side sort the list on arrival/departure time
			Collections.sort(list);
			
			//now we have all the sequence of trains stops sorted by time
			for(int j = 0 ; j < list.size()  ;j++){
				
				TrainStep step = list.get(j);
				
				//check if the time of departure is greater than my current time
				//also check if the train is running today
				if(myTime < step.getDepartDate() && trainsRunningToday.containsKey(step.getTripId().getTripId())){

					//get the train for this train stop
					TrainPath train = trainsRunningToday.get(step.getTripId().getTripId());
				
					//find if the destination and closest stop exist in the current running train
					//, the train stops at the destination after the boarding station
					TrainStep destinationStep =null;
					
					if((destinationStep = train.getDestinationStep2(step.getStop(), destination))!=null){
						//add penalty to the arrival station
						step.increaseTravelTime(timePenalty);
						//add this as a contender for boarding station
						stationsICanBaord.add(new TrainStationIBoard(currentLocation, step, destinationStep,train, destination));
					}
				}
			}
		}
		
		//sort it on the basis of smallest time to travel and if this is same get the first train
		Collections.sort(stationsICanBaord, new FastestTimeComparator());
		
		Utils.log("Total Stations found:" + stationsICanBaord.size(), 0);
		for(int i =0 ; i < stationsICanBaord.size() && i < MAX;i++){
			Utils.log(i + ":" + stationsICanBaord.get(i).toString(),1);
		}
		
		return stationsICanBaord;
	}
	

	public List<TrainStationIBoard> getClosestsTrain(LatLng currentLocation, Date time, Stops destination, int MAX) throws ParseException{
		
		if(currentLocation == null || time == null || destination == null){
			new Exception("Invalid null parameters Location:" + currentLocation +
					" time:"  + time + " destination:" + destination);
		}
		
		//get all the running trains today
		Map<String, TrainPath> trainsRunningToday = getTrainsRunningToday(time);
		
		//if there are no trains, then we are at the end of day, set the time to next day 4 am
		if(trainsRunningToday.size()==0){
			time = Utils.resetTimeTo4am(time);
			trainsRunningToday = getTrainsRunningToday(time);
						
		}
		
		//list to hold my location and station
		List<MeAndStation> closestStationList = null;
		closestStationList = getStationsClosestToMe(currentLocation);

		//my time in milli seconds from last night 12 am
		long myTime = Utils.getTimeToDate(Utils.dateToHHmmss(time)).getTime();
		
		int total=0;
		List<TrainStationIBoard>  stationsICanBaord = new ArrayList<TrainStationIBoard>(MAX);
		
		for(int i = 0 ; i < closestStationList.size() && total < MAX ;i++){

			//get the closest station first
			Stops closestStop = closestStationList.get(i).station;
			
			//get all the steps when the trains stops at this station 
			List<TrainStep> list  = gtfs.allStopStep.get(closestStop);
			//to be on safer side sort the list on arrival/departure time
			Collections.sort(list);
			
			//now we all the sequence of trains stops sorted by time
			for(int j = 0 ; j < list.size() && total <  MAX ;j++){
				TrainStep step = list.get(j);
				//check if the time of departure is greater than my current time
				//also check if the train is running today
				if(myTime < step.getDepartDate() && trainsRunningToday.containsKey(step.getTripId().getTripId())){

					//find if the destination and closest stop exist in the current running train
					TrainPath train = trainsRunningToday.get(step.getTripId().getTripId());
					
					//check if the train stops at the destination after the boarding station
					TrainStep destinationStep =null;
					if((destinationStep = train.getDestinationStep2(step.getStop(), destination))!=null){
						stationsICanBaord.add(new TrainStationIBoard(currentLocation, step, destinationStep,train, destination));
						total++;
					}
				}
			}
		}
		Utils.log("Total Closest stations found:" + stationsICanBaord.size(), 1);
		for(int i =0 ; i < stationsICanBaord.size();i++){
			Utils.log(i + ":" + stationsICanBaord.get(i).toString(),1);
		}
		return stationsICanBaord;
	}

	
	public void getClosestTrainToSanMateoNow(LatLng loc) throws ParseException{
		getClosestsTrain(loc, new Date(), gtfs.allStops.get(StringConstants.SAN_MATEO_STOP_ID), 1);
	}
	
	/*
	 * Get all the trains which are running today and currently
	 * on track as per time
	 */
	private Map<String, TrainPath> getTrainsRunningToday(Date date) throws ParseException{
		Map<String, TrainPath> runningTrain = new HashMap<String, TrainPath>();
		
		//get all teh possible trains/trips
		for( Entry<String, Trips> train : gtfs.allTrips.entrySet()){
			
			Trips trip = train.getValue();
			//if not running today.
			if(!trip.isTrainRunningToday(date)){
				continue;
			}
			TrainPath path = gtfs.allTrainPath.get(trip.getTripId());
			if(!path.isCurrentlyOnTrack(date)){
				continue;
			}
			runningTrain.put(trip.getTripId(), path);
		}
		Utils.log("Total running trains:" + runningTrain.size(), 1);
		return runningTrain;
	}
	
	private List<MeAndStation> getStationsClosestToMe(LatLng mylocation){

		List<MeAndStation> meAndStationList = new ArrayList<MeAndStation>();
		//iterate over all stations
		for(String stopId: gtfs.allStops.keySet()){
			meAndStationList.add( new MeAndStation(mylocation, gtfs.allStops.get(stopId)));
		}
		//sort them by distance
		Collections.sort(meAndStationList, new MeStationDistanceComparator());
		return meAndStationList;
	}
	
}
