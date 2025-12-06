package model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import model.Item;
import model.ItemRepository;
import model.XY;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.common.Sword;

public final class Shop implements Room {
  private int floor;
  final private List<XY> accessible = new ArrayList<>();
  final private Map<Item, Integer> currentShop = new HashMap<>(); // Item : Price

  public Shop(int floor2) {
    floor = floor2;
    createShop();
    buy(new Sword());
  }
  
  private void createShop() {
  	var allItem = ItemRepository.getItemrankLst();
		var sizeList = allItem.size();
		var sixth = (int) (sizeList / 6);
		currentShop.put(new KeyDoor(), 10);
		Random rand = new Random();
		while (currentShop.size() != 4) {
			int randomIndexWithGauss = (int) rand.nextGaussian(sixth * floor * 2, 1);
			if (randomIndexWithGauss < 0) randomIndexWithGauss = 0;
			if (randomIndexWithGauss >= sizeList - 1) randomIndexWithGauss = sizeList - 1;
			var item = allItem.get(randomIndexWithGauss);
			currentShop.put(item, item.finalScore());
		}
  	IO.println(currentShop);
  }
  
  public void buy(Item item) {
  	currentShop.put(item, -1);
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
}