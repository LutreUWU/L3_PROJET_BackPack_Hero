package model.map.eventManager;

import java.util.ArrayList;

import game.GameData;
import game.data.GameDataClick;
import game.data.GameDataCombat;
import model.Curse;
import model.Hero;
import model.RandomItem;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.rare.Cookie;
import model.item.superrare.Massue;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Treasure;
import model.monster.Enemy;

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
		case "subHP" -> conseqSubHP(hero);
		case "addGold" -> GameDataClick.addDragItem(new Gold(floor * 5 * (int) bonus));
		case "addWeapon" -> GameDataClick.addDragItem(RandomItem.generate(data.floor()));
		case "key" -> consequenceKeyEvent(data);
		case "lifeExchangeGold" -> consequenceHealer(data);
		case "openTreasure" -> consequenceTreasure(data);
		case "floor" -> data.newFloor();
		case "curseInBag" -> GameDataClick.addDragItem(new Curse());
		case "curseNotInBag" -> {
			consequenceCurseEvent(data);
			GameDataCombat.setCurseEvent(false);
		}
		case "cookie" -> GameDataClick.addDragItem(new Cookie());
		case null -> {
			var heroPos = data.map().getHeroPos();
			switch(data.map().getGrid()[heroPos.y()][heroPos.x()]) {
			case Exit _ -> {
				data.newFloor();
			}
			default -> {}
			}
			GameDataCombat.startCombat(enemyList, data);
		}
		default -> {
		} // Nothing
		}
	}
	
	private void consequenceCurseEvent(GameData data) {
		data.hero().add("curse", 1);
		data.hero().sub("HP", data.hero().getCurseRefuse());
		if(data.hero().getHP() <= 0) {
			data.endGame();
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
		if (data.bag().getGoldInBag() >= floor * 5) {
			data.bag().fuseGoldInBag(floor * 5);
			hero.add("hp", floor * 15);
			if (hero.getHP() > hero.getMaxHP())
				hero.setHP(hero.getMaxHP());
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
			case KeyDoor _ -> {
				var heroPos = data.map().getHeroPos();
				switch (data.map().getGrid()[heroPos.y()][heroPos.x()]) {
				case LockedDoor room -> {
					room.unlock();
					data.bag().removeItemFromBackpack(item);
					data.map().updateMap(heroPos);
					return ;
				}
				default ->
					throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
				}
			}
			default -> {}
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
