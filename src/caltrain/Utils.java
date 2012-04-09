package caltrain;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Utils {

	
	static int DEBUG_LEVEL =1;
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmdd"); 
	public static SimpleDateFormat hhmmssFormat = new SimpleDateFormat("HH:mm:ss"); 
	public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;	
	public static final long TIME_PENALTY  =5 * 60 * 1000;
	public static final boolean isStopedTrain = true;
	public static void log(String message){
		
		log(message,1);
	}

	public static void log(String message, int level){
		
		if(DEBUG_LEVEL <= level){
			System.out.println(message);
		}
	}
	
	public static Date getDate(String yyyymmdd) throws ParseException{
		return dateFormat.parse(yyyymmdd);
	}
	
	public static Date getTimeToDate(String hhmmss) throws ParseException{
		return hhmmssFormat.parse(hhmmss);
	}
	
	public static String normText(String text){
		return text.replaceAll("\\\"", "");
	}
	
	public static boolean isWeekday(String yyymmdd) throws ParseException{
		return !isWeekend(yyymmdd);
	}
	
	public static boolean isWeekday(Date date) throws ParseException{
		return !isWeekend(date);
	}
	public static boolean isWeekend(String yyyymmdd) throws ParseException{
		return isWeekend(Utils.getDate(yyyymmdd));
		
	}
	public static boolean isWeekend(Date date) throws ParseException{
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(date);
		int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
		if(dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY){
			return true;
		}
		return false;
	}
	
	public static String dateToHHmmss(Date date){
		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(date);
		int hour  = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int sec = cal.get(Calendar.SECOND);
		
		String hhmmss = String.format("%02d", hour) + ":" + String.format("%02d", mm) + ":" 
				+ String.format("%02d", sec);

		return hhmmss;
	}
	
	
	public static String dateToHHMMSSFormat(long millisec){

		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTimeInMillis(millisec);
		
		return hhmmssFormat.format(cal.getTime());
	}
	
	public static String getTimeToArrival(long diff) throws ParseException{
		
		return dateToHHMMSSFormat ( getTimeToDate("00:00:00").getTime() + diff);
	}
	
	public static Date resetTimeTo4am(Date date){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
		c.set(Calendar.HOUR_OF_DAY, 4);
		return c.getTime();
	}
}
