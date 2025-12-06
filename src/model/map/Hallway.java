package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public final class Hallway implements Room {
	final private List<XY> accessible = new ArrayList<>();
	
	public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
}
