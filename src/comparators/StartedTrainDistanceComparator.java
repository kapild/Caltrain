package comparators;
//package caltrain;
//
//import java.util.Comparator;
//
//import caltrain.objects.TrainStopICatch;
//
//public class StartedTrainDistanceComparator implements Comparator<TrainStopICatch>{
//
//	//there can be two kind of train, one which are already running and one which are about to
//	//reach the first station
//	@Override
//	public int compare(TrainStopICatch o1, TrainStopICatch o2) {
//		
//		long arrivalFirstStop1 = o1.path.getFirstStep().getArrivateDate();
//		long arrivalFirstStop2  = o2.path.getFirstStep().getArrivateDate();
//			
//		
//		//give preference to current running trains;
//		
//		if(arrivalFirstStop1 < arrivalFirstStop2){
//			return 1;
//		}
//	}
//
//	
//	
//}
