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
    private static final Map<Integer, Item> item_map = new HashMap<>();
    private static List<Item> itemRank_lst;

    
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
        item_map.put(item.getID(), item);
    }
    
    private static void createRankingWeapon() {
    	Collection<Item> items = item_map.values();
    	itemRank_lst = new ArrayList<>(items);
    	itemRank_lst.sort((a, b) -> Integer.compare(a.final_score(), b.final_score()));
    	IO.println(itemRank_lst);
    }

    // Récupérer une arme par ID
    public static Item getWeapon(int id) {
        return item_map.get(id);
    }
    
    public static List<Item> getItemrankLst() {
			return itemRank_lst;
		}


}

