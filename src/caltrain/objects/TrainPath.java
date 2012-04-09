package caltrain.objects;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import caltrain.Utils;

/*
 * Contains all the steps from of a train
 */
public class TrainPath {

	private List<TrainStep> steps;
	private String uniqueTripId;

	private int destinationTrainStepInx = -1;
	private Stops destinationStop = null;
	public TrainPath(String tripId){
		this.uniqueTripId = tripId;
		steps = new ArrayList<TrainStep>();
	}
	
	public boolean isCurrentlyOnTrack(Date date) throws ParseException{
		
		String hhmmss = Utils.dateToHHmmss(date);
		long currentTime = Utils.getTimeToDate(hhmmss).getTime();
		
		TrainStep firstStep = steps.get(0);
		TrainStep lastStep = steps.get(steps.size()-1);
		
		if(Utils.isStopedTrain){
			if( currentTime <=  lastStep.getDepartDate()){
				return true;
			}
		}else{
			if(currentTime >= firstStep.getArrivateDate() && currentTime <=  lastStep.getDepartDate()){
				return true;
			}
		}
		return false;
	}
	
	public boolean addStep(TrainStep step){
		if(steps!=null){
			steps.add(step);
			return true;
		}
		
		return false;
	}
	
	public TrainStep getFirstStep(){
		return steps.get(0);
	}
	
	public boolean isRunningNow(Date date){
		return false;
	}
	
	public TrainStep getDestinationStep(Stops dest){
		if(destinationTrainStepInx==-1 || destinationStop==null || !destinationStop.getStopId().equals(dest.getStopId())){
			for(int i =0; i < steps.size();i++ ){
				if(steps.get(i).getStop().getStopId().equals(dest.getStopId())){
					destinationStop = dest;
					destinationTrainStepInx = i;
					return steps.get(i);
				}
			}
		}
		if(destinationTrainStepInx == -1){
			return null;
		}
		return steps.get(destinationTrainStepInx);
	}
	
	public Stops getFirstStop(){
		TrainStep first = steps.get(0);
		
		if(first.getStopSequence() == 1){
			return first.getStop();
		}
		
		return null;
	}

	//TODO: ignore the fact that San Mateo is gonna be the first next stop.
	public TrainStep getNextStopTowardDestination(Stops destination, Date date) throws ParseException{
		
		long myTime = Utils.getTimeToDate(Utils.dateToHHmmss(date)).getTime();
		
		TrainStep previous =null;
		
		int i;
		TrainStep boardingStep = null;
		boolean isNextStopDestination = false;
		for(i = 0; i < steps.size() ;i++){
			TrainStep current =steps.get(i);
			
			if(previous == null){
				if(myTime <= current.getDepartDate()){
					boardingStep= current;
					break;
				}
				previous = current;
				continue;
			}
			isNextStopDestination = current.getStop().getStopId().equals(destination.getStopId()) ? true : false;
			//irrespective of time, if next stop is destination, we can catch this train. abort
			if(isNextStopDestination){
				return null;
			}
			//if next is not destination and we are between arriavel and departure, catch at this stop
			if( myTime > previous.getDepartDate() && myTime <= current.getArrivateDate()){
				boardingStep= current;
				break;
			}
			
			previous = current;
		}

		//check if the trains stops at destination
		for(;i < steps.size();i++){
			TrainStep current =steps.get(i);
			if(current.getStop().getStopId().equals(destination.getStopId())){
				return boardingStep;
			}
		}
		return null;
	}
	
	
	
	public TrainStep getDestinationStep2(Stops board, Stops dest){

		boolean isBoard = false, isDest = false;
		int i;
		for(i = 0; i < steps.size() ;i++){
			TrainStep current =steps.get(i);
			if(!isBoard && current.getStop().equals(board)){
				isBoard = true;
			}
			
			//destination cannot come before boarding
			if(isDest && isBoard){
				return null;
			}
			if(!isDest && current.getStop().equals(dest)){
				isDest = true;
			}
			if(isDest && isBoard){
				return current;
			}
		}
		return null;
		
		
	}
	
	
	public boolean doesStopAndDestinationExists(Stops board, Stops dest){
		if(getDestinationStep2(board, dest)!=null){
			return true;
		}
		return false;
	}

	public String getUniqueTripId() {
		return uniqueTripId;
	}

	
}
