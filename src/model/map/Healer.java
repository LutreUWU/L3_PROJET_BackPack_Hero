package model.map;

import java.util.ArrayList;
import java.util.List;

import model.Hero;
import model.XY;
import model.map.eventManager.LinkedEvent;

public final class Healer implements Room {
	private int floor;
	final private List<XY> accessible = new ArrayList<>();
	private boolean alreadyVisited = false;
	private LinkedEvent event;

	/**
	 * Constructor for the Healer
	 * 
	 * @param floor2
	 */
	public Healer(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor, "healerRoom");
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
	 * Set alreadyVisited = "true"
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}

	/**
	 * Getter for alreadyVisited
	 * 
	 * @return boolean
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
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