package model.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
	
	private int chooseNbEnemy(List<Enemy> allEnemy, int floor, Random random) {
		var enemyStrength = (int) random.nextGaussian(floor, 1);
		if (enemyStrength >= allEnemy.size()) enemyStrength = allEnemy.size() -1;
		if (enemyStrength < 0) enemyStrength = 0;
		return enemyStrength;
	}
	
	private void createEnemyLst(int floor) {
		Set<Enemy> differentEnemy = new HashSet<>();
		var allEnemy = EnemyRepository.getEnemyLst();
		var random = new Random();
		var nbEnemy = (int) random.nextGaussian(floor, 0.5);
		if (nbEnemy < 1) nbEnemy = 1;
		if (nbEnemy > 3) nbEnemy = 3;
		while (differentEnemy.size() != nbEnemy) {
			var enemyNb = chooseNbEnemy(allEnemy, floor, random);
			var enemy = allEnemy.get(enemyNb);
			if (!differentEnemy.contains(enemy)) {
				enemyLst.add(enemy);
				differentEnemy.add(enemy);
			}
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
