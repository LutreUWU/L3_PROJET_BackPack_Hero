package model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import game.GameData;
import model.item.common.*;
import model.item.epic.*;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;

/**
 * Handles item synergies and calculates damage bonuses based on item combinations.
 * 
 * This class provides static methods to check if an item triggers a synergy in the
 * current game state, and computes the associated bonus damage.
 */
public class Synergy {
  /** Temporary storage for the calculated bonus damage */
	private static int bonusDmg = 0;
	
	/**
   * Checks if the given item triggers a synergy in the current game state.
   * Updates the bonus damage if applicable.
   * 
   * @param data The current game data
   * @param item The item to check
   * @return true if the item triggers a synergy, false otherwise
   */
	public static boolean checkSynergie(GameData data, Item item) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(item);
		switch(item) {
		case Arrow _, PoisonArrow _ -> {return containBow(data);}
		case Bomb _ -> {bonusDmg = numberOfBombArround(data, item); return true;}
		case Shield _ -> {bonusDmg = 2; return true;}
		default -> {return true;}
		}
	}
	
	 /**
   * Checks if the player's backpack contains a bow.
   * 
   * @param data the current game data
   * @return true if a bow is present, false otherwise
   */
	private static boolean containBow(GameData data) {
		for (var itemBag : data.bag().bagItemLst()) {
			switch(itemBag) {
			case Bow _ -> {return true;}
			default -> {}
			}
		}
		return false;
	}
	
	 /**
   * Counts how many bombs of the same type are adjacent to the given bomb.
   * 
   * @param data The current game data
   * @param item The bomb to check around
   * @return the number of adjacent bombs of the same type
   */
	private static int numberOfBombArround(GameData data, Item item) {
		Set<Item> neighbor = new HashSet<>();
		for (var coord : item.shape()) {
			for (var i = -1; i < 2; i++) {
				for (var j = -1; j < 2; j++) {
					if (indexInBagPack(data, coord.x() + i, coord.y() + j)) {
						var itemNeighbor = data.bag().getItem(coord.x() + i, coord.y() + j);
						// IO.println(coord.x() + i + " : " + (coord.y() + j) + " ITEM " + itemNeighbor);
						if (itemNeighbor != null && itemNeighbor.info().ID() == item.info().ID()) {
							neighbor.add(itemNeighbor);
							// IO.println(coord.x() + i + " : " + (coord.y() + j));
						}
					}
				}
			}
		}
		
		return neighbor.size();
	}
	
	 /**
   * Checks if the given coordinates are inside the backpack grid.
   * 
   * @param data The current game data
   * @param x 	 The column index
   * @param y 	 The row index
   * @return true if the coordinates are inside the grid, false otherwise
   */
	private static boolean indexInBagPack(GameData data, int x, int y) {
		if (x < 0) return false;
		if (y < 0) return false;
		if (x >= data.bag().grid()[0].length) return false;
		if (y >= data.bag().grid().length) return false;
		return true;
	}
	
	/**
   * Returns the current bonus damage and resets it to zero.
   * 
   * @return the bonus damage calculated by the last synergy check
   */
	public static int getBonusDmg() {
		var tmpBonus = bonusDmg;
		bonusDmg = 0;
		return tmpBonus;
	}
}
