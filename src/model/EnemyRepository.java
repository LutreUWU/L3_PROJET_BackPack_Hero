package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import game.GameData;
import model.monster.Chicken;
import model.monster.Crabe;
import model.monster.Enemy;
import model.monster.Gnome;
import model.monster.Robot;
import model.monster.Soldat;


public class EnemyRepository {
    // Stocke les ennemies
    private static List<Enemy> enemyLst = new ArrayList<>();
    private static List<Enemy> bossLst = new ArrayList<>();
    
    public static void createEnemyRepository() {
    	if (enemyLst.size() == 0 && bossLst.size() == 0) {
    		registerAllEnemy();
      	registerAllBoss();
    	}
    	
    	
    }
    
    
    private static void registerAllEnemy() {
    	registerEnemy(new Chicken());
  	  registerEnemy(new Gnome());
  	  registerEnemy(new Soldat());
  	  enemyLst = enemyLst.stream().sorted(Comparator.comparing(Enemy::getHP)).toList();
    }
    
    
    private static void registerAllBoss() {
    	registerBoss(new Robot());
  	  registerBoss(new Crabe());
  	  bossLst = bossLst.stream().sorted(Comparator.comparing(Enemy::getHP)).toList();
    }
    
    private static void registerBoss(Enemy enemy) {
    	bossLst.add(enemy);
    }
    
    private static void registerEnemy(Enemy enemy) {
    	enemyLst.add(enemy);
    }
    
    public static List<Enemy> getBossLst() {
			return List.copyOf(bossLst);
		}
    
    private static int chooseEnemy(List<Enemy> allEnemy, int floor, Random random) {
  		var enemyStrength = (int) random.nextGaussian(floor, 1);
  		if (enemyStrength >= allEnemy.size()) enemyStrength = allEnemy.size() -1;
  		if (enemyStrength < 0) enemyStrength = 0;
  		return enemyStrength;
  	}
  	
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
    
    public static List<Enemy> getEnemyLst() {
			return List.copyOf(enemyLst);
		}
}

