package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.EnemyRepository;
import model.XY;
import model.monster.Chicken;
import model.monster.Enemy;

public final class EnemyRoom implements Room {
	private boolean alreadyVisited = false;

	final private List<XY> accessible = new ArrayList<>();
	final private List<Enemy> enemyLst= new ArrayList<>();
	
	public EnemyRoom(int floor) {
		createEnemyLst(floor);
	}
	
	private void createEnemyLst(int floor) {
		var allEnemy = EnemyRepository.getEnemyRankLst();
		var random = new Random();
		var nbEnemy = (int) random.nextGaussian(floor, 0.5);
		var enemyStrength = (int) random.nextGaussian(floor, 1);
		if (nbEnemy < 1) nbEnemy = 1;
		if (nbEnemy > 3) nbEnemy = 3;
		for (int i = 0; i < nbEnemy; i++) {
			enemyLst.add(allEnemy.get(nbEnemy));
		}
	}
	
	/**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
	public List<XY> getAccessible() {
		return List.copyOf(accessible);
	}
	
	/**
	 * Adds rooms that is accessible from the others
	 * @param coord
	 */
	@Override
	public void addAccessible(XY coord){
		accessible.add(coord);
	}

	/**
	 * Getter for alreadyVisited
	 * 
	 * @return boolean
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}

	/**
	 * Set nowVisited = true
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}
	
	public List<Enemy> getLstEnemy() {
		return enemyLst;
	}

}
