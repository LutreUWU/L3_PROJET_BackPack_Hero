package model;

public class ItemFactory {
	public static Item createItem(String name) {
		/**
		 * Create any Item
		 * @param name : Name of the item
		 * @return Item : Item
		 */
		return switch(name) { // String name, int[] location, int[][] shape, String rarity + int energy_use + Other
		  case "Baguette" -> new Weapon(name, new int[] {-1, -1}, new int[][] {{1}, {1}, {1}}, "Common", 1, "Melee", 4);
		  case "Croissant Gun" -> new Weapon(name, new int[] {-1, -1}, new int[][] {{1, 1}, {1, 0}}, "Common", 1, "Ranged", 3);
		  case "Cheese Armor" -> new Armor(name, new int[] {-1, -1}, new int[][] {{1, 1}, {1, 1}}, "Common", 1, 3);
		  case "Umbrella" -> new Armor(name, new int[] {-1, -1}, new int[][] {{1, 1}, {1, 1}}, "Rare", 1, 5);
		  case "Magic Wine" -> new MagicItem(name, new int[] {-1, -1}, new int[][] {{1}}, "Common", 1, 2, 5, 2);
		  default -> throw new IllegalArgumentException("Unknow name");
		};
	}
 }
