package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.accessory.Gant;
import model.shield.DespairShield;
import model.weapon.Axe;
import model.weapon.Massue;
import model.weapon.Mimicry;
import model.weapon.Sword;

public class ItemRepository {

    // Stocke les armes par ID
    private static final Map<Integer, Item> itemRepositoryMap = new HashMap<>();
    private static List<Item> itemRankLst;

    
    // Trier du moins rare au plus rare
    public static void createItemRepository() {
  	  registerWeapon(new Sword()); // Common 
  	  registerWeapon(new Gant()); // Rare
  	  registerWeapon(new Massue()); // SuperRare
  	  registerWeapon(new DespairShield()); // Epic
  	  registerWeapon(new Axe()); // Legendary
  	  registerWeapon(new Mimicry()); // Mythics
  	  createRankingWeapon();
    }
    
    // Méthode pour enregistrer une arme
    private static void registerWeapon(Item item) {
        itemRepositoryMap.put(item.getID(), item);
    }
    
    private static void createRankingWeapon() {
    	Collection<Item> items = itemRepositoryMap.values();
    	itemRankLst = new ArrayList<>(items);
    	itemRankLst.sort((a, b) -> Integer.compare(a.final_score(), b.final_score()));
    }

    // Récupérer une arme par ID
    public static Item getWeapon(int id) {
        return itemRepositoryMap.get(id);
    }
    
    public static List<Item> getItemrankLst() {
			return itemRankLst;
		}


}

