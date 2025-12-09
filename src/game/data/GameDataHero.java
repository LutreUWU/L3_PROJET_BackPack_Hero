package game.data;

import java.util.Objects;

import model.Hero;


/**
 * The game data with all methods for hero manipulation. 
 * Link to the hero from GameData
 * 
 */
public class GameDataHero {  
  private static Hero hero;
  /**
   * Link the hero with the one in GameData
   * @param heroData Hero's data from GameData
   * @throws Objects.requireNonNull if no hero is initialize
   */
  public GameDataHero(Hero heroData) {
  	Objects.requireNonNull(heroData);
    hero = heroData;
  }

  /**
   * Add the value in a specific stat's hero that we have choose
   * 
   * @param string The stat's (HP, Shield ...)  we wants to add
   * @param value	 The value we wants to add 
   */
  public static void add(String string, int value) {
  	hero.add(string, value);
  }
  
  /**
   * Sub the value in a specific stat's hero that we have choose
   * 
   * @param string The stat's (HP, Shield ...)  we wants to add
   * @param value	 The value we wants to add 
   */
  public static void sub(String string, int value) {
  	hero.sub(string, value);
  }
  
  /**
   * Reset the value of the shield the hero has.
   * 
   */
  public static void reset() {
  	hero.reset();
  }
	   
  /**
   * Return the current hero's statue from data
   * @return
   */
  public static Hero hero() {
    return hero;
  }
}
