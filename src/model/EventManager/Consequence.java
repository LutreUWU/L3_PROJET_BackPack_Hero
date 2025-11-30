package model.EventManager;
import game.GameData;
import model.Hero;

import java.util.Random;

public class Consequence {
	private String final_answer;
	private String consequence_string;
	private boolean is_good;
	private int floor;
	private double bonus; // OR **MALUS**
	
	final private int GOOD_COUNT = 2;
	final private int BAD_COUNT = 2;
	
	public Consequence(int floor, boolean is_good, double bonus, String final_answer) {
		this.is_good = is_good;
		this.floor = floor;
		this.bonus = bonus;
		this.final_answer = final_answer;
	}
	
	public void apply(Hero hero) {
		if (is_good) applyGood(hero);
		else applyBad(hero);
	}
	
	private void applyGood(Hero hero) {
		Random rand = new Random();
		var x = rand.nextInt(GOOD_COUNT) + 1;
		switch (x) {
			case 1 -> {consequence_string = final_answer + "\nVoici de l'or (" + (int) (floor*5*bonus) + ") !";
			  				hero.add("gold", (int) (floor * 5 * bonus));}
			case 2 -> {consequence_string = final_answer + "\nTu mérites bien une arme!";
								}// Ajouter l'arme et raojuter le bonus
		}
	}
	
	private void applyBad(Hero hero) {
		Random rand = new Random();
		var x = rand.nextInt(BAD_COUNT) + 1;
		switch (x) {
			case 1 -> {consequence_string = final_answer + "\n*Vous vous battez et perdez de la vie*";
								hero.sub("hp", (int) (floor * 2 * bonus));
								if (hero.getHP() <= 0) hero.setHP(1);}
			case 2 -> {consequence_string = final_answer + "\n*On te force à mettre linux (aucun effet c'est gratuit)*";
								}
		}
	}
	
	public String getConsequenceString() {
		return consequence_string;
	}
}
