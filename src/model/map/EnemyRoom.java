package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public class EnemyRoom implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();

  public EnemyRoom(int floor) {
      this.floor = floor;
  }
  
  public List<XY> get_accessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
}
