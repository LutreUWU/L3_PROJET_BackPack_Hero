package model.map;
import model.EventManager.*;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import model.Hero;
import model.XY;

public class EventRoom implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	private LinkedEvent event;
	private boolean alreadyVisited = false;

  public EventRoom(int floor, Hero hero) {
      this.floor = floor;
      event = new LinkedEvent(floor, hero);
  }
  
  public LinkedEvent getEvent() {
  	return event;
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public void visitedEvent() {
		alreadyVisited = true;
	}
	
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}
}
