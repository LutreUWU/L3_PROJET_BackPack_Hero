package model.map;

import java.util.List;

import model.XY;

public sealed interface Room permits Exit, EnemyRoom, EventRoom, Hallway, Healer, LockedDoor, Shop, Start, Treasure {
	public abstract List<XY> getAccessible();
	public void addAccessible(XY coord);
}
