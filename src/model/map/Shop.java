package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public final class Shop implements Room {
  int floor;
  private List<XY> accessible = new ArrayList<>();

  public Shop(int floor2) {
      floor = floor2;
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
  
}