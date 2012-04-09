package comparators;

import java.util.Comparator;

import caltrain.objects.util.TrainStationIBoard;

public class DistanceComparator implements Comparator<TrainStationIBoard>{

	@Override
	public int compare(TrainStationIBoard o1, TrainStationIBoard o2) {
		
		double dist1 = o1.getDistance();
		double dist2 = o2.getDistance();
		
			return dist1 - dist2 < 0 ? -1 : 1;
	}

	
	
}
