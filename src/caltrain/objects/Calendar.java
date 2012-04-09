package caltrain.objects;

import java.text.ParseException;
import java.util.Date;

import caltrain.Utils;

public class Calendar {

	
	private final String serviceId ;
	private final boolean[] table ;
	private final Date startDate;
	private final Date endDate;
	
	
	
	public Calendar(String [] tokens) throws ParseException{

		if(tokens.length !=10){
			new Exception("Expecting 10 values in Calendars.");
		}
	
		this.serviceId = Utils.normText(tokens[0]);
		table = new boolean [7];

		int i=1;
		for( ; i < 8;i++){
			table[i-1] = isTrue(Utils.normText(tokens[i]));
		}
		
		startDate = Utils.getDate(Utils.normText(tokens[i++]));
		endDate = Utils.getDate(Utils.normText(tokens[i++]));
		
	}
	
	public boolean isWeekend(){
		
		for(int i =5; i < 7;i++){
			if(table[i] == true){
				return true;
			}
		}
		return false;
	}
	
	public boolean isWeekday(){

		for(int i =0; i <5	;i++){
			if(table[i] == true){
				return true;
			}
		}
		return false;
		
	}
	
	private static boolean isTrue(String isTrue){
		if(isTrue.equals("1")){
			return true;
		}
		else return false;
	}

}
