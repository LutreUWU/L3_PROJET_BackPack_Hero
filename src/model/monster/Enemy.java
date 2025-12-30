package model.monster;

import java.util.Map;

import model.Effect;

/**
 * Interface for enemies in the game.
 */
public interface Enemy {
	void addEffect(Effect effect, int value);
	void updateEffects();
	Map<Effect, Integer> getEffects();
	/**
	 * Sub the current HP of the mob by it's value 
	 * 
	 * @param nb Value we wants to remove
	 */
	void subHP(int nb);
	
	/**
	 * Return the current HP of the enemy
	 * 
	 * @return (int) HP of the enemy
	 */
	int getHP();
	
	/**
	 * Return the current Shield of the enemy
	 * 
	 * @return (int) Shield of the enemy
	 */
	int getShield();
	
	/**
	 * Return the current action of the enemy
	 * 
	 * @return (String) action of the enemy
	 */
	String getAction();
	
	/**
	 * Choose randomly an attack from the enemy. 
	 * 
	 * @return String of the attack we choose
	 */
	String preAction();
	/**
	 * Apply the action the enemy will do.
	 * Since all enemy has different attacks, we do an interface
	 * 
	 */
	void action();
	
	EnemyInfo getInfo();
}
