package model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.data.GameDataClick;
import model.Item;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.map.eventManager.LinkedEvent;

public final class Shop implements Room {
  private int floor;
  final private List<XY> accessible = new ArrayList<>();
  final private Map<Item, Integer> currentShop = new HashMap<>(); // Item : Price

  /**
   * Constructor for the Shop
   * @param floor2
   */
  public Shop(int floor2) {
    floor = floor2;
    createShop();
  }
  
  /**
   * Create the shop with 4 items
   */
  private void createShop() {
  	int i = 0;
  	boolean alreadyThere = false;
		currentShop.put(new Gold(10), 10);
		while (currentShop.size() != 4) {
			var item = RandomItem.generate(floor);
			for (var itemBag : currentShop.keySet()) {
				if (itemBag.ID() == item.ID()) {
					alreadyThere = true;
				}
			}
			// Créer une méthode pour calculer le prix en fonction du score et la rareté de l'item
			if (!alreadyThere) currentShop.put(item, item.score()); 
			alreadyThere = false;
		}
  }
  
  /**
   * Buy an item of the shop
   * @param item
   */
  public void buy(Item item) {
  	currentShop.put(item, -1);
  	GameDataClick.addDragItem(item);
  }
  
  public Map<Item, Integer> getCurrentShop() {
		return currentShop;
	}
  
  /**
	 * Getter for accessibles
	 * 
	 * @return List<XY>
	 */
	@Override
  public List<XY> getAccessible(){
		return accessible;
	}
	
}