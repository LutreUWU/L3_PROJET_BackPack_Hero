package model.map.eventManager;
import java.util.ArrayList;

import game.GameData;
import game.data.GameDataClick;
import model.Hero;
import model.Item;
import model.item.common.KeyDoor;
import model.item.superrare.Massue;
import model.map.Healer;
import model.map.LockedDoor;

public class Consequence {
	private int floor;
	private double bonus; // OR *MALUS* !
	private String idConsequence;
	
	public Consequence(String idConsequence2, int floor2, double bonus2) {
		floor = floor2;
		idConsequence = idConsequence2;
		bonus = bonus2;
	}
	
	public void applyConsequence(GameData data) {
		var hero = data.hero();
		switch(idConsequence) {
			case "sub_hp" -> conseqSubHP(hero);
			case "add_gold" -> hero.add("gold", (int) (floor * 5 * bonus));
			case "add_weapon" -> GameDataClick.addDragItem(new Massue());
			case "fight" -> {IO.println("FAUT QU'ON SE BATTE ICI !!!"); data.newFloor();}
			case "key" -> consequenceKeyEvent(data);
			case "lifeAndGold" -> consequenceHealer(data);
			case "nothing" -> {}
			default -> {} // Nothing
		}
	}
	
	private void consequenceHealer(GameData data) {
		var hero = data.hero();
		hero.setHP(hero.getMax_HP()); 
		hero.add("gold", floor * 8);
		var heroPos = data.map().getHeroPos();
		switch(data.map().getGrid()[heroPos.y()][heroPos.x()]) {
			case Healer room -> room.nowVisited();
		default -> throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
		}
	}
	
	private void consequenceKeyEvent(GameData data) {
		if (data.bag().bagItemLst().contains(new KeyDoor())) {
			var heroPos = data.map().getHeroPos();
			switch(data.map().getGrid()[heroPos.y()][heroPos.x()]) {
				case LockedDoor room -> {room.unlock();
																var bag = data.bag().bagItemLst(); 
																bag.remove(bag.stream()
																							.filter(item -> item.getID() == 1)
																							.findFirst().orElse(null));
																data.map().updateMap(heroPos);}
				default -> throw new IllegalArgumentException("Unexpected value: " + data.map().getGrid()[heroPos.y()][heroPos.x()]);
			}
		}
	}
	
	private void conseqSubHP(Hero hero) {
		hero.sub("hp", (int) (floor * 3 * bonus));
		if (hero.getHP() <= 0) hero.setHP(1);
	}
	
	
	
}
