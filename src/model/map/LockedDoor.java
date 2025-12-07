package model.map;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import model.Backpack;
import model.XY;
import model.map.eventManager.LinkedEvent;

public final class LockedDoor implements Room {
	private int floor;
	final private List<XY> accessible = new ArrayList<>();
	private boolean lock = true;
	private LinkedEvent event;

	/**
	 * Constructor for the LockedDoor
	 * 
	 * @param floor2
	 */
	public LockedDoor(int floor2) {
		floor = floor2;
		event = new LinkedEvent(floor, "lockedDoor");
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
	 * Unlock the LockedDoor
	 */
	public void unlock() {
		lock = false;
	}

	/**
	 * Getter to know if the LockedDoor is lock
	 * 
	 * @return
	 */
	public boolean getLock() {
		return lock;
	}

	/**
	 * Getter for event
	 * 
	 * @return LinkedEvent
	 */
	public LinkedEvent getEvent() {
		return event;
	}
}
