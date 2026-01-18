package model.map;

import java.util.List;

import model.XY;

/**
 * Interface for all types of rooms on a floor.
 * Defines common behaviors for accessing neighboring rooms.
 */
public sealed interface Room permits Exit, EnemyRoom, EventRoom, Hallway, Healer, LockedDoor, Shop, Start, Treasure {

    /**
     * Get the list of coordinates for rooms that can be accessed from this room.
     * 
     * @return List of accessible coordinates
     */
    public abstract List<XY> getAccessible();

    /**
     * Add a coordinate to the list of accessible rooms from this room.
     * 
     * @param coord coordinate to add
     */
    public void addAccessible(XY coord);
}
