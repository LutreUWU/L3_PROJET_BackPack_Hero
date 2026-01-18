package model.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import game.data.GameDataClick;
import model.Item;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.map.eventManager.LinkedEvent;

/**
 * Represents a treasure room that contains rewards for the hero.
 * The hero can open the treasure to get items.
 */
public final class Treasure implements Room {

    /** Current floor number */
    private int floor;

    /** List of coordinates of rooms accessible from this room */
    private final List<XY> accessible = new ArrayList<>();

    /** Set of items that are the treasure rewards */
    private final HashSet<Item> rewardList = new HashSet<>();

    /** True if the treasure room has already been visited */
    private boolean alreadyVisited = false;

    /** Event associated with this room */
    private LinkedEvent event;

    /**
     * Constructor for the treasure room
     * 
     * @param floor2 current floor number
     */
    public Treasure(int floor2) {
        floor = floor2;
        createReward();
        event = new LinkedEvent(floor, "treasure");
    }

    /**
     * Getter for the event associated with the treasure
     * 
     * @return LinkedEvent
     */
    public LinkedEvent getEvent() {
        return event;
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
     * Getter for the rewards in this treasure
     * 
     * @return set of items
     */
    public HashSet<Item> getRewardList() {
        return rewardList;
    }

    /**
     * Checks if the room has been visited
     * 
     * @return true if visited, false otherwise
     */
    public boolean getAlreadyVisited() {
        return alreadyVisited;
    }

    /**
     * Mark the treasure room as visited
     */
    public void nowVisited() {
        alreadyVisited = true;
    }

    /**
     * Gives all items in the treasure to the hero
     */
    public void openReward() {
        for (var item : rewardList) {
            GameDataClick.addDragItem(item);
        }
    }

    /**
     * Creates the rewards for this treasure room
     */
    private void createReward() {
        Random rand = new Random();
        rewardList.add(new Gold(rand.nextInt(20) + 10)); // Gold between 10 and 30
        rewardList.add(new KeyDoor()); // add a key
        // add 3 random items based on floor probability
        for (int i = 1; i <= 3; i++) {
            var item = RandomItem.generate(floor);
            rewardList.add(item);
        }
    }
}
