package model.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.GameData;
import game.data.GameDataBackpack;
import game.data.GameDataClick;
import model.Item;
import model.ItemRepository;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.legendary.Axe;
import model.map.eventManager.LinkedEvent;

public final class Treasure implements Room {
	private int floor;
	final private List<XY> accessible = new ArrayList<>();
	final private HashSet<Item> rewardList = new HashSet<>();
	private boolean alreadyVisited = false;
	private LinkedEvent event;

	/**
	 * Constructor for the trasure
	 * 
	 * @param floor2
	 */
	public Treasure(int floor2) {
		floor = floor2;
		createReward();
		event = new LinkedEvent(floor, "treasure");
	}

	/**
	 * Getter for events
	 * 
	 * @return LinkedEvent
	 */
	public LinkedEvent getEvent() {
		return event;
	}

	/**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
	public List<XY> getAccessible() {
		return accessible;
	}

	/**
	 * Getter for rewardList
	 * 
	 * @return HashSet<Item>
	 */
	public HashSet<Item> getRewardList() {
		return rewardList;
	}

	/**
	 * Getter for alreadyVisited
	 * 
	 * @return boolean
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}

	/**
	 * Now, alreadyVisited is "true"
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}

	/**
	 * Open the treasure
	 */
	public void openReward() {
		for (var item : rewardList) {
			GameDataClick.addDragItem(item);
		}
	}

	/**
	 * Create the rewards that will be in the treasure
	 */
	private void createReward() {
		Random rand = new Random();
		rewardList.add(new Gold(rand.nextInt(20) + 10)); // Gold between 10 and 30
		rewardList.add(new KeyDoor()); // add a key
		// add 1, 2 or 3 random item (probability with Gauss)
		for (int i = 1; i <= 3; i++) {
			IO.println(rewardList);
			var item = RandomItem.generate(floor);
			rewardList.add(item);
		}
		IO.println(rewardList);
	}
}