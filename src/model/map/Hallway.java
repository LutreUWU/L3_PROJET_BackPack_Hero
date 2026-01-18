package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

/**
 * Represents a hallway in the floor grid. A hallway is a basic room that
 * connects to other rooms.
 */
public final class Hallway implements Room {
	/**
	 * List of coordinates of rooms that can be accessed from this hallway.
	 */
	private final List<XY> accessible = new ArrayList<>();

	@Override
	public List<XY> getAccessible() {
		return accessible;
	}

	@Override
	public void addAccessible(XY coord) {
		accessible.add(coord);
	}
}
