package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import game.GameData;
import model.item.common.Gold;

public class Backpack {
	/**
	 * - Backpack - grid_size - All items in the bag
	 */
	private int[][] backpack = { // -2 : not unlock, -1 empty, else ID of item
			{ -2, -2, -1, -1, -1, -2, -2 }, { -2, -1, -1, -1, -1, -1, -2 }, { -2, -1, -1, -1, -1, -1, -2 },
			{ -2, -1, -1, -1, -1, -1, -2 }, { -2, -2, -1, -1, -1, -2, -2 } };

	private int gridSize;
	private ArrayList<Item> bagItemLst = new ArrayList<>(); // List of items I have (Index = ID)
	private int caseUnlock = 0; // Number of case we can currently unlock.

	public Item getItem(int x, int y) {
		var itemFromBag = bagItemLst.stream()
				.filter(item -> Arrays.stream(item.shape()).anyMatch(b -> (b.x() == x && b.y() == y))).findFirst().orElse(null);
		return itemFromBag;
	}

	/**
	 * Register the grid size of each tile in the backpack
	 * 
	 * @param gridSize
	 */
	public Backpack(int screenHeight) {
		gridSize = screenHeight / 15;
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
	 * Return an ArrayList of all item in the backpack
	 * 
	 * @return ArrayList<Item_Object>
	 */
	public ArrayList<Item> bagItemLst() {
		return bagItemLst;
	}

	public int getGridSize() {
		return gridSize;
	}

	public int getGoldInBag() {
		return bagItemLst.stream().filter(item -> item instanceof Gold).mapToInt(item -> ((Gold) item).value()).sum();
	}

	public boolean fuseGoldInBag(int value) {
		List<Gold> goldItems = bagItemLst.stream().filter(item -> item.ID() == 2).map(item -> (Gold) item).toList();
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
		List<Gold> goldItems = bagItemLst.stream().filter(item -> item.ID() == 2).map(item -> (Gold) item).toList();
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
					backpack[block.y()][block.x()] = item.ID();
				}
				bagItemLst.add(item);
				return true;
			}
		}
		default -> {
			if (checkPlace(item)) {
				var b = item.shape();
				for (var block : b) {
					backpack[block.y()][block.x()] = item.ID();
				}
				bagItemLst.add(item);
				return true;
			}
		}
		}
		return false;
	}

	private Set<XY> getArround(XY xy) {
		Set<XY> arround = new HashSet<>();
		for (var coord : getItem(xy.x(), xy.y()).shape()) {
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i == 0 || j == 0) {
						arround.add(new XY(coord.x() + i, coord	.y() + j));
					}
				}
			}
		}
		return arround;
	}
	
	public List<Item> getManaStone(GameData data) {
		return data.bag().bagItemLst.stream()
																.filter(t -> t.ID() == 16)
																.toList();
	}

	public Set<XY> getManaConnectedCoords(List<Item> manaStones) {
		List<XY> queue = new ArrayList<>();
		Set<XY> visited = new HashSet<>();
		Set<XY> connected = new HashSet<>();

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

		return connected;
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

	public void unlockCaseBackpack(XY coord) {
		if (caseUnlock > 0 && backpack[coord.y()][coord.x()] == -2) {
			backpack[coord.y()][coord.x()] = -1;
			caseUnlock -= 1;
		}
	}

	public int getCaseUnlock() {
		return caseUnlock;
	}

	public void addCaseUnlock(int value) {
		caseUnlock += value;
	}
}
