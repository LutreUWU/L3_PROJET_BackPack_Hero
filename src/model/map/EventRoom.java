package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

/**
 * Represents a room containing an event in the game. Each EventRoom has a list
 * of accessible coordinates, a LinkedEvent, and keeps track of whether it has
 * already been visited.
 */
public final class EventRoom implements Room {

	/** The floor where this room is located. */
	private int floor;

	/** List of accessible coordinates within the room. */
	private final List<XY> accessible = new ArrayList<>();

	/** The event associated with this room. */
	private LinkedEvent event;

	/** Indicates whether the room has already been visited. */
	private boolean alreadyVisited = false;

	/**
	 * Constructs an EventRoom for a given floor.
	 *
	 * @param floor2 the floor number where the room is located
	 */
	public EventRoom(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor, "eventRoom");
	}

	/**
	 * Returns the event associated with this room.
	 *
	 * @return the LinkedEvent of this room
	 */
	public LinkedEvent getEvent() {
		return event;
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
	 * Marks this room as visited.
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}

	/**
	 * Checks whether the room has already been visited.
	 *
	 * @return true if the room has been visited, false otherwise
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}
}
