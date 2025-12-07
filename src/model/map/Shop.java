package model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.data.GameDataClick;
import model.Item;
import model.ItemRepository;
import model.RandomItem;
import model.XY;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.common.Sword;

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
		currentShop.put(new KeyDoor(), 10);
		while (currentShop.size() != 4) {
			var item = RandomItem.generate(floor);
			currentShop.put(item, item.finalScore());
		}
  	IO.println(currentShop);
  }
  
  /**
   * Buy an item of the shop
   * @param item
   */
  public void buy(Item item) {
  	currentShop.put(item, -1);
  	GameDataClick.addDragItem(item);
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