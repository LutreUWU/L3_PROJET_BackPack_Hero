package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.monster.Chicken;
import model.monster.Enemy;

public final class EnemyRoom implements Room {
	int floor;
	
	private List<XY> accessible = new ArrayList<>();

  public EnemyRoom(int floor2) {
      floor = floor2;
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
}
