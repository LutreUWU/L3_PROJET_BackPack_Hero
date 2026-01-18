package model.map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.XY;

/**
 * Class representing a floor in the game. It contains rooms, manages hero
 * position, visibility, and accessible paths.
 */
public class Floor {

	// Number of rows in the grid
	private final int ROW = 5;

	// Number of columns in the grid
	private final int COL = 11;

	// Grid of rooms
	final private Room[][] grid = new Room[ROW][COL];

	// Set of rooms visited by the hero
	final private HashSet<XY> heroVisited = new HashSet<>();

	// Set of rooms accessible by the hero
	final private HashSet<XY> heroAccessible = new HashSet<>();

	// Set of visible rooms
	final private HashSet<XY> heroVisible = new HashSet<>();

	// Set of visible rooms including line-of-sight
	final private HashSet<XY> heroVisibleForLine = new HashSet<>();

	// Current hero position
	private XY heroPos;

	/**
	 * Constructor for the floor. Creates all rooms and sets initial hero position.
	 * 
	 * @param floor current floor number
	 */
	public Floor(int floor) {
		XY start = createAllRoom(floor);
		HashSet<XY> visited = new HashSet<>();
		createWay(visited, start);
		heroPos = start;
		updateMap(start);
	}

	/**
	 * Set the hero's position.
	 * 
	 * @param heroPos2 new position of the hero
	 */
	public void setHeroPos(XY heroPos2) {
		heroPos = heroPos2;
	}

	/**
	 * Recursively create paths between rooms.
	 * 
	 * @param visited set of already visited rooms
	 * @param start   starting room coordinate
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
	 * Add all possible accessible neighbors for a given coordinate.
	 * 
	 * @param listacc list of accessible coordinates
	 * @param x       x-coordinate
	 * @param y       y-coordinate
	 */
	private void addAcc(List<XY> listacc, int x, int y) {
		if (x > 0) {
			listacc.add(new XY(x - 1, y));
		}
		if (y > 0) {
			listacc.add(new XY(x, y - 1));
		}
		if (x < COL - 1) {
			listacc.add(new XY(x + 1, y));
		}
		if (y < ROW - 1) {
			listacc.add(new XY(x, y + 1));
		}
		Collections.shuffle(listacc);
	}

	/**
	 * Create all rooms on the floor, including special rooms.
	 * 
	 * @param floor current floor number
	 * @return starting room coordinate
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
		return new XY(list2.get(0).x(), list2.get(0).y());
	}

	/**
	 * Create special rooms on the floor (shops, healers, events, treasures,
	 * enemies).
	 * 
	 * @param list  list of coordinates
	 * @param floor current floor number
	 */
	private void createSpecialRoom(List<XY> list, int floor) {
		var index = createOneSpecialRoomOfEach(list, floor);
		for (int i = 0; i < (ROW * COL) / 60; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new Shop(floor);
			grid[list.get(index).y()][list.get(index++).x()] = new Healer(floor);
			grid[list.get(index).y()][list.get(index++).x()] = new EventRoom(floor);
		}
		for (int i = 0; i < (ROW * COL) / 30; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new Treasure(floor);
		}
		for (int i = 0; i < (ROW * COL) / 20; i++) {
			grid[list.get(index).y()][list.get(index++).x()] = new EnemyRoom(floor);
		}
	}

