package model.map;

import java.util.ArrayList;
import java.util.List;

import model.EnemyRepository;
import model.XY;
import model.monster.Enemy;

/**
 * Represents a room containing enemies in the game. Each EnemyRoom has a list
 * of accessible coordinates and a list of enemies. It also keeps track of
 * whether it has already been visited.
 */
public final class EnemyRoom implements Room {

	/** Indicates whether the room has already been visited. */
	private boolean alreadyVisited = false;

	/** List of accessible coordinates within the room. */
	private final List<XY> accessible = new ArrayList<>();

	/** List of enemies in this room. */
	private List<Enemy> enemyLst = new ArrayList<>();

	/**
	 * Constructs an EnemyRoom for a given floor.
	 *
	 * @param floor the floor number used to generate enemies in the room
	 */
	public EnemyRoom(int floor) {
		enemyLst = EnemyRepository.createEnemyLst(floor);
	}

	@Override
	public List<XY> getAccessible() {
		return List.copyOf(accessible);
	}

	@Override
	public void addAccessible(XY coord) {
		accessible.add(coord);
	}

	/**
	 * Checks whether the room has already been visited.
	 *
	 * @return true if the room has been visited, false otherwise
	 */
	public boolean getAlreadyVisited() {
		return alreadyVisited;
	}

	/**
	 * Marks this room as visited.
	 */
	public void nowVisited() {
		alreadyVisited = true;
	}

	/**
	 * Returns the list of enemies in this room. Note: Modifying the returned list
	 * will modify the room's internal enemy list.
	 *
	 * @return the list of enemies
	 */
	public List<Enemy> getLstEnemy() {
		return enemyLst;
	}

}
