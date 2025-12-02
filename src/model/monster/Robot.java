package model.monster;

import java.util.List;
import java.util.Random;

import game.data.GameDataHero;

/**
 * Class of a Chicken
 */
public class Robot implements Enemy{
	/**
	 * - HP :				 	When it reach 0, the enemy die
	 * - Shield : 		Can mitigate damage received
	 * - xp :					XP he drops when he die
	 * - lst_attack : List of all attack the enemy has 
	 * - action : 		To register which action the enemy will do next turn
	 */
	private int HP = 50;
	private int shield = 0;
	private final int xp = 20;
	private final List<String> lst_attack = List.of("Hoo...", "HOHOHOH", "HEHEHEH");
	private String action;
	// For graphism
	private final String img = "robot";
	private final double sizeY = 1.2;
	private final double sizeX = 1.2;

	
	/**
	 * Chose randomly an action between all attacks the enemy has.
	 * 
	 * @return Action he'll do 
	 */
	@Override
	public String preAction() {
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
			case "Hoo..." -> this.subHP(3);
			case "HOHOHOH" -> GameDataHero.sub("HP", 15);
			case "HEHEHEH" -> shield = 20;
		}
		preAction();
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
	public String getImg() {
		return img;
	}
	
	@Override
	public double getSizeX() {
		return sizeX;
	}
	
	@Override
	public double getSizeY() {
		return sizeY;
	}
	
	@Override
	public String toString() {
		return "Poulet malicieux";
	}
}
