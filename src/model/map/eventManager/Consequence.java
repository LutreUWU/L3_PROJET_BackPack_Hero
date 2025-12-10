package model.map.eventManager;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import game.data.GameDataBackpack;
import game.data.GameDataClick;
import game.data.GameDataCombat;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.item.common.KeyDoor;
import model.item.superrare.Massue;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Treasure;
import model.monster.Chicken;
import model.monster.Enemy;
import model.monster.Soldat;

public class Consequence {
	final private int floor;
	final private double bonus; // OR *MALUS* !
	private String idConsequence = null;
	private ArrayList<Enemy> enemyList = null;

	/**
	 * Constructor for consequence
	 * 
	 * @param idConsequence2
	 * @param floor2
	 * @param bonus2
	 */
	public Consequence(String idConsequence2, int floor2, double bonus2) {
		floor = floor2;
		idConsequence = idConsequence2;
		bonus = bonus2;
	}
	
	/**
	 * Constructor for consequence (for fight)
	 * 
	 * @param enemyList2
	 * @param floor2
	 * @param bonus2
	 */
	public Consequence(ArrayList<Enemy> enemyList2, int floor2, double bonus2) {
		floor = floor2;
		bonus = bonus2;
		enemyList = enemyList2;
	}
	
	/**
	 * Can apply all consequence
	 * 
	 * @param data
	 */
	public void applyConsequence(GameData data) {
		var hero = data.hero();
		switch (idConsequence) {
		case "sub_hp" -> conseqSubHP(hero);
		case "add_gold" -> hero.add("gold", (int) (floor * 5 * bonus));
		case "add_weapon" -> GameDataClick.addDragItem(new Massue());
		case "key" -> consequenceKeyEvent(data);
		case "lifeExchangeGold" -> consequenceHealer(data);
		case "openTreasure" -> consequenceTreasure(data);
		case "floor" -> data.newFloor();
		case null -> GameDataCombat.startCombat(enemyList, data);
		default -> {
		} // Nothing
		}
	}

	/**
	 * apply consequence for the treasure
	 * 
	 * @param data
	 */
	private void consequenceTreasure(GameData data) {
		var heroPos = data.map().getHeroPos();
		switch (data.map().getGrid()[heroPos.y()][heroPos.x()]) {
		case Treasure room -> {
			room.openReward();
			room.nowVisited();
		}
		default ->
			throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
		}
	}

	/**
	 * apply consequence for the healer
	 * 
	 * @param data
	 */
	private void consequenceHealer(GameData data) {
		var hero = data.hero();
		if (hero.getGold() >= floor * 5) {
			hero.sub("gold", floor * 5);
			hero.add("hp", floor * 15);
			if (hero.getHP() > hero.getMax_HP())
				hero.setHP(hero.getMax_HP());
			var heroPos = data.map().getHeroPos();
			switch (data.map().getGrid()[heroPos.y()][heroPos.x()]) {
			case Healer room -> room.nowVisited();
			default ->
				throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
			}
		}
	}

	/**
	 * apply consequence for the lockedDoor (unlock door if the hero have a key)
	 * 
	 * @param data
	 */
	private void consequenceKeyEvent(GameData data) {
		for (var item : data.bag().bagItemLst()) {
			switch(item) {
			case KeyDoor key -> {
				var heroPos = data.map().getHeroPos();
				switch (data.map().getGrid()[heroPos.y()][heroPos.x()]) {
				case LockedDoor room -> {
					room.unlock();
					GameDataBackpack.removeItemFromBackpack(item);
					data.map().updateMap(heroPos);
					return ;
				}
				default ->
					throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
				}
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + item);
			}
		}
	}

	/*
	 * Sub HP and keep the hero alive
	 */
	private void conseqSubHP(Hero hero) {
		hero.sub("hp", (int) (floor * 3 * bonus));
		if (hero.getHP() <= 0)
			hero.setHP(1);
	}

}
