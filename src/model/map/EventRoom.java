package model.map;
import java.util.ArrayList;
import java.util.List;

import game.GameData;
import model.Hero;
import model.XY;
import model.map.eventManager.*;

public final class EventRoom implements Room {
	private int floor;
	final private List<XY> accessible = new ArrayList<>();
	private LinkedEvent event;
	private boolean alreadyVisited = false;
	
	/**
	 * Constructor for EventRooms
	 * @param floor2
	 */
  public EventRoom(int floor2) {
      floor = floor2;	
      event = new LinkedEvent(floor, "eventRoom");
  }
  
  /**
   * Getter for events
   * @return LinkedEvent
   */
  public LinkedEvent getEvent() {
  	return event;
  }
  
  /**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
  public List<XY> getAccessible(){
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
   * Set alreadyVisited = true 
   */
	public void visitedEvent() {
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
}
