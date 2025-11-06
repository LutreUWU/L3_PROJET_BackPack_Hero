package model;

public class ItemFactory {
	public static Item createItem(String name) {
		/**
		 * Create any Item
		 * 
		 * OPTIONS :
		 * Weapon : String type, int damage
		 * Shield : int protection
		 * ManaStone : int mana_gain
		 * MagicItem : int mana_use, int protection, int damage
		 * Armor : int protection_armor (as a pourcentage)
		 * @param name : Name of the item
		 * @return Item : Item
		 */
		return switch(name) { // String name, int[][] shape, String rarity, int energy_use + Options
		  case "Baguette" -> new Weapon(name, new int[][] {{1}, {1}, {1}}, "Common", 1, "Melee", 4);
		  case "Croissant Gun" -> new Weapon(name, new int[][] {{1, 1}, {1, 0}}, "Common", 1, "Ranged", 3);
		  case "Cheese Armor" -> new Armor(name, new int[][] {{1, 1}, {1, 1}}, "Common", 1, 3);
		  case "Umbrella" -> new Armor(name, new int[][] {{1, 1}, {1, 1}}, "Rare", 1, 5);
		  case "Magic Wine" -> new MagicItem(name, new int[][] {{1}}, "Common", 1, 2, 5, 2);
		  default -> throw new IllegalArgumentException("Unknow name");
		};
	}
 }
