package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public final class Exit implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	
	public Exit(int floor) {
		this.floor = floor;
	}
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
}
