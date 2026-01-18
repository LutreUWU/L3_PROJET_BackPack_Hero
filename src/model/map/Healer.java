package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

/**
 * Represents a Healer room in the floor. The hero can interact with this room
 * for healing.
 */
public final class Healer implements Room {
	/** Floor number where the room is located */
	private int floor;

	/** List of coordinates of rooms that can be accessed from this room */
	private final List<XY> accessible = new ArrayList<>();

	/** Indicates whether the hero has already visited this room */
	private boolean alreadyVisited = false;

	/** Event associated with the room */
	private LinkedEvent event;

	/**
	 * Constructor for the Healer room
	 * 
	 * @param floor2 floor number of the room
	 */
	public Healer(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor, "healerRoom");
	}

	@Override
	public List<XY> getAccessible() {
		return accessible;
	}

	@Override
	public void addAccessible(XY coord) {
		accessible.add(coord);
	}

	/**
	 * Mark the room as visited
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}

	/**
	 * Check if the room has already been visited
	 * 
	 * @return true if visited, false otherwise
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}

	/**
	 * Get the event associated with this room
	 * 
	 * @return LinkedEvent object
	 */
	public LinkedEvent getEvent() {
		return event;
	}
}