	/**
	 * Create one of each special room on the floor.
	 * 
	 * @param list  list of coordinates
	 * @param floor current floor number
	 * @return index after creating special rooms
	 */
	private int createOneSpecialRoomOfEach(List<XY> list, int floor) {
		var index = 0;
		grid[list.get(index).y()][list.get(index++).x()] = new Start();
		grid[list.get(index).y()][list.get(index++).x()] = new Shop(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new Treasure(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new Exit(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new EnemyRoom(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new Healer(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new LockedDoor(floor);
		grid[list.get(index).y()][list.get(index++).x()] = new EventRoom(floor);
		return index;
	}

	/**
	 * Create a list of all coordinates in the grid.
	 * 
	 * @return list of coordinates
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
	 * Shuffle a list of coordinates.
	 * 
	 * @param list list to shuffle
	 * @return shuffled list
	 */
	private List<XY> shuffleList(List<XY> list) {
		List<XY> list2 = new ArrayList<>(list);
		Collections.shuffle(list2);
		return list2;
	}

	/**
	 * Update the map for hero visibility and accessibility.
	 * 
	 * @param coord hero coordinate
	 */
	public void updateMap(XY coord) {
		updateAll(coord);
		addTwoVisible();
	}

	/**
	 * Adds two additional visible rooms for each accessible room, excluding
	 * LockedDoors.
	 */
	public void addTwoVisible() {
		for (var coord : heroAccessible) {
			var room = grid[coord.y()][coord.x()];
			switch (room) {
			case LockedDoor _ -> {
			}
			default -> updateVisibilityForRoom(room, coord);
			}
		}
	}

	/**
	 * Updates visibility for a given room and its accessible neighbors.
	 * 
	 * - Adds the room itself to `heroVisible` and `heroVisibleForLine`. - Adds
	 * directly accessible rooms to `heroVisible`. - Adds neighbors of those rooms
	 * to `heroVisible` and `heroVisibleForLine`. - LockedDoor rooms are ignored.
	 *
	 * @param room  the room to update visibility for
	 * @param coord the coordinate of the room
	 */
	private void updateVisibilityForRoom(Room room, XY coord) {
		heroVisible.add(coord);
		heroVisibleForLine.add(coord);
		for (var coord_acc : room.getAccessible()) {
			heroVisible.add(coord_acc);
			var room2 = grid[coord_acc.y()][coord_acc.x()];
			switch (room2) {
			case LockedDoor _ -> {
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

	/**
	 * Update all hash sets: visited, visible, accessible, and visible for line.
	 * 
	 * @param coord hero coordinate
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
				case Hallway _ -> updateAll(coord_ac);
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
	 * Compute the shortest path for the hero between two points.
	 * 
	 * @param start starting coordinate
	 * @param end   ending coordinate
	 * @return list of coordinates for the best path
	 */
	public List<XY> heroShortestPath(XY start, XY end) {
	    if (start.equals(end)) return List.of();

	    List<XY> queue = new ArrayList<>();
	    Set<XY> visited = new HashSet<>();
	    Map<XY, XY> parents = new HashMap<>();
	    List<XY> bestPath = new ArrayList<>();
	    queue.add(start);

	    while (!queue.isEmpty()) {
	        List<XY> result = processNode(queue.get(0), queue, visited, parents, start, end, bestPath);
	        if (result != null) return result;
	        queue.remove(0);
	    }
	    return null;
	}

	/**
	 * Process a single node in the queue for BFS.
	 * Updates visited set, parents map, and checks if end is reached.
	 * 
	 * @param current current coordinate to process
	 * @param queue queue of coordinates
	 * @param visited set of already visited coordinates
	 * @param parents map of child -> parent coordinates
	 * @param start starting coordinate
	 * @param end ending coordinate
	 * @param bestPath list to store the path from start to end
	 * @return list of coordinates if end is reached, otherwise null
	 */
	private List<XY> processNode(XY current, List<XY> queue, Set<XY> visited, Map<XY, XY> parents,
	                             XY start, XY end, List<XY> bestPath) {
	    for (var acc : grid[current.y()][current.x()].getAccessible()) {
	        if (!visited.contains(acc) && (heroAccessible.contains(acc) || heroVisited.contains(acc))) {
	            parents.put(acc, current);
	            visited.add(acc);
	            if (!heroAccessible.contains(acc)) queue.add(acc);
	            if (acc.equals(end)) {
	                var node = acc;
	                while (!node.equals(start)) {
	                    bestPath.add(node);
	                    node = parents.get(node);
	                }
	                bestPath.add(start);
	                Collections.reverse(bestPath);
	                return List.copyOf(bestPath);
	            }
	        }
	    }
	    return null;
	}


	/**
	 * Getter for grid of rooms.
	 * 
	 * @return 2D array of rooms
	 */
	public Room[][] getGrid() {
		return grid;
	}

	/**
	 * Getter for visited rooms.
	 * 
	 * @return set of visited coordinates
	 */
	public HashSet<XY> getHeroVisited() {
		return heroVisited;
	}

	/**
	 * Getter for visible rooms.
	 * 
	 * @return set of visible coordinates
	 */
	public HashSet<XY> getHeroVisible() {
		return heroVisible;
	}

	/**
	 * Getter for visible rooms including line-of-sight.
	 * 
	 * @return set of coordinates
	 */
	public HashSet<XY> getHeroVisibleLine() {
		return heroVisibleForLine;
	}

	/**
	 * Getter for accessible rooms.
	 * 
	 * @return set of accessible coordinates
	 */
	public HashSet<XY> getHeroAccessible() {
		return heroAccessible;
	}

	/**
	 * Getter for hero position.
	 * 
	 * @return coordinate of hero
	 */
	public XY getHeroPos() {
		return heroPos;
	}

	/**
	 * Getter for number of rows.
	 * 
	 * @return number of rows
	 */
	public int getRow() {
		return ROW;
	}

	/**
	 * Getter for number of columns.
	 * 
	 * @return number of columns
	 */
	public int getCol() {
		return COL;
	}
}
