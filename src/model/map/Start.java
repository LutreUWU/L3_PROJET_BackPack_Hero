package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public class Start implements Room {
	int floor;
	private List<XY> accessible = new ArrayList<>();
	
	public Start(int floor) {
		this.floor = floor;
	}
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
}
