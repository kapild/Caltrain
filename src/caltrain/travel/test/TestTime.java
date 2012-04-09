package caltrain.travel.test;

import java.text.ParseException;
import java.util.Calendar;

import javax.jws.Oneway;

import caltrain.Utils;

public class TestTime {

	
	
	public static void main(String argc[]) throws ParseException{
		String Sec = "00:00:00";
		String oneSec = "00:00:01";
		String twoSec = "00:00:30";

		long time = Utils.getTimeToDate(oneSec).getTime() - Utils.getTimeToDate(Sec).getTime();
		time=time/1000;
//		System.out.println(time);
		
//		System.out.println(Utils.getTimeToDate(twoSec).getTime()/1000);
//		System.out.println(Utils.dateToHHMMSSFormat(180000));
		
		long arrivateDate = Utils.getTimeToDate(Sec).getTime() ;
//		System.out.println(Utils.dateToHHMMSSFormat(arrivateDate));
		 
		
		 System.out.println(Utils.getTimeToArrival(1000));
		 
	}
}
