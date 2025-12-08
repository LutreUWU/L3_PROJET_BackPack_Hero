package model.map.eventManager;

import java.util.ArrayList;

import game.GameData;
import game.data.GameDataBackpack;
import game.data.GameDataClick;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.item.common.KeyDoor;
import model.item.superrare.Massue;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Treasure;

public class Consequence {
	private int floor;
	private double bonus; // OR *MALUS* !
	private String idConsequence;

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
		case "fight" -> {
			IO.println("FAUT QU'ON SE BATTE ICI !!!");
			data.newFloor();
		}
		case "key" -> consequenceKeyEvent(data);
		case "lifeAndGold" -> consequenceHealer(data);
		case "openTreasure" -> consequenceTreasure(data);
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
		case Treasure room -> room.openReward();
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
	 * apply consequence for the lockedDoor
	 * 
	 * @param data
	 */
	private void consequenceKeyEvent(GameData data) {
		if (data.bag().bagItemLst().contains(new KeyDoor())) {
			var heroPos = data.map().getHeroPos();
			switch (data.map().getGrid()[heroPos.y()][heroPos.x()]) {
			case LockedDoor room -> {
				room.unlock();
				var bag = data.bag();
				GameDataBackpack.removeItemFromBackpack(bag.bagItemLst().stream().filter(item -> item.getID() == 1).findFirst().get()); // Bien
				data.map().updateMap(heroPos);
			}
			default ->
				throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
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
