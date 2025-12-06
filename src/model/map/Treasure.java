package model.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import game.GameData;
import game.data.GameDataBackpack;
import game.data.GameDataClick;
import model.Item;
import model.ItemRepository;
import model.XY;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.legendary.Axe;
import model.map.eventManager.LinkedEvent;

public final class Treasure implements Room {
  private int floor;
  final private List<XY> accessible = new ArrayList<>();
  final private List<Item> rewardList = new ArrayList<Item>();
  private boolean alreadyVisited = false;
  private LinkedEvent event;

  public Treasure(int floor2) {
      floor = floor2;
      createReward();
      event = new LinkedEvent(floor, "treasure");
  }
  
  public LinkedEvent getEvent() {
  	return event;
  }
  
  public List<XY> getAccessible(){
		return accessible;
	}
  
  public List<Item> getRewardList(){
		return rewardList;
	}
  
  public boolean getAlreadyVisited() {
  	return alreadyVisited;
  }
  
  public void nowVisited() {
  	alreadyVisited = true;
  }
	
	public void addAccessible(XY coord){
		accessible.add(coord);
	}
	
	public void openReward() {
		for (int i = 0; i < rewardList.size(); i++) {
			GameDataClick.addDragItem(rewardList.get(i));
		}
	}
	
	private void createReward() {
		var allItem = ItemRepository.getItemrankLst();
		var sizeList = allItem.size();
		var sixth = (int) (sizeList / 6);
		Random rand = new Random();
		rewardList.add(new Gold(rand.nextInt(20) + 10)); // Gold between 10 and 30
		rewardList.add(new KeyDoor()); // add a key
		// add 3 random item (probability with Gauss)
		for (int i = 1; i <= 3; i++) {
			int randomIndexWithGauss = (int) rand.nextGaussian(sixth * floor * 2, 1);
			if (randomIndexWithGauss < 0) randomIndexWithGauss = 0;
			if (randomIndexWithGauss >= sizeList - 1) randomIndexWithGauss = sizeList - 1;
			rewardList.add(allItem.get(randomIndexWithGauss));
		}
	}
}