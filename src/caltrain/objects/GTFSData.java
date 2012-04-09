package caltrain.objects;

import java.util.List;
import java.util.Map;

public class GTFSData {

	public final Map<String, Stops> allStops ;
	public final Map<String, Route> allRoutes;
	public final Map<String, Calendar> allCalendars;
	public final Map<String, Trips> allTrips;
	public final Map<String, TrainPath> allTrainPath;
	public final Map<Stops, List<TrainStep>> allStopStep;

	public GTFSData(Map<String, Stops> allStops, Map<String, Route> allRoutes, Map<String, Calendar> allCalendars,
			Map<String, Trips> allTrips, Map<String, TrainPath> allTrainPath, Map<Stops, List<TrainStep>> allStopStep){
		
		this.allStops= allStops;
		this.allRoutes= allRoutes;
		this.allCalendars= allCalendars;
		this.allTrainPath= allTrainPath;
		this.allTrips= allTrips;
		this.allStopStep  = allStopStep;
		
	}
}
