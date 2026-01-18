package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.item.common.Arrow;
import model.item.epic.Bow;
import model.item.epic.DespairShield;
import model.item.epic.EnchantedDiamondSword;
import model.item.epic.Shield;
import model.item.legendary.Axe;
import model.item.mythic.Mimicry;
import model.item.rare.Cookie;
import model.item.rare.FireBall;
import model.item.rare.Gant;
import model.item.rare.ManaStone;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;
import model.item.superrare.Massue;

/**
 * Repository for all items in the game.
 * 
 * This class stores all weapons and items, allows retrieval by ID,
 * and maintains a ranked list of items based on rarity and score.
 */
public class ItemRepository {
  /** Map of item ID to item instance */
  private static final Map<Integer, Item> itemRepositoryMap = new HashMap<>();
  /** List of items ranked by final score (rarity * score) */
  private static List<Item> itemRankLst;

  /**
   * Initializes the item repository.
   * 
   * Registers all items in the game, including common, rare, super rare,
   * epic, legendary, and mythic items. Also generates the ranking list.
   */
  public static void createItemRepository() {
	  registerWeapon(new Arrow()); // Common
	  registerWeapon(new Gant()); // Rare
	  registerWeapon(new PoisonArrow()); // Rare
	  registerWeapon(new FireBall()); // Rare
	  registerWeapon(new ManaStone(2)); // Rare
	  registerWeapon(new Cookie()); // Rare 
	  registerWeapon(new Massue()); // SuperRare
	  registerWeapon(new Bomb()); // SuperRare
	  registerWeapon(new DespairShield()); // Epic
	  registerWeapon(new EnchantedDiamondSword()); // Epic
	  registerWeapon(new Shield()); // Epic
	  registerWeapon(new Bow()); // Epic
	  registerWeapon(new Axe()); // Legendary
	  registerWeapon(new Mimicry()); // Mythics
	  createRankingWeapon();
  }
  
  /**
   * Registers a single weapon in the repository.
   * 
   * @param item the item to register
   */
  private static void registerWeapon(Item item) {
      itemRepositoryMap.put(item.info().ID(), item);
  }
  
  /**
   * Computes the final score of an item for ranking purposes.
   * 
   * @param item the item
   * @return final score based on rarity and base score
   */
  private static int finalScore(Item item) {
    return (item.info().rarity().ordinal() + 1) * item.info().score();
  }
  
  /**
   * Creates a sorted ranking list of all items in the repository.
   * 
   * Items are sorted in ascending order based on their final score.
   */
  private static void createRankingWeapon() {
  	Collection<Item> items = itemRepositoryMap.values();
  	itemRankLst = new ArrayList<>(items);
  	itemRankLst.sort((a, b) -> Integer.compare(finalScore(a), finalScore(b)));
  }

  /**
   * Returns the item associated with a given ID.
   * 
   * @param id the ID of the item
   * @return the corresponding item, or null if not found
   */
  public static Item getWeapon(int id) {
      return itemRepositoryMap.get(id);
  }
  
  /**
   * Returns a copy of the ranked item list.
   * 
   * @return list of items sorted by final score
   */
  public static List<Item> getItemrankLst() {
		return List.copyOf(itemRankLst);
	}
}

