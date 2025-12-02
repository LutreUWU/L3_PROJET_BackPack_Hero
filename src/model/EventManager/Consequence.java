package model.EventManager;
import java.util.ArrayList;

import game.data.GameDataClick;
import model.Hero;
import model.Item;
import model.weapon.Axe;

public class Consequence {
	private int floor;
	private double bonus; // OR *MALUS* !
	private String idConsequence;
	
	public Consequence(String idConsequence2, int floor2, double bonus2) {
		floor = floor2;
		idConsequence = idConsequence2;
		bonus = bonus2;
	}
	
	public void applyConsequence(Hero hero, ArrayList<Item> itemLst) {
		switch(idConsequence) {
			case "sub_hp" -> conseqSubHP(hero);
			case "add_gold" -> hero.add("gold", (int) (floor * 5 * bonus));
			case "add_weapon" -> GameDataClick.addDragItem(new Axe());
			default -> {} // Nothing
		}
	}
	
	private void conseqSubHP(Hero hero) {
		hero.sub("hp", (int) (floor * 3 * bonus));
		if (hero.getHP() <= 0) hero.setHP(1);
	}
	
	
	
}
