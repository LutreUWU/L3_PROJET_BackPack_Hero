package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

/**
 * Represents the starting room of the floor.
 * The hero starts the game from this room.
 */
public final class Start implements Room {
	
	/**
	 * Default constructor that does nothing
	 */
	public void start() {}
	
  /** List of coordinates of rooms that can be accessed from this room */
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
