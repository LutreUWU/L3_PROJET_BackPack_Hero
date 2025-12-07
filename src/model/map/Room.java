package model.map;

import java.util.List;

import model.XY;

public sealed interface Room permits Exit, EnemyRoom, EventRoom, Hallway, Healer, LockedDoor, Shop, Start, Treasure {
	public abstract List<XY> getAccessible();
	
	/**
	 * Adds rooms that is accessible from the others
	 * @param coord
	 */
	default public void addAccessible(XY coord){
		var accessible = getAccessible();
		accessible.add(coord);
	}
}
