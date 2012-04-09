package comparators;

import java.util.Comparator;

import caltrain.objects.util.TrainStationIBoard;

public class FastestTimeComparator implements Comparator<TrainStationIBoard>{

	@Override
	public int compare(TrainStationIBoard o1, TrainStationIBoard o2) {

		long timeDiff = o1.getTime() - o2.getTime();

		//find the train which has smallest time
		if(timeDiff!=0){
			return o1.getTime() - o2.getTime() < 0 ? -1 : 1;
		}
		
		//if they are equal, find the first train
		return o1.baordingStep.getArrivateDate()- o2.baordingStep.getArrivateDate()< 0 ? -1 : 1; 

	}

	
	
}
