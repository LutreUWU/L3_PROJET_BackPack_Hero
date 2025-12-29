package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

public final class Exit implements Room {
	private int floor;
	final private List<XY> accessible = new ArrayList<>();
	private LinkedEvent event;

	/**
	 * Constructor for Exits
	 * 
	 * @param floor2
	 */
	public Exit(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor, "exitRoom");
	}

	/**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
	public List<XY> getAccessible() {
		return accessible;
	}
	
	/**
	 * Adds rooms that is accessible from the others
	 * @param coord
	 */
	@Override
	public void addAccessible(XY coord){
		accessible.add(coord);
	}

	/**
	 * Getter for events
	 * 
	 * @return LinkedEvent
	 */
	public LinkedEvent getEvent() {
		return event;
	}
}
