package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.item.common.Arrow;
import model.item.common.Gold;
import model.item.epic.Bow;
import model.item.epic.DespairShield;
import model.item.legendary.Axe;
import model.item.mythic.Mimicry;
import model.item.rare.Gant;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;
import model.item.superrare.Massue;

public class ItemRepository {

    // Stocke les armes par ID
    private static final Map<Integer, Item> itemRepositoryMap = new HashMap<>();
    private static List<Item> itemRankLst;

    
    // Trier du moins rare au plus rare
    public static void createItemRepository() {
  	  registerWeapon(new Arrow()); // Common
  	  registerWeapon(new Gant()); // Rare
  	  registerWeapon(new PoisonArrow()); // Rare 
  	  registerWeapon(new Massue()); // SuperRare
  	  registerWeapon(new Bomb()); // SuperRare
  	  registerWeapon(new DespairShield()); // Epic
  	  registerWeapon(new Bow()); // Epic
  	  registerWeapon(new Axe()); // Legendary
  	  registerWeapon(new Mimicry()); // Mythics
  	  createRankingWeapon();
    }
    
    // Méthode pour enregistrer une arme
    private static void registerWeapon(Item item) {
        itemRepositoryMap.put(item.ID(), item);
    }
    
    private static int finalScore(Item item) {
      return (item.rarity().ordinal() + 1) * item.score();
    }
    
    private static void createRankingWeapon() {
    	Collection<Item> items = itemRepositoryMap.values();
    	itemRankLst = new ArrayList<>(items);
    	itemRankLst.sort((a, b) -> Integer.compare(finalScore(a), finalScore(b)));
    }

    // Récupérer une arme par ID
    public static Item getWeapon(int id) {
        return itemRepositoryMap.get(id);
    }
    
    public static List<Item> getItemrankLst() {
			return itemRankLst;
		}

}

