package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.monster.Chicken;
import model.monster.Enemy;
import model.monster.Gnome;
import model.monster.Robot;
import model.monster.Soldat;


public class EnemyRepository {

    // Stocke les ennemies
    private static List<Enemy> enemyLst = new ArrayList<>();

    
    public static void createEnemyRepository() {
  	  registerEnemy(new Chicken());
  	  registerEnemy(new Gnome());
  	  registerEnemy(new Robot());
  	  registerEnemy(new Soldat());
    }
    
    // Méthode pour enregistrer une arme
    private static void registerEnemy(Enemy enemy) {
    	enemyLst.add(enemy);
    }
    
    public static List<Enemy> getEnemyLst() {
			return List.copyOf(enemyLst);
		}
    
    public static List<Enemy> getEnemyRankLst() {
			return List.copyOf(enemyLst);
		}
}

