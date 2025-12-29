package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;

public final class Start implements Room {
	final private List<XY> accessible = new ArrayList<>();
	
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
}
