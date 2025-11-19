package monster;

/**
 * Interface for enemies in the game.
 */
public interface Enemy {
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
	String pre_action();
	
	/**
	 * Get the path to the enemy image
	 * 
	 * @return (String) path 
	 */
	String getUrl();
	
	/**
	 * Get the size of the enemy
	 * 
	 * @return (int) enemy
	 */
	double getSize();
	
	/**
	 * Get the XP drop by the enemy
	 * 
	 * @return (int) enemy
	 */
	int getXP();
	
	/**
	 * Apply the action the enemy will do.
	 * Since all enemy has different attacks, we do an interface
	 * 
	 */
	void action();
	
}
