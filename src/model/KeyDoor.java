package model;

import model.monster.Enemy;

public class KeyDoor implements Item {
	private Block[] b = new Block[2]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = null; 
	private final int id = 1;
	private final int score = -1;
	
	public KeyDoor() {
		setXY(new XY(0, 0));
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
		if (direction() == Direction.UP || direction() == Direction.DOWN) {
			b[0] = new Block(coord.x(), coord.y());
			b[1] = new Block(coord.x(), coord.y() - 1);
		}
		else {
			b[0] = new Block(coord.x(), coord.y());
			b[1] = new Block(coord.x() - 1, coord.y());
		}
	}
  
  @Override
  public boolean equals(Object obj) {
  	return switch(obj) {
	  	case KeyDoor keyDoor2 -> keyDoor2.getID() == id;
	  	default -> false;
  	};
  }
  
  @Override
  public int hashCode() {
  	return id;
  }

}
