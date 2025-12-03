package model;

import game.data.GameDataHero;
import model.monster.Enemy;

public class Gold implements Item {
	private int gold = 100;
	private int size_count = 1; // 1, 2, 3 or 4
	private Block[] b = new Block[1]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = null; 
	private final int id = 2;
	private final int score = -1;
	
	public Gold() {
		setXY(new XY(0, 0));
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
		if (gold <= 10) size_count = 1;
		else if (gold <= 25) size_count = 2;
		else if (gold <= 50) size_count = 3;
		else size_count = 4;
	}
	
	@Override
	public void use(Enemy enemy) {
		// Can't be use with a click
	}
	
  @Override
  public void setDirection(Direction d) {
    this.direction = d;
  }
	
  @Override
  public Block[] shape() {
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
	public void setXY(XY coord) {
		b[0] = new Block(coord.x(), coord.y());
	}

	
}
