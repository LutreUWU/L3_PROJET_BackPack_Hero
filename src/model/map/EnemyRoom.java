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
	private List<Enemy> enemyLst= new ArrayList<>();
	
	public EnemyRoom(int floor) {
		enemyLst = EnemyRepository.createEnemyLst(floor);
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
