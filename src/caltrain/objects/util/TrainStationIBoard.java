package caltrain.objects.util;

import java.text.ParseException;

import caltrain.Utils;
import caltrain.objects.Stops;
import caltrain.objects.TrainPath;
import caltrain.objects.TrainStep;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class TrainStationIBoard {

	public LatLng myLocation;
	
	public TrainStep baordingStep;
	public TrainPath path;
	public Stops destination;
	public TrainStep destinationStep ;
	
	public TrainStationIBoard (LatLng my, TrainStep baordingStep, TrainStep destinationStep, TrainPath path, Stops destination){
		myLocation = my;
		this.baordingStep = baordingStep;
		this.path= path;
		this.destination = destination;
		this.destinationStep = destinationStep;
	}
	
	
	public long getTime(){
		destinationStep = path.getDestinationStep(destination);
		return destinationStep.getArrivateDate() - baordingStep.getArrivateDate();
	}
	
	public double getDistance(){
		return LatLngTool.distance(myLocation, baordingStep.getStop().getLatLong(), LengthUnit.MILE);
	}
	
	
	public String toString(){
		
		try {
			return "\tMiles:" + String.format("%.02f", getDistance()) + "\n\tTime to travel:" + Utils.getTimeToArrival(getTime()) + "\n\tBoarding station:" + baordingStep.toString()
					+ "\n\tDestination station:" + Utils.dateToHHMMSSFormat(destinationStep.getArrivateDate());
		} catch (ParseException e) {
			return "null";
		}
	}
}
