package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import model.item.common.Gold;
import model.item.rare.ManaStone;

/**
 * Represents the player's backpack grid.
 *
 * The backpack is a fixed-size grid that can contain items of various shapes.
 * It manages item placement, removal, gold and mana aggregation, locked cells,
 * and mana connectivity between items.
 */
public class Backpack {
	/**
	 * 2D grid representing the backpack.
	 *
	 * Values meaning:
	 * - -2 : locked cell
	 * - -1 : empty cell
	 * - >= 0 : item ID
	 */
	private int[][] backpack;
	/** Number of rows in the backpack grid. */
	final private int ROW = 5;
	/** Number of columns in the backpack grid. */
	final private int COL = 7;
	/** Pixel size of a single grid cell. */
	final private int gridSize;
	/** List of all items currently stored in the backpack. */
	final private List<Item> bagItemLst = new ArrayList<>();
	/** Number of locked cells that can currently be unlocked. */
	private int caseUnlock = 0;
	/** Coordinates connected to mana sources. */
	final private Set<XY> connected = new HashSet<>();
	/** Items connected, directly or indirectly, to mana sources. */
	final private Set<Item> connectedItem = new HashSet<>();

		
	/**
	 * Creates a new backpack and initializes its grid.
	 *
	 * @param screenHeight the screen height used to compute the grid cell size
	 */
	public Backpack(int screenHeight) {
		backpack = createBackpackGrid();
		gridSize = screenHeight / 15;
	}
	
	/**
	 * Creates and initializes the backpack grid.
	 * Border cells are locked, inner cells are empty.
	 *
	 * @return a newly initialized backpack grid
	 */
	private int[][] createBackpackGrid() {
    int[][] backpack = new int[ROW][COL];
    for (int i = 0; i < ROW; i++) {
        for (int j = 0; j < COL; j++) {
            if (i == 0 || i == ROW  - 1 || j <= 0 || j == COL - 1) {
                backpack[i][j] = -2;
            } else {
                backpack[i][j] = -1;
            }
        }
    }
    return backpack;
	}

