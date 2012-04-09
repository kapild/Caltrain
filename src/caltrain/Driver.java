package caltrain;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.javadocmd.simplelatlng.LatLng;

import caltrain.objects.GTFSData;
import caltrain.objects.Stops;
import caltrain.objects.util.TrainStationIBoard;
import caltrain.travel.CaltrainTravel;



public class Driver {

	
	
	
	public static void main(String argv[]) throws IOException, ParseException{
	
		if(argv.length!=3){
			System.out.println("Usage: java Driver \\\"destination\\\" \\\"lat,long\\\" option[1|2|3]" );
			System.out.println("Closest Train: java Driver \\\"San Mateo Caltrain\\\"  \\\"37.428835,-122.142703\\\" option1");
			System.out.println("Fastest Train from Palo Alto on 04/09/2012 at 16:30 java Driver \\\"San Mateo Caltrain\\\"  \\\"Palo Alto\\\" option3");
			
			System.exit(0);
		}
		
		String destinationId = argv[0];
		String latLongTokens[] = argv[1].split("\\,");
		if(latLongTokens.length!=2){
			System.out.println("Invalid representation of Lat Long. usage:  \\\"37.428835,-122.142703\\\"");
			System.exit(0);
		}
		
		//set the destination LatLng
		LatLng destinationLatLong = new LatLng(Double.parseDouble(latLongTokens[0]), Double.parseDouble(latLongTokens[1]));
		Date now = new Date();

		//init the caltrain data, this will load all the necessary table info by reading files. 
		Caltrain readCaltarinFiles  = new Caltrain();
		
		//get the data in GTFS( General Transit Format)
		GTFSData caltrainGTFSData = readCaltarinFiles.getCaltrainGTFSData();
		
		//this is where all the logic is implemented
		CaltrainTravel travel = new CaltrainTravel(caltrainGTFSData);
		Stops destionationStop = caltrainGTFSData.allStops.get(destinationId);
	
		if(argv[2].equals("option1"))
		{
				List<TrainStationIBoard> closestStations = travel.getClosestsTrain(destinationLatLong, now, destionationStop, 1);
		}else if(argv[2].equals("option2"))
		{
				List<TrainStationIBoard> fastestStation = travel.getFastestTrainToDestination(destinationLatLong, now, caltrainGTFSData.allStops.get(StringConstants.SAN_MATEO_STOP_ID), 1);
		}else
		{
			LatLng myLatlong = caltrainGTFSData.allStops.get(StringConstants.PALO_ALTO_STOP_ID).getLatLong();
			destionationStop = caltrainGTFSData.allStops.get(StringConstants.SAN_MATEO_STOP_ID);
			java.util.Calendar c = java.util.Calendar.getInstance();
			c.setTime(now);
			c.set(java.util.Calendar.DAY_OF_MONTH, 9);
			c.set(java.util.Calendar.HOUR_OF_DAY, 16);
			c.set(java.util.Calendar.MINUTE,30);
			c.set(java.util.Calendar.SECOND, 00);
			Date dateMonday = c.getTime();
			List<TrainStationIBoard> fastestStation = travel.getFastestTrainToDestination(myLatlong, dateMonday, destionationStop, 2);
		}
}
	
	
//	JSONObject jsonObject = new JSONObject();
////JsonO
//TrainStationIBoard obj = fastestStation.get(0);
//jsonObject.put("myLocation", obj.myLocation);
//jsonObject.put("boardStation", obj.baordingStep.getStop().getStopId());
//jsonObject.put("departDate", Utils.dateToHHMMSSFormat(obj.baordingStep.getArrivateDate()));
//jsonObject.put("arrivalDate", Utils.dateToHHMMSSFormat(obj.destinationStep.getArrivateDate()));
//jsonObject.put("destinationStation", obj.destination.getStopId());
//jsonObject.put("totalTime", Utils.getTimeToArrival(obj.getTime()));
//jsonObject.put("totalDistance", obj.getDistance());
	
	
}
