package caltrain.objects;

public class Route {

	private final String  routeId;
	private final String longName;
	private final String routeType;
	
	
	
	public Route(String routeId, String longName, String routeType){
		this.routeId = routeId;
		this.longName = longName;
		this.routeType = routeType;
	}
	
	
	public String getRouteId() {
		return routeId;
	}


	public String getLongName() {
		return longName;
	}


	public String getRouteType() {
		return routeType;
	}


	@Override
	public String toString(){
		return "routeId:" + routeId +
				", longName:" + longName +
				", routeType:" + routeType ;
								
 	}
	
}
