package model;

import game.GameData;
import model.item.common.*;
import model.item.epic.*;

public class Synergy {
	public static boolean checkSynergie(GameData data, String item) {
		return switch(item) {
		case "arrow" -> containBow(data);
		default -> true;
		};
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
}
