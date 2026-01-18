package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import model.monster.Chicken;
import model.monster.Crabe;
import model.monster.Enemy;
import model.monster.Gnome;
import model.monster.Robot;
import model.monster.Soldat;

/**
 * Repository class for all enemies and bosses in the game.
 * 
 * This class stores and manages lists of normal enemies and boss enemies.
 * It provides methods to initialize the repository, retrieve enemies, and
 * generate enemy lists for a given floor level.
 */
public class EnemyRepository {
	
	/**
	 * Default constructor that does nothing
	 */
	public void enemyRepository() {}
  /** List of all normal enemies, sorted by HP */
  private static List<Enemy> enemyLst = new ArrayList<>();
  /** List of all bosses, sorted by HP */
  private static List<Enemy> bossLst = new ArrayList<>();
  
  /**
   * Initializes the enemy repository.
   * If the lists are empty, it registers all normal enemies and bosses.
   */
  public static void createEnemyRepository() {
  	if (enemyLst.size() == 0 && bossLst.size() == 0) {
  		registerAllEnemy();
    	registerAllBoss();
  	}	
  }
  
  /**
   * Registers all normal enemies into the repository.
   * The enemies are sorted by HP.
   */
  private static void registerAllEnemy() {
  	registerEnemy(new Chicken());
	  registerEnemy(new Gnome());
	  registerEnemy(new Soldat());
	  enemyLst = enemyLst.stream().sorted(Comparator.comparing(Enemy::getHP)).toList();
  }
  
  /**
   * Registers all bosses into the repository.
   * The bosses are sorted by HP.
   */
  private static void registerAllBoss() {
  	registerBoss(new Robot());
	  registerBoss(new Crabe());
	  bossLst = bossLst.stream().sorted(Comparator.comparing(Enemy::getHP)).toList();
  }
  
  /**
   * Adds a single boss to the boss list.
   * 
   * @param enemy the boss enemy to register
   */
  private static void registerBoss(Enemy enemy) {
  	bossLst.add(enemy);
  }
  
  /**
   * Adds a single normal enemy to the enemy list.
   * 
   * @param enemy the enemy to register
   */
  private static void registerEnemy(Enemy enemy) {
  	enemyLst.add(enemy);
  }
  
  /**
   * Returns a copy of the list of all bosses.
   * 
   * @return immutable list of bosses
   */
  public static List<Enemy> getBossLst() {
		return List.copyOf(bossLst);
	}
  
  /**
   * Chooses an enemy index from a given list based on floor level.
   * 
   * @param allEnemy 	The list of enemies to choose from
   * @param floor 		Current floor level
   * @param random 		Random generator
   * @return index of the chosen enemy in the list
   */
  private static int chooseEnemy(List<Enemy> allEnemy, int floor, Random random) {
		var enemyStrength = (int) random.nextGaussian(floor, 1);
		if (enemyStrength >= allEnemy.size()) enemyStrength = allEnemy.size() -1;
		if (enemyStrength < 0) enemyStrength = 0;
		return enemyStrength;
	}
	
  /**
   * Creates a list of unique enemies for a given floor.
   * The number of enemies equals the floor number.
   * 
   * @param  floor Current floor (must be below or equal 3)
   * @return list of enemies for this floor
   * @throws IllegalArgumentException if floor above 3
   */
	public static List<Enemy> createEnemyLst(int floor) {
		if (floor > 3) throw new IllegalArgumentException("FLOOR > 3 !");
		Set<Enemy> differentEnemy = new HashSet<>();
		List<Enemy> enemies = new ArrayList<>();
		var random = new Random();
		while (differentEnemy.size() != floor) {
			var enemyIndex = chooseEnemy(enemyLst, floor, random);
			var enemy = enemyLst.get(enemyIndex);
			if (!differentEnemy.contains(enemy)) {
				enemies.add(enemy);
				differentEnemy.add(enemy);
			}
		}
		return enemies;
	}
	
	/**
   * Returns a single boss for a given floor using Gaussian selection.
   * 
   * @param floor Current floor (must be below or equal 3)
   * @return a boss enemy
   * @throws IllegalArgumentException if floor above 3
   */
  public static Enemy getOneBossLst(int floor) {
  	if (floor > 3) throw new IllegalArgumentException("FLOOR > 3 !");
  	var random = new Random();
  	var bossIndex = (int) random.nextGaussian(floor, 1);
  	if (bossIndex < 0) {
  		bossIndex = 0;
  	}
  	if (bossIndex >= bossLst.size()) {
  		bossIndex = bossLst.size() - 1;
  	}
		return bossLst.get(bossIndex);
	}
  
  /**
   * Returns a copy of the list of all normal enemies.
   * 
   * @return immutable list of enemies
   */
  public static List<Enemy> getEnemyLst() {
		return List.copyOf(enemyLst);
	}
}

