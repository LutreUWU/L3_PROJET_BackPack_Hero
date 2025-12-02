package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public final class LockedDoor implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	private boolean lock = true;

  public LockedDoor(int floor) {
      this.floor = floor;
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
}
