package model.map;

import java.util.ArrayList;
import java.util.List;

import model.Hero;
import model.XY;
import model.map.eventManager.LinkedEvent;

public final class Healer implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	private boolean alreadyVisited = false;
	private LinkedEvent event;

	public Healer(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor2, "healerRoom");
	}
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public void nowVisited() {
		alreadyVisited = true;
	}
	
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}
	
	public LinkedEvent getEvent() {
		return event;
	}
	
}