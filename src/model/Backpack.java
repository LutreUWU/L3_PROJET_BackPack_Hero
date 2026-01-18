package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
	final private ArrayList<Item> bagItemLst = new ArrayList<>();
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
	 * Return an List of all item in the backpack
	 * 
	 * @return List<Item_Object>
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

	public boolean fuseGoldInBag(int value) {
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

	public void subGoldInBag(int value) {
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
				if (GameDataCombat.combat()) {
					GameDataCombat.setCurseEvent(false);
				}
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
	 * Update all coord where mana can be found. Update all Item that can use mana.
	 */
	public void updateManaConnected() {
		connected.clear();
		connectedItem.clear();
		var manaStones = getManaStone();
		List<XY> queue = new ArrayList<>();
		Set<XY> visited = new HashSet<>();
		for (Item mana : manaStones) {
			for (XY coord : mana.shape()) {
				queue.add(coord);
				visited.add(coord);
				connected.add(coord);
			}
		}
		while (!queue.isEmpty()) {
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
	 * @throws Objects.requireNonNull if item is null
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
	 * @throws Objects.requireNonNull if coord is null
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
}
