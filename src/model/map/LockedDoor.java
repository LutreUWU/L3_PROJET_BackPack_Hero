package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

/**
 * Represents a Locked Door room in the floor.
 * The hero must unlock it to pass through.
 */
public final class LockedDoor implements Room {
    /** Floor number where the room is located */
    private int floor;

    /** List of coordinates of rooms that can be accessed from this room */
    private final List<XY> accessible = new ArrayList<>();

    /** Indicates whether the door is locked */
    private boolean lock = true;

    /** Event associated with this room */
    private LinkedEvent event;

    /**
     * Constructor for the LockedDoor
     * 
     * @param floor2 floor number of the room
     */
    public LockedDoor(int floor2) {
        floor = floor2;
        event = new LinkedEvent(floor, "lockedDoor");
    }

    @Override
    public List<XY> getAccessible() {
        return accessible;
    }

    @Override
    public void addAccessible(XY coord) {
        accessible.add(coord);
    }

    /**
     * Unlocks the door so the hero can pass
     */
    public void unlock() {
        lock = false;
    }

    /**
     * Check if the door is locked
     * 
     * @return true if the door is locked, false otherwise
     */
    public boolean getLock() {
        return lock;
    }

    /**
     * Get the event associated with this room
     * 
     * @return LinkedEvent object
     */
    public LinkedEvent getEvent() {
        return event;
    }
}
