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

public class ItemRepository {

    private static final Map<Integer, Item> itemRepositoryMap = new HashMap<>();
    private static List<Item> itemRankLst;

    
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
    
    // Méthode pour enregistrer une arme
    private static void registerWeapon(Item item) {
        itemRepositoryMap.put(item.info().ID(), item);
    }
    
    private static int finalScore(Item item) {
      return (item.info().rarity().ordinal() + 1) * item.info().score();
    }
    
    private static void createRankingWeapon() {
    	Collection<Item> items = itemRepositoryMap.values();
    	itemRankLst = new ArrayList<>(items);
    	itemRankLst.sort((a, b) -> Integer.compare(finalScore(a), finalScore(b)));
    }

    public static Item getWeapon(int id) {
        return itemRepositoryMap.get(id);
    }
    
    public static List<Item> getItemrankLst() {
			return List.copyOf(itemRankLst);
		}

}

