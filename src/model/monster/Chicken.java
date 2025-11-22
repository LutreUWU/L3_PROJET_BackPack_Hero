package model.monster;

import java.util.List;
import java.util.Random;

import game.data.GameDataHero;

/**
 * Class of a Chicken
 */
public class Chicken implements Enemy{
	/**
	 * - HP :				 	When it reach 0, the enemy die
	 * - Shield : 		Can mitigate damage received
	 * - xp :					XP he drops when he die
	 * - lst_attack : List of all attack the enemy has 
	 * - action : 		To register which action the enemy will do next turn
	 */
	private int HP = 20;
	private int shield = 0;
	private int xp = 4;
	private List<String> lst_attack = List.of("Morsure", "Protection");
	private String action;
	// For graphism
	private String url_img = "data/monster/chicken.png";
	private double size = 0.4;
	
	/**
	 * Chose randomly an action between all attacks the enemy has.
	 * 
	 * @return Action he'll do 
	 */
	@Override
	public String pre_action() {
		Random randomNumbers = new Random();
		action = lst_attack.get(randomNumbers.nextInt(2));
		return action;
	}
	
	/**
	 * Apply the action the monster will do, and choose randomly the next action of the monster
	 */
	@Override
	public void action() {
		switch(action) {
			case "Morsure" -> GameDataHero.sub("HP", 3);
			case "Protection" -> shield += 2;
		}
		pre_action();
	}
	
	/**
	 * Sub the HP by the value
	 * If he has a shield, subtract it
	 * 
	 */
	@Override
	public void subHP(int value) {
		if(shield >= value) {
			shield -= value;
			return;
		}
		HP -= value - shield;
		shield = 0;
	}
	
	// Getter 
	@Override
	public int getHP() {
		return HP;
	}
	
	@Override
	public int getShield() {
		return shield;
	}
	
	@Override
	public String getAction() {
		return action;
	}
	
	@Override
	public int getXP() {
		return xp;
	}
	
	@Override
	public String getUrl() {
		return url_img;
	}
	
	@Override
	public double getSize() {
		return size;
	}
	
	@Override
	public String toString() {
		return "Poulet malicieux";
	}
}
