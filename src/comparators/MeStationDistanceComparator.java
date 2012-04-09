package comparators;

import java.util.Comparator;

import caltrain.objects.util.MeAndStation;

public class MeStationDistanceComparator implements Comparator<MeAndStation> {

	@Override
	public int compare(MeAndStation arg0, MeAndStation arg1) {
		
		//simple sort by distance of me and the station
		double dist1 = arg0.getDistance();
		double dist2 = arg1.getDistance();
		
		return dist1 - dist2 < 0 ? -1 : 1;
	}

}
