package model.map.eventManager;
import java.util.ArrayList;

import game.GameData;
import game.data.GameDataClick;
import model.Hero;
import model.Item;
import model.item.superrare.Massue;

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
			case "nothing" -> {}
			default -> {} // Nothing
		}
	}
	
	private void conseqSubHP(Hero hero) {
		hero.sub("hp", (int) (floor * 3 * bonus));
		if (hero.getHP() <= 0) hero.setHP(1);
	}
	
	
	
}
