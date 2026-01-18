package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.map.eventManager.LinkedEvent;

/**
 * Represents a room that serves as an exit in the game.
 * Each Exit room has a list of accessible coordinates and an associated LinkedEvent.
 */
public final class Exit implements Room {

    /** The floor where this exit is located. */
    private int floor;

    /** List of accessible coordinates within the exit room. */
    private final List<XY> accessible = new ArrayList<>();

    /** The event associated with this exit room. */
    private LinkedEvent event;

    /**
     * Constructs an Exit room for a given floor.
     *
     * @param floor2 the floor number where the exit is located
     */
    public Exit(int floor2) {
        floor = floor2;
        event = new LinkedEvent(floor, "exitRoom");
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
     * Returns the event associated with this exit room.
     *
     * @return the LinkedEvent of this room
     */
    public LinkedEvent getEvent() {
        return event;
    }
}
