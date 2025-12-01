package model.EventManager;
import game.GameData;
import game.data.GameDataBackpack;
import game.data.GameDataClick;
import model.Hero;
import model.Item;
import model.weapon.Axe;

import java.util.ArrayList;
import java.util.Random;

public class Consequence {
	private int floor;
	private double bonus; // OR *MALUS* !
	private String id_consequence;
	
	public Consequence(String id_consequence2, int floor2, double bonus2) {
		floor = floor2;
		id_consequence = id_consequence2;
		bonus = bonus2;
	}
	
	public void applyConsequence(Hero hero, ArrayList<Item> item_list) {
		switch(id_consequence) {
			case "sub_hp" -> conseqSubHP(hero);
			case "add_gold" -> hero.add("gold", (int) (floor * 5 * bonus));
			case "add_weapon" -> GameDataClick.add_item(new Axe());
			default -> {} // Nothing
		}
	}
	
	private void conseqSubHP(Hero hero) {
		hero.sub("hp", (int) (floor * 3 * bonus));
		if (hero.getHP() <= 0) hero.setHP(1);
	}
	
	
	
}
