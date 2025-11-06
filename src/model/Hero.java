package model;

public class Hero {
	// HP Stats
	private int max_HP = 40;
	private int HP = 40;
	
	private int current_protection = 0; // damage countered
	private int current_armor = 0; // as a pourcentage (0 - 100%)
	private int energy_point = 3;
	private int mana_point = 0;
	private int gold = 0;
	
	private int xp = 0; // (0 - 10 + (level - 1) * 2)
	private int level = 1;
	
	private int MAX_XP() {
		return 10 + (level - 1) * 2;
	}
	
	// Getter
	
	public int getEnergyPoint() {
		return energy_point;
	}
	
	public int getManaPoint() {
		return mana_point;
	}
	
	//==============================
  //   METHODS FOR ATTRIBUTES
  //==============================
	
	public void add(String string, int value) {
		/**
		 * Function to add for all attribtues
		 * @param string : What we want to add
		 * @param value : how many points to add
		 */
		switch(string.toLowerCase()) {
			case "max_hp" -> max_HP += value;
			case "hp" -> {HP += value;
										if (HP > max_HP) HP = max_HP;}
			case "protection" -> current_protection += value;
			case "armor" -> current_armor += value;
			case "energy" -> energy_point += value;
			case "mana" -> mana_point += value;
			case "gold" -> gold += value;
			case "xp" -> {xp += value; if (xp >= MAX_XP()) {xp -= MAX_XP(); level++;}}
		}
	}
	
	public void sub(String string, int value) {
		/**
		 * Function to substract for all attribtues
		 * @param string : What we want to substract
		 * @param value : how many points to substract
		 */
		switch(string.toLowerCase()) {
			case "max_hp" -> max_HP -= value;
			case "hp" -> {HP -= value;
										if (HP <= 0) IO.println("Faudra qu'on s'occupe de la mort\n");}
			case "protection" -> current_protection -= value;
			case "armor" -> current_armor -= value;
			case "energy" -> energy_point -= value;
			case "mana" -> mana_point -= value;
			case "gold" -> gold -= value;
		}
	}
	
	public void reset(String string) {
		/**
		 * Function to reset for some attribtues
		 * @param string : What we want to reset
		 */
		switch(string.toLowerCase()) {
			case "protection" -> current_protection = 0;
			case "armor" -> current_armor -= 0;
		}
	}
}
