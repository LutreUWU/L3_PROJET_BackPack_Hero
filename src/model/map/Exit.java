package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

public final class Exit implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	private LinkedEvent event; 
	
	public Exit(int floor) {
		this.floor = floor;
		event = new LinkedEvent(floor, true);
	}
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public LinkedEvent getEvent() {
		return event;
	}
}
