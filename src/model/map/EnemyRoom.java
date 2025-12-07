package model.map;

import java.util.ArrayList;
import java.util.List;

import model.XY;
import model.monster.Chicken;
import model.monster.Enemy;

public final class EnemyRoom implements Room {
	private boolean alreadyVisited = false;

	final private List<XY> accessible = new ArrayList<>();

	/**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
	public List<XY> getAccessible() {
		return accessible;
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

}
