package model;

import java.util.ArrayList;
import java.util.List;

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
    	registerAllEnemy();
    	registerAllBoss();
    	
    }
    
    
    private static void registerAllEnemy() {
    	registerEnemy(new Chicken());
  	  registerEnemy(new Gnome());
  	  registerEnemy(new Soldat());
    }
    
    
    private static void registerAllBoss() {
    	registerBoss(new Robot());
  	  registerBoss(new Crabe());
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
    
    public static Enemy getOneBossRankLst(int floor) {
    	var bossNb = floor / 3;
    	if (bossNb >= bossLst.size()) bossNb = bossLst.size() - 1;
			return bossLst.get(bossNb);
		}
    
    public static List<Enemy> getEnemyLst() {
			return List.copyOf(enemyLst);
		}
}

