package model;

import java.util.HashSet;
import java.util.Set;

import game.GameData;
import model.item.common.*;
import model.item.epic.*;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;

public class Synergy {
	
	private static int bonusDmg = 0;
	
	public static boolean checkSynergie(GameData data, Item item) {
		switch(item) {
		case Arrow _, PoisonArrow _ -> {return containBow(data);}
		case Bomb _ -> {bonusDmg = numberOfBombArround(data, item); return true;}
		default -> {return true;}
		}
	}
	
	private static boolean containBow(GameData data) {
		for (var itemBag : data.bag().bagItemLst()) {
			switch(itemBag) {
			case Bow _ -> {return true;}
			default -> {}
			}
		}
		return false;
	}
	
	private static int numberOfBombArround(GameData data, Item item) {
		Set<Item> neighbor = new HashSet<>();
		for (var coord : item.shape()) {
			for (var i = -1; i < 2; i++) {
				for (var j = -1; j < 2; j++) {
					if (indexInBagPack(data, coord.x(), coord.y())) {
						var itemNeighbor = data.bag().getItem(coord.x() + i, coord.y() + j);
						if (itemNeighbor != null) {
							neighbor.add(itemNeighbor);
						}
					}
				}
			}
		}
		
		return neighbor.size() - 1;
	}
	
	private static boolean indexInBagPack(GameData data, int x, int y) {
		if (x < 0) return false;
		if (y < 0) return false;
		if (x > data.bag().grid()[0].length) return false;
		if (y > data.bag().grid().length) return false;
		return true;
	}
	
	public int getBonusDmg() {
		return bonusDmg;
	}
}