	/**
	 * Attempts to merge a specified amount of gold into the gold items in the backpack.
	 * 
	 * It iterates over all gold items (ID = 2) in the backpack. If an item's value
	 * is greater than the remaining amount to merge, it adds the value to that gold item.
	 * Otherwise, it consumes the gold item and continues with the remaining amount.
	 * 
	 * @param value the amount of gold to merge
	 * @return true if the full value was merged, false if there was not enough gold
	 * 
   * @throws IllegalArgumentException if nb is not positive

	 */
	public boolean fuseGoldInBag(int value) {
		if (value < 0) {
      throw new IllegalArgumentException("maxHP must be > 0");
    }
		List<Gold> goldItems = bagItemLst.stream().filter(item -> item.info().ID() == 2).map(item -> (Gold) item).toList();
		for (Gold gold : goldItems) {
			if (gold.value() > value) {
				gold.addGoldValue(value);
			} else {
				value -= gold.value();
				removeItemFromBackpack(gold);
			}
			if (value == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Subtracts a specified amount of gold from the gold items in the backpack.
	 * 
	 * It iterates over all gold items (ID = 2) in the backpack. If an item's value
	 * is greater than the remaining amount to subtract, it decreases the item's value.
	 * Otherwise, it removes the gold item completely and continues with the remaining amount.
	 * 
	 * @param value the amount of gold to subtract
	 * 
   * @throws IllegalArgumentException if nb is not positive
	 */
	public void subGoldInBag(int value) {
		if (value < 0) {
      throw new IllegalArgumentException("maxHP must be > 0");
    }
		List<Gold> goldItems = bagItemLst.stream().filter(item -> item.info().ID() == 2).map(item -> (Gold) item).toList();
		for (Gold gold : goldItems) {
			if (gold.value() > value) {
				Gold newGold = new Gold(gold.shape(), gold.direction(), gold.value() - value);
				removeItemFromBackpack(gold);
				addItemToBackpack(newGold);
			} else {
				value -= gold.value();
				removeItemFromBackpack(gold);
			}
			if (value == 0) {
				return;
			}
		}
	}

	/**
	 * Check for each block of the item if it can fit in the backpack
	 * 
	 * @param item Item we wants to check
	 * @return true if we can, else false if we can't
	 */
	private boolean checkPlace(Item item) {
		if (item == null) {
			return false;
		}
		var b = item.shape();
		for (var block : b) {
			int y = block.y();
			int x = block.x();
			if (inBackpack(x, y)) {
				return false;
			}
			switch (item) {
			case Curse _ -> {
				if (backpack[y][x] == -2 || backpack[y][x] == 13) {
					return false;
				}
			}
			default -> {
				if (backpack[y][x] != -1) {
					return false;
				}
			}
			}

		}
		return true;
	}
	
	/**
	 * Checks whether a given coordinate is outside the backpack grid.
	 * 
	 * The backpack is represented as a 2D array, where (0,0) is the top-left corner.
	 * This method returns true if the coordinates are invalid (outside the grid),
	 * and false if they are within bounds.
	 * 
	 * @param x the column index to check
	 * @param y the row index to check
	 * @return true if the coordinate is outside the backpack, false otherwise
	 */
	public boolean inBackpack(int x, int y) {
		return (y < 0 || y >= backpack.length || x < 0 || x >= backpack[0].length);
	}

	/**
	 * Add an item in the backpack of the game. If item is null, it means that we
	 * just press the button for adding.
	 * 
	 * @param item Item we wants to check
	 * @return true if we can add it, else false if we can't
	 */
	public boolean addItemToBackpack(Item item) {
		if (item == null) {
			return false;
		}
		switch (item) {
		case Curse _ -> {
			if (checkPlace(item)) {
				var b = item.shape();
				for (var block : b) {
					if (backpack[block.y()][block.x()] != -1) {
						removeItemFromBackpack(getItem(block.x(), block.y()));
					}
					backpack[block.y()][block.x()] = item.info().ID();
				}
	
				bagItemLst.add(item);
				return true;
			}
		}
		default -> {
			if (checkPlace(item)) {
				var b = item.shape();
				for (var block : b) {
					backpack[block.y()][block.x()] = item.info().ID();
				}
				bagItemLst.add(item);
				return true;
			}
		}
		}
		return false;
	}

	/**
	 * Get all coord arround an item
	 * 
	 * @param xy
	 * @return list of coord arrount the item with coord xy
	 */
	private Set<XY> getArround(XY xy) {
		Set<XY> arround = new HashSet<>();
		for (var coord : getItem(xy.x(), xy.y()).shape()) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i == 0 || j == 0) {
						arround.add(new XY(coord.x() + i, coord.y() + j));
					}
				}
			}
		}
		return arround;
	}

	/**
	 * Get all mana stone
	 * 
	 * @return a list of mana stone
	 */
	private List<Item> getManaStone() {
		return bagItemLst.stream().filter(t -> t.info().ID() == 16).toList();
	}

	/**
	 * Initializes the sets and queue for tracking mana connectivity.
	 * 
	 * This method finds all ManaStone items in the backpack, adds their coordinates
	 * to the processing queue, marks them as visited, and adds them to the connected
	 * set. It effectively sets up the starting points for a breadth-first search
	 * to determine which items are connected to mana sources.
	 * 
	 * @param queue  the list of coordinates to process for mana connectivity
	 * @param visited the set of coordinates already visited
	 */
	private void initManaConnected(List<XY> queue, Set<XY> visited) {
    var manaStones = getManaStone();
    connected.clear();
    connectedItem.clear();
    for (Item mana : manaStones) {
        for (XY coord : mana.shape()) {
            queue.add(coord);
            visited.add(coord);
            connected.add(coord);
        }
    }
	}
	/**
	 * Processes one step of the breadth-first search for mana connectivity.
	 * 
	 * This method takes the first coordinate from the queue, checks all surrounding
	 * coordinates (using {@code getArround}), and if the coordinate is not yet visited
	 * and within the backpack, it checks for an item. If an item exists, it is added
	 * to the set of connected items. If the item is conductive, its coordinates are
	 * added to the queue and connected set for further processing.
	 * 
	 * @param queue   the list of coordinates to process for mana connectivity
	 * @param visited the set of coordinates already visited
	 */
	private void whileManaConnected(List<XY> queue, Set<XY> visited) {
	    var first = queue.get(0);
	    for (var acc : getArround(first)) {
	        if (!visited.contains(acc)) {
	            visited.add(acc);
	            if (!inBackpack(acc.x(), acc.y())) {
	                Item item = getItem(acc.x(), acc.y());
	                if (item != null) {
	                    connectedItem.add(item); // An item can use mana even if it is not conductive
	                    if (item.isConductive()) {
	                        queue.add(acc);
	                        connected.add(acc);
	                    }
	                }
	            }
	
	        }
	    }
	    queue.remove(0);
	}

	/**
	 * Update all coord where mana can be found. Update all Item that can use mana.
	 */
	public void updateManaConnected() {
	    List<XY> queue = new ArrayList<>();
	    Set<XY> visited = new HashSet<>();
	    initManaConnected(queue, visited);
	    while (!queue.isEmpty()) {
	        whileManaConnected(queue, visited);
	    }
	}

	/**
	 * Check if the item is connected to a manastone in the bag.
	 * 
	 * @param item {@code Item} we wants to check
	 * @return true if connected to a manastone, else false
	 */
	public boolean itemConnectedToMana(Item item) {
		return connectedItem.contains(item);
	}

	/**
	 * Remove an item from the backpack
	 * 
	 * @param item Item we wants to remove
	 * @throws NullPointerException if item is null
	 */
	public void removeItemFromBackpack(Item item) {
		Objects.requireNonNull(item);
		var b = item.shape();
		for (var block : b) {
			backpack[block.y()][block.x()] = -1;
		}
		bagItemLst.remove(item);
	}

	/**
	 * Unlock an item from the backpack
	 * 
	 * @param coord 	coord of the case we wants to unlock
	 * @throws NullPointerException if coord is null
	 */
	public void unlockCaseBackpack(XY coord) {
		Objects.requireNonNull(coord);
		if (caseUnlock > 0 && backpack[coord.y()][coord.x()] == -2) {
			backpack[coord.y()][coord.x()] = -1;
			caseUnlock -= 1;
		}
	}

	/**
	 * Returns the number of backpack cells that can currently be unlocked.
	 *
	 * @return number of unlockable cells
	 */
	public int getCaseUnlock() {
		return caseUnlock;
	}

	/**
	 * Increases the number of backpack cells that can be unlocked.
	 *
	 * @param value number of cells to add to the unlock counter
	 */
	public void addCaseUnlock(int value) {
		caseUnlock += value;
	}

	/**
	 * Returns all grid coordinates that are connected to a mana source.
	 *
	 * @return an immutable set of coordinates connected to mana
	 */
	public Set<XY> getManaConnectedCoords() {
		return Set.copyOf(connected);
	}
	
	/**
	 * Returns the number of rows in the backpack grid.
	 *
	 * @return row count
	 */
	public int getRow() {
		return ROW;
	}
	
	/**
	 * Returns the number of columns in the backpack grid.
	 *
	 * @return column count
	 */
	public int getCol() {
		return COL;
	}
	
	/**
	 * Returns the item occupying the given grid coordinate.
	 *
	 * @param x column index
	 * @param y row index
	 * @return the item at the given position, or null if none exists
	 */
	public Item getItem(int x, int y) {
		var itemFromBag = bagItemLst.stream()
				.filter(item -> Arrays.stream(item.shape()).anyMatch(b -> (b.x() == x && b.y() == y))).findFirst().orElse(null);
		return itemFromBag;
	}

	/**
	 * Return the grid of the backpack.
	 * 
	 * @return integer[][]
	 */
	public int[][] grid() {
		return backpack;
	}

	/**
	 * Returns an immutable list of all items currently stored in the backpack.
	 * 
	 * Modifications to the returned list are not allowed; to add or remove items,
	 * use the appropriate Backpack methods.
	 * 
	 * @return an unmodifiable List of Item objects contained in the backpack
	 */
	public List<Item> bagItemLst() {
		return List.copyOf(bagItemLst);
	}

	/**
	 * Returns the pixel size of a single grid cell.
	 *
	 * @return grid cell size
	 */
	public int getGridSize() {
		return gridSize;
	}

	/**
	 * Computes the total amount of mana stored in the backpack.
	 *
	 * @return total mana value
	 */
	public int getManaInBag() {
		return bagItemLst.stream().filter(item -> item instanceof ManaStone).mapToInt(item -> ((ManaStone) item).value())
				.sum();
	}

	/**
	 * Computes the total amount of gold stored in the backpack.
	 *
	 * @return total gold value
	 */
	public int getGoldInBag() {
		return bagItemLst.stream().filter(item -> item instanceof Gold).mapToInt(item -> ((Gold) item).value()).sum();
	}
	
	/**
   * Get the price the backpack (Sum all price of each item)
   * 
   * @return The price of the backpack
   */
  public int backpackPrice() {
      return bagItemLst.stream().mapToInt(item -> item.info().score()).sum();
  }
}
