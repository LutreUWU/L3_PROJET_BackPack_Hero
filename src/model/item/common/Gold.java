package model.item.common;

import java.util.ArrayList;

import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public class Gold implements Item {
	private int gold;
	private int sizeCount = 1; // 1, 2, or 3
	private XY[] b = new XY[1]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = Rarity.COMMON;
	private final int id = 2;
	private final int score = -1;
	private String description = "De l'or, l'indispensable pour SURVIVRE";
	private final String effect = "C'est de l'argent frérot, ça sert pour tout.";


	public Gold(int gold2) {
		gold = gold2;
		setXY(new XY(0, 0));
	}
	
	public int getSizeCount() {
		return sizeCount;
	}
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold2) {
		if (gold < 0) throw new IllegalArgumentException("Gold must be not negative");
		gold = gold2;
	}

	public void addGold(int value) {
		gold += value;
	}
	
	public void subGold(int value) {
		gold -= value;
	}
	
	public void updateGoldSize() {
		if (gold <= 15) sizeCount = 1;
		else if (gold <= 50) sizeCount = 2;
		else sizeCount = 3;
	}
	
	@Override
	public void use(Enemy enemy, ArrayList<Enemy> lstEnemy) {
		// Can't be use with a click
	}
	
  @Override
  public void setDirection(Direction d) {
    this.direction = d;
  }
	
  @Override
  public XY[] shape() {
      return b;
  }

  @Override
  public Direction direction() {
      return direction;
  }
  
  @Override
  public Rarity getRarity() {
		return rarity;
	}
  
  @Override
  public int getScore() {
		return score;
	}
  
  @Override
  public int getID() {
		return id;
	}
  
  @Override
  public String getDescription() {
  	return description;
  }
  
  @Override
  public String getEffect() {
  	return effect;
  }
  
  @Override
	public void setXY(XY coord) {
		b[0] = new XY(coord.x(), coord.y());
	}

  @Override
  public String toString() {
  	return "Gold";
  }
  
  @Override
  public Item copy() {
  	return null;
  }
	
}
