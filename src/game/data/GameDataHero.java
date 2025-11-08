package game.data;

import java.util.Objects;

import item.Hero;


/**
 * The game data with all methods for hero manipulation. 
 * Link to the hero from GameData
 * 
 */
public class GameDataHero {  
  private static Hero hero;
  private static int max_HP, HP, current_protection, current_armor, energy_point, mana_point, gold, xp, level;
  /**
   * Link the hero with the one in GameData
   * @param data_hero Hero's data from GameData
   * @throws Objects.requireNonNull if no hero is initialize
   */
  public GameDataHero(Hero data_hero) {
	Objects.requireNonNull(hero);
    hero = data_hero;
    max_HP = data_hero.getMax_HP();
    HP = data_hero.getHP();
    current_protection = data_hero.getCurrent_protection();
    current_armor = data_hero.getCurrent_armor();
    energy_point = data_hero.getEnergy_point();
    mana_point = data_hero.getMana_point();
    gold = data_hero.getGold();
    xp = data_hero.getXp();
    level = data_hero.getLevel();
  }
	   
  /**
   * Return the current hero's statue from data
   * @return
   */
  public Hero hero() {
    return hero;
  }
	  
  private int MAX_XP() {
	return 10 + (level - 1) * 2;
  }  
  //==============================
  //   METHODS FOR HERO
  //==============================	
  
  /**
   * Function to add for all attribtues
   * @param string : What we want to add
   * @param value : how many points to add
   */
  public void add(String string, int value) {
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
