package model.EventManager;
import game.GameData;
import model.Hero;

import java.util.Random;

public class Consequence {
	private String consequence_string;
	private boolean is_good;
	private int floor;
	
	final private int GOOD_COUNT = 2;
	final private int BAD_COUNT = 2;
	
	public Consequence(int floor, boolean is_good) {
		this.is_good = is_good;
		this.floor = floor;
	}
	
	public void apply(Hero hero) {
		if (is_good) applyGood(hero);
		else applyBad(hero);
	}
	
	private void applyGood(Hero hero) {
		Random rand = new Random();
		var x = rand.nextInt(GOOD_COUNT) + 1;
		switch (x) {
			case 1 -> {consequence_string = "Très bonne réponse !\nVoici de l'or (" + floor*5 + ") !";
			  				hero.add("gold", floor * 5);}
			case 2 -> {consequence_string = "J'en étais sûre que tu étais d'accord avec moi ! Tu mérites bien une arme!";
								}// Ajouter l'arme
		}
	}
	
	private void applyBad(Hero hero) {
		Random rand = new Random();
		var x = rand.nextInt(BAD_COUNT) + 1;
		switch (x) {
			case 1 -> {consequence_string = "T'es complètement marteau ? *Vous vous battez et perdez de la vie*";
								hero.sub("hp", floor * 2);
								if (hero.getHP() <= 0) hero.setHP(1);}
			case 2 -> {consequence_string = "Ca va pas la tête ? *On te force à mettre linux (aucun effet c'est gratuit)*";
								}
		}
	}
	
	public String getConsequenceString() {
		return consequence_string;
	}
}
