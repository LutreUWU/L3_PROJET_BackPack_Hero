package model.map;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import model.Backpack;
import model.XY;
import model.map.eventManager.LinkedEvent;

public final class LockedDoor implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	private boolean lock = true;
	private LinkedEvent event;

  public LockedDoor(int floor2) {
      floor = floor2;
      event = new LinkedEvent(floor, "lockedDoor");
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public void unlock() {
		lock = false;
	}
	
	public boolean getLock() {
		return lock;
	}
	
	public LinkedEvent getEvent() {
		return event;
	}
}
