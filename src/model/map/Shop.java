package model.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import game.data.GameDataClick;
import model.Backpack;
import model.Item;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.item.common.Sword;

public final class Shop implements Room {
	final private int SHOP_SIZE = 5;
  private int floor;
  final private List<XY> accessible = new ArrayList<>();
  final private Map<Item, Integer> currentShop = new LinkedHashMap<>(); // Item : Price
  private String logShop = "Bienvenue au shop !";
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
		while (currentShop.size() != SHOP_SIZE) {
			var item = RandomItem.generate(floor);
			for (var itemBag : currentShop.keySet()) {
				if (itemBag.ID() == item.ID()) {
					alreadyThere = true;
				}
			}
			
			if (!alreadyThere) currentShop.put(item, item.score()); 
			alreadyThere = false;
		}
  }
  
  /**
   * Buy an item of the shop
   * @param item
   */
  public void buy(Backpack backpack) {
		var map = currentShop;
	  Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
    var item = it.next();
    if (backpack.getGoldInBag() >= item.getValue()) {
    	logShop = setLog(item.getKey());
    	backpack.subGoldInBag(item.getValue());
    	it.remove();
    	GameDataClick.addDragItem(item.getKey());
    }
    else {
    	logShop = "T'as pas la thune pour acheter ça";
    }
    if (map.isEmpty()) {
    	logShop = "Y a plus rien à acheter, reviens plus tard";
    }
  }
  
  public void setSellItemPrice(Item item) {
  	Objects.requireNonNull(item);
  	logShop = switch(item) {
					  	case Gold _ -> "Tu me vends de l'or contre de l'or ?";
					  	default -> "Je te rachète " + item.toString() + " pour " + item.score() / 2 + " gold";
					  	};		
  }
  
  public void setSellItem(Item item) {
  	Objects.requireNonNull(item);
  	logShop = "Transaction effectuée pour " + item.toString();
  }
  
  private String setLog(Item item) {
  	return switch(item) {
  	case Sword _ -> "T'es sah à acheter ça ?";
		default ->  "Très bon achat mon frère";
  	};
  }
  
  public Map<Item, Integer> getCurrentShop() {
		return currentShop;
	}
  
  public void rightShiftShop() {
		var map = currentShop;
	  Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
    Map.Entry<Item, Integer> last = null;
    while (it.hasNext()) {
    	last = it.next();
    }
    map.remove(last.getKey());
    LinkedHashMap<Item, Integer> copy = new LinkedHashMap<>();
    copy.put(last.getKey(), last.getValue());
    copy.putAll(map);
    map.clear();
    map.putAll(copy);
  }
  
  public void leftShiftShop() {
  	var map = currentShop;
    Iterator<Map.Entry<Item, Integer>> it = map.entrySet().iterator();
    Map.Entry<Item, Integer> first = it.next();
    it.remove();
    map.put(first.getKey(), first.getValue());
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
	
	/**
	 * Adds rooms that is accessible from the others
	 * @param coord
	 */
	@Override
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public String getLogShop() {
		return logShop;
	}
	
}