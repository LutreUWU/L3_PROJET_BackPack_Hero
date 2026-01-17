package model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.XY;

public class Floor {

	private final int ROW = 4; //5
	private final int COL = 9; // 11

	final private Room[][] grid = new Room[ROW][COL];

	final private HashSet<XY> heroVisited = new HashSet<>();
	final private HashSet<XY> heroAccessible = new HashSet<>();
	final private HashSet<XY> heroVisible = new HashSet<>();
	final private HashSet<XY> heroVisibleForLine = new HashSet<>();
	private XY heroPos;

	/**
	 * Constructor for the floor
	 * 
	 * @param floor
	 * @param hero
	 */
	public Floor(int floor) {
		XY start = createAllRoom(floor);
		HashSet<XY> visited = new HashSet<>();
		createWay(visited, start);
		heroPos = start;
		updateMap(start);
	}

	/**
	 * Setters for hero position
	 * 
	 * @param hero_pos
	 */
	public void setHeroPos(XY heroPos2) {
		heroPos = heroPos2;
	}

	/**
	 * Create all way between rooms
	 * 
	 * @param visited
	 * @param start
	 */
	private void createWay(HashSet<XY> visited, XY start) {
		List<XY> accessible = new ArrayList<>();
		addAcc(accessible, start.x(), start.y());
		for (var coord : accessible) {
			if (!visited.contains(coord)) {
				visited.add(coord);
				grid[start.y()][start.x()].addAccessible(coord);
				grid[coord.y()][coord.x()].addAccessible(start);
				createWay(visited, coord);
			}
		}
	}

	/**
	 * Add accessible room of each room
	 * 
	 * @param listacc
	 * @param x
	 * @param y
	 */
	private void addAcc(List<XY> listacc, int x, int y) {
		if (x > 0)
			listacc.add(new XY(x - 1, y));
		if (y > 0)
			listacc.add(new XY(x, y - 1));
		if (x < COL - 1)
			listacc.add(new XY(x + 1, y));
		if (y < ROW - 1)
			listacc.add(new XY(x, y + 1));
		Collections.shuffle(listacc);
	}

	/**
	 * Create All Room
	 * 
	 * @param floor
	 * @return Starter Room
	 */
	private XY createAllRoom(int floor) {
		List<XY> list1 = createXYList();
		List<XY> list2 = shuffleList(list1);
		createSpecialRoom(list2, floor);
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				if (grid[i][j] == null) {
					grid[i][j] = new Hallway();
				}
			}
		}
		return new XY(list2.get(8).x(), list2.get(8).y());
	}

	/**
	 * Create all special room
	 * 
	 * @param list
	 * @param floor
	 */
	private void createSpecialRoom(List<XY> list, int floor) {
		var index = createOneSpecialRoomOfEach(list, floor);
		for (int i = 0; i < (ROW * COL) / 60; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new Shop(floor); // Create shop
			grid[list.get(index).y()][list.get(index++).x()] = new Healer(floor); // Create Healer
			grid[list.get(index).y()][list.get(index++).x()] = new EventRoom(floor); // Create Event
		}
		for (int i = 0; i < (ROW * COL) / 30; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new Treasure(floor); // Create Treasure
		}
		for (int i = 0; i < (ROW * COL) / 20; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new EnemyRoom(floor); // Create Treasure
		}
	}
	
	private int createOneSpecialRoomOfEach(List<XY> list, int floor) {
		var index = 0;
		grid[list.get(index).y()][list.get(index++).x()] = new Shop(floor); // Create shop
		grid[list.get(index).y()][list.get(index++).x()] = new Treasure(floor); // Create treasure
		grid[list.get(index).y()][list.get(index++).x()] = new Exit(floor); // Create exit
		grid[list.get(index).y()][list.get(index++).x()] = new EnemyRoom(floor); // Create Enemy
		grid[list.get(index).y()][list.get(index++).x()] = new Healer(floor); // Create Healer
		grid[list.get(index).y()][list.get(index++).x()] = new Start(); // Create start
		grid[list.get(index).y()][list.get(index++).x()] = new LockedDoor(floor); // Create LockedDoor (We need data to check if the hero has a key)
		grid[list.get(index).y()][list.get(index++).x()] = new EventRoom(floor); // Create Event
		return index;
	}

	/**
	 * Create the grid with coord
	 * 
	 * @return a list of 55 XY
	 */
	private List<XY> createXYList() {
		List<XY> list = new ArrayList<>();
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW; j++) {
				list.add(new XY(i, j));
			}
		}
		return list;
	}

	/**
	 * @param list of 55 XY
	 * @return a new list shuffled
	 */
	private List<XY> shuffleList(List<XY> list) {
		List<XY> list2 = new ArrayList<>(list);
		Collections.shuffle(list2);
		return list2;
	}

	/**
	 * Update the map
	 * 
	 * @param coord
	 */
	public void updateMap(XY coord) {
		updateAll(coord);
		addTwoVisible();
	}

	/**
	 * Add two more visible for each accessible room (not for the "LockedDoor")
	 */
	public void addTwoVisible() {
		for (var coord : heroAccessible) {
			var room = grid[coord.y()][coord.x()];
			switch (room) {
			case LockedDoor door -> {
			}
			default -> {
				heroVisible.add(coord);
				heroVisibleForLine.add(coord);
				for (var coord_acc : room.getAccessible()) {
					heroVisible.add(coord_acc);
					var room2 = grid[coord_acc.y()][coord_acc.x()];
					switch (room) {
					case LockedDoor door -> {
					}
					default -> {
						heroVisibleForLine.add(coord_acc);
						for (var coord_acc2 : room2.getAccessible()) {
							heroVisible.add(coord_acc2);
						}
					}
					}
				}
			}
			}
		}
	}

	/**
	 * Update All hashMap: visited, visible, accessible, and accessible for line
	 * 
	 * @param coord
	 */
	public void updateAll(XY coord) {
		if (!heroVisited.contains(coord)) {
			heroAccessible.remove(coord);
			heroVisibleForLine.add(coord);
			heroVisited.add(coord);
			heroVisible.add(coord);
			var room = grid[coord.y()][coord.x()];
			for (var coord_ac : room.getAccessible()) {
				var room_acc = grid[coord_ac.y()][coord_ac.x()];
				switch (room_acc) {
				case Hallway coordHallWay -> updateAll(coord_ac);
				default -> {
					if (!heroVisited.contains(coord_ac)) {
						heroVisible.add(coord_ac);
						heroAccessible.add(coord_ac);
					}
				}
				}
			}
		}
	}

	/**
	 * Get the shortest Path
	 * @param start
	 * @param end
	 * @return List of the best Way
	 */
	public List<XY> heroShortestPath(XY start, XY end) {
		if (start.equals(end)) return List.of();
		List<XY> queue = new ArrayList<>();
		Set<XY> visited = new HashSet<>();
		Map<XY, XY> parents = new HashMap<>();
		List<XY> bestPath = new ArrayList<>();
		
		queue.add(start);
		
		while (queue.size() != 0) {
			var first = queue.get(0);
			for (var acc : grid[first.y()][first.x()].getAccessible()) {
				if (!visited.contains(acc) && (heroAccessible.contains(acc) || heroVisited.contains(acc))) {
					parents.put(acc, first);
					visited.add(acc);
					if (!heroAccessible.contains(acc)) {
						queue.add(acc);
					}
					if (acc.equals(end)) {
						var enfant = acc;
						while(!enfant.equals(start)) {
							bestPath.add(enfant);
							enfant = parents.get(enfant);
						}
						bestPath.add(enfant);
						return List.copyOf(bestPath);
					}
				}
			}
			queue.remove(0);
		}
		return null;
	}
	
	/**
	 * Getter for grid
	 * 
	 * @return Room[][]
	 */
	public Room[][] getGrid() {
		return grid;
	}

	/**
	 * Getter for hero visited
	 * 
	 * @return HashSet<XY>
	 */
	public HashSet<XY> getHeroVisited() {
		return heroVisited;
	}

	/**
	 * Getter for hero visible
	 * 
	 * @return HashSet<XY>
	 */
	public HashSet<XY> getHeroVisible() {
		return heroVisible;
	}

	/**
	 * Getter for hero vsible line
	 * 
	 * @return HashSet<XY>
	 */
	public HashSet<XY> getHeroVisibleLine() {
		return heroVisibleForLine;
	}

	/**
	 * Getter for hero accessible
	 * 
	 * @return HashSet<XY>
	 */
	public HashSet<XY> getHeroAccessible() {
		return heroAccessible;
	}

	/**
	 * Getter for hero position
	 * 
	 * @return XY
	 */
	public XY getHeroPos() {
		return heroPos;
	}
	
	
	public int getRow() {
		return ROW;
	}
	
	public int getCol() {
		return COL;
	}
}
