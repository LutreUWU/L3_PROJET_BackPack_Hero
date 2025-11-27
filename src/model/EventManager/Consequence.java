package model.EventManager;
import game.GameData;
import java.util.Random;

public class Consequence {
	private String consequence_string;
	
	public Consequence(int floor, GameData data, boolean is_good) {
		if (is_good) allGoodConsequence(floor, data);
		else allBadConsequence(floor, data);
	}
	
	public String getConsequenceString() {
		return consequence_string;
	}
	
	private void goodConsequence1(int floor, GameData data) {
		consequence_string = "Très bonne réponse !\nVoici de l'or (" + floor*5 + ") !";
		data.hero().add("gold", floor * 5);
	}
	
	private void goodConsequence2(int floor, GameData data) {
		consequence_string = "J'en étais sûre que tu étais d'accord avec moi !\nTu mérites bien une arme!";
		// Ajouter l'arme
	}
	
	private void badConsequence1(int floor, GameData data) {
		consequence_string = "Ca va pas la tête ?\n*On te lance un ordinateur dessus (-" + floor * 3 + "hp)*";
		data.hero().sub("gold", floor * 3);
	}
	
	private void badConsequence2(int floor, GameData data) {
		consequence_string = "T'es complètement marteau ?\n*Vous vous battez (-" + floor * 2 + " energy)*";
		data.hero().sub("energy", floor * 2);
	}
	
	public void allGoodConsequence(int floor, GameData data) {
		Random rand = new Random();
		var x = rand.nextInt(2) + 1;
		switch(x) {
			case 1 -> goodConsequence1(floor, data);
			case 2 -> goodConsequence2(floor, data);
		}
	}
	
	public void allBadConsequence(int floor, GameData data) {
		Random rand = new Random();
		var x = rand.nextInt(2) + 1;
		switch(x) {
			case 1 -> badConsequence1(floor, data);
			case 2 -> badConsequence2(floor, data);
		}
	}
}
