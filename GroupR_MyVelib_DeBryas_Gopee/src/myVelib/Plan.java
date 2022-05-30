package myVelib;
import java.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * a class that regroups all methods to compute plan for each ride.
 * i.e. start and end parking slots, stations, etc.
 *  
 * @author jehandebryas
 *
 */

public class Plan {
	private ParkingSlot startParkingSlot;
	private ParkingSlot endParkingSlot;
	private boolean goToBike;
	private MyVelib myVelib;
	
	public Plan(MyVelib myVelib) {
		this.myVelib = myVelib;
	}

	public ParkingSlot getStartParkingSlot() {
		return startParkingSlot;
	}

	public void setStartParkingSlot(ParkingSlot startParkingSlot) {
		this.startParkingSlot = startParkingSlot;
	}

	public ParkingSlot getEndParkingSlot() {
		return endParkingSlot;
	}

	public void setEndParkingSlot(ParkingSlot endParkingSlot) {
		this.endParkingSlot = endParkingSlot;
	}
	
	public void ClassicPlan(Location start, Location end, BicycleType type){
		
		/**
		 * cheking that location start and location end are valid
		 */
		if(start.getX()> myVelib.getSide()) {
			System.out.println("location out of the map !");
			return;
		}
		if(start.getY()> myVelib.getSide()) {
			System.out.println("location out of the map !");
			return;
		}
		if(end.getX()> myVelib.getSide()) {
			System.out.println("location out of the map !");
			return;
		}
		if(end.getY()> myVelib.getSide()) {
			System.out.println("location out of the map !");
			return;
		}
		if(start.getX()< 0) {
			System.out.println("location out of the map !");
			return;
		}
		if(start.getY()< 0) {
			System.out.println("location out of the map !");
			return;
		}
		if(end.getX()< 0) {
			System.out.println("location out of the map !");
			return;
		}
		if(end.getY()< 0) {
			System.out.println("location out of the map !");
			return;
		}
		
		/**
		 * looking for a start parking slot & station
		 */
		
		//import stations
		Map<UUID,Station> stationsMap = myVelib.getStations();
		ArrayList <Station> stationsList = new ArrayList<Station>(stationsMap.values());
		
		//compare stations in term of distance from starting point
		
		DistanceStationComparator comparator = new DistanceStationComparator(start); 
		Collections.sort(stationsList,comparator);
		
		
		labelStart:
		for(Station s : stationsList) {
			for(ParkingSlot pS : s.getParkingSlots()) {
				if(pS.getState()== ParkingSlotState.Bicycle && pS.getBicycle().getBicycleType() == type) {
					this.startParkingSlot = pS;
					break labelStart;
				}
			}
		}
		
		
		/**
		 * looking for end parking slot & station
		 */
		
		//compare stations in term of distance from ending point
		
		DistanceStationComparator comparator2 = new DistanceStationComparator(end); 
		Collections.sort(stationsList,comparator2);
		
		labelEnd:
		for(Station s : stationsList) {
			for(ParkingSlot pS : s.getParkingSlots()) {
				if(pS.getState()== ParkingSlotState.FreeToUse) {
					this.endParkingSlot = pS;
					break labelEnd;
				}
			}
		}
		
		//cheking that it is interesting to really using this bike instead of walking
		
		double walkingDistance = start.distance(end);
		double bikeWalkingDistance = start.distance(startParkingSlot.getStation().getLocation()) + 
				end.distance(endParkingSlot.getStation().getLocation());
		
		if(walkingDistance <= bikeWalkingDistance) {
			System.out.println("it is not interesting to take a bike for the user");
			this.goToBike = false;
		}if(startParkingSlot==null || endParkingSlot==null) {
			this.goToBike = false;
			System.out.println("no bike is available");
		}else {this.goToBike=true;}
		
		
	}
}
