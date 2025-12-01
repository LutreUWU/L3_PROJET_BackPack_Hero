package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Item;
import model.XY;

public class Treasure implements Room {
  int floor;
  private List<XY> accessible = new ArrayList<>();
  List<Item> reward;

  public Treasure(int floor) {
      this.floor = floor;
      reward = createReward();
  }
  
  public List<XY> get_accessible(){
		return accessible;
	}
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	private Item addItem(int random) {
		Random rand = new Random();
		var rarity = rand.nextInt(1);
	}
	
	private ArrayList<Item> createReward() {
		var list = new ArrayList<Item>();
		Random rand = new Random();
		for (int i = 0; i < 5; i++) {
			var x = rand.nextInt(5) + 1;
			
		}
		return list;
	}
}