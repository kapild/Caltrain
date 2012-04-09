package caltrain;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import caltrain.objects.Calendar;
import caltrain.objects.GTFSData;
import caltrain.objects.Route;
import caltrain.objects.Stops;
import caltrain.objects.TrainPath;
import caltrain.objects.TrainStep;
import caltrain.objects.Trips;



public class Caltrain {

	public Map<String, Stops> allStops ;
	public Map<String, Route> allRoutes ;
	public Map<String, Calendar> allCalendars;
	public Map<String, Trips> allTrips;
	public Map<String, TrainPath> allTrainPath ;
	public Map<Stops, List<TrainStep>> allStopTrainStep;
	
	public Caltrain() throws IOException, ParseException{
		init();
		
	}
	
	
	public void init() throws IOException, ParseException{
		
		readStationFile();
		readRouteFile();
		readCalendarFile();
		readTrainTripsFile();
		loadTrainStopsFile();
	}
	
	public GTFSData getCaltrainGTFSData(){
		return new GTFSData(allStops, allRoutes, allCalendars, allTrips, allTrainPath, allStopTrainStep);
		
	}
	public void readStationFile() throws IOException{
		BufferedReader reader= null;
		try {
			reader  = FileHandler.getReader(StringConstants.STATION_FILE_NAME);
		} catch (FileNotFoundException e) {
			reader = null;
			e.printStackTrace();
		}
		if(reader == null){
			return;
		}
		
		allStops = new HashMap<String, Stops>();
		
		
		String line = null;

		Utils.log("\n\n",0);
		Utils.log("Loading all Stations..", 0);
		
		//ignore teh first line
		reader.readLine();
		while( (line= reader.readLine())!=null){
			String [] tokens = line.split("\\\"");
			//"22nd Street Caltrain","22nd Street Caltrain","1149 22nd Street,San Francisco","37.757674","-122.392636","1",
			allStops.put(tokens[1], new Stops(tokens[1], tokens[3],tokens[5],tokens[7],tokens[9],tokens[11]));
		}

		Utils.log("Total size of stations:" + allStops.size(),0);
		Utils.log("\n\n",0);
		
	}
	
	public void readRouteFile() throws IOException{

		BufferedReader reader= null;
		try {
			reader  = FileHandler.getReader(StringConstants.ROUTE_FILE_NAME);
		} catch (FileNotFoundException e) {
			reader = null;
			e.printStackTrace();
		}
		if(reader == null){
			return;
		}
		
		allRoutes = new HashMap<String, Route>();
		
		String line = null;
		Utils.log("\n\n",0);
		Utils.log("Loading all Routes..",0);
		
		//ignore teh first line
		reader.readLine();
		while( (line= reader.readLine())!=null){
			String [] tokens = line.split("\\,");
			//"ct_bullet_20110701",,"Bullet",,2,,E31837,
			allRoutes.put(Utils.normText(tokens[0]), new Route(Utils.normText(tokens[0]), 
					Utils.normText(tokens[2]),Utils.normText(tokens[4])));
		}

		Utils.log("Total size of Routes:" + allRoutes.size(),0);
		Utils.log("\n\n",0);
		
	}

	public void readCalendarFile() throws IOException, ParseException{

		BufferedReader reader= null;
		try {
			reader  = FileHandler.getReader(StringConstants.CALENDAR_FILE_NAME);
		} catch (FileNotFoundException e) {
			reader = null;
			e.printStackTrace();
		}
		if(reader == null){
			return;
		}
		
		allCalendars = new HashMap<String, Calendar>();
		String line = null;

		Utils.log("\n\n");
		Utils.log("Loading all Calendars..",0);
		
		//ignore teh first line
		reader.readLine();
		while( (line= reader.readLine())!=null){
			String [] tokens = line.split("\\,");
			//"WD_20110701","1","1","1","1","1","0","0","20110701","20160701"
			allCalendars.put(Utils.normText(tokens[0]), new Calendar(tokens));
		}

		Utils.log("Total size of Calendars:" + allCalendars.size(),0);
		Utils.log("\n\n",0);
		
	}

	public void readTrainTripsFile() throws IOException, ParseException{

		BufferedReader reader= null;
		try {
			reader  = FileHandler.getReader(StringConstants.TRAIN_TRIP_FILE_NAME);
		} catch (FileNotFoundException e) {
			reader = null;
			e.printStackTrace();
		}
		if(reader == null){
			return;
		}
		
		allTrips = new HashMap<String, Trips>();
		String line = null;

		Utils.log("\n\n");
		Utils.log("Loading all Trains..",0);
		
		//ignore the first line
		reader.readLine();
		
		while( (line= reader.readLine())!=null){
		String [] tokens = line.split("\\,");
//			trip_id,route_id,service_id,trip_headsign,direction_id,block_id,shape_id
//			"101_20110701","ct_local_20110701","WD_20110701","San Francisco (Train 101)","0",,"cal_sj_sf"
			if(tokens.length< 5){
				new Exception("Invalid trains length:" + line);
			}
			String service_id = Utils.normText(tokens[2]);
			if(!allCalendars.containsKey(service_id) ){
				Utils.log("Ignoring the train as no service: " + service_id + " found" + line, 0);
				continue;
			}
			String routeId = Utils.normText(tokens[1]);
			if(!allRoutes.containsKey(routeId) ){
				Utils.log("Ignoring the train as no route:" + routeId + " found" + line, 0);
				continue;
			}
			
			allTrips.put(Utils.normText(tokens[0]), new Trips(Utils.normText(tokens[0]), allRoutes.get(routeId),allCalendars.get(service_id),
					Utils.normText(tokens[3]),Utils.normText(tokens[4])));
		}

		Utils.log("Total size of trains:" + allTrips.size(),0);
		Utils.log("\n\n",0);
		
	}
		
	public  void loadTrainStopsFile() throws IOException, NumberFormatException, ParseException{

		BufferedReader reader= null;
		try {
			reader  = FileHandler.getReader(StringConstants.TRAIN_STEP_STOP_FILE_NAME);
		} catch (FileNotFoundException e) {
			reader = null;
			e.printStackTrace();
		}
		if(reader == null){
			return;
		}
		
		allTrainPath = new HashMap<String, TrainPath>();
		allStopTrainStep = new HashMap<Stops, List<TrainStep>>();
		String line = null;
		Utils.log("\n\n",0);
		Utils.log("Loading all Train Steps stop times..",0);
		
		//ignore the first line
		reader.readLine();
		
		int index =0;
		
		String previousTripId = null;
		TrainPath trainPath = null;
				
		while( (line= reader.readLine())!=null){
		String [] tokens = line.split("\\,");
//		trip_id,arrival_time,departure_time,stop_id,stop_sequence,pickup_type,drop_off_type,shape_dist_traveled
//		"101_20110701","04:30:00","04:30:00","San Jose Caltrain",1,,,
			if(tokens.length< 5){
				new Exception("Invalid trains step stop length:" + line);
			}
			
			String tripid = Utils.normText(tokens[0]);
			String stopId = Utils.normText(tokens[3]);

			if(filterTrips(tripid, allTrips, line) || filterStops(stopId, allStops, line)){
				continue;
			}
			
			if(previousTripId == null || !previousTripId.equals(tripid)){
				trainPath = new TrainPath(tripid);
				previousTripId=tripid;
				allTrainPath.put(tripid, trainPath);
			}
			
			String arrivalDate = Utils.normText(tokens[1]);
			String departDate = Utils.normText(tokens[2]);
			Stops stop = allStops.get(stopId);
			//add the step to the trainPath
			TrainStep trainStep =new TrainStep(index, allTrips.get(tripid),arrivalDate, departDate, stop, Integer.parseInt(tokens[4])); 
			trainPath.addStep(trainStep);
			
			List<TrainStep> list ;
			if(!allStopTrainStep.containsKey(stop)){
				list = new ArrayList<TrainStep>();
				allStopTrainStep.put(stop, list);
			}
			allStopTrainStep.get(stop).add(trainStep);
			
		}

		Utils.log("Total size of trains steps stop:" + allTrainPath.size(),0);
		Utils.log("\n\n",0);
		
	}
	
	
	private boolean filterTrips(String tripId, Map<String, Trips> alTrips, String line){
		
		if(!allTrips.containsKey(tripId)){
			Utils.log("Ignoring the train Step, no trip:" + tripId + " found:" + line, 0);
			return true;
		}
		return false;
	}
	
	private boolean filterStops(String stopId, Map<String, Stops> allStops, String line){

		if(!allStops.containsKey(stopId)){
			Utils.log("Ignoring the train Step, no Stop:" + stopId + " found:" + line, 0);
			return true;
		}
		return false;
	}
}
