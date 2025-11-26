package model;

import java.util.Objects;

public class Hero {
  // Statistics of the hero
	private int max_HP = 40;
	private int HP = 40;
	private int current_protection = 0; // damage countered
	private int current_armor = 0; // as a percentage (between 0 and 100%)
	private int energy_point = 3;
	private int mana_point = 0;
	private Gold money = new Gold();
	
	private int xp = 0; // (between 0 and 10 + (level - 1) * 2)
	private int level = 1;
	
	// For graphic interface
	private int size_x = 150;
	private int size_y = (int) (size_x * 1.2);
	
	
	private int MAX_XP() {
		return 10 + (level - 1) * 2;
	}
	
	//==============================
  //   METHODS FOR ATTRIBUTES
  //==============================
	
	public void setPos(XY coord) {
		Objects.requireNonNull(coord);
	}
	
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
			case "gold" -> money.addGold(value);
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
			case "gold" -> money.subGold(value);
		}
	}
	
	public void reset() {
		/**
		 * Function to reset for some attribtues
		 * @param string : What we want to reset
		 */
		current_protection = 0;
		current_armor = 0;
		
	}
	
	
	
	// Getter
	public int getMax_HP() {
		return max_HP;
	}

	public int getHP() {
		return HP;
	}

	public int getCurrent_protection() {
		return current_protection;
	}

	public int getCurrent_armor() {
		return current_armor;
	}

	public int getEnergy_point() {
		return energy_point;
	}

	public int getMana_point() {
		return mana_point;
	}

	public int getGold() {
		return money.getGold();
	}

	public int getXp() {
		return xp;
	}
	
	public int getSizeX() {
		return size_x;
	}
	
	public int getSizeY() {
		return size_y;
	}

	public int getLevel() {
		return level;
	}

}
