package model.item.common;

import java.util.ArrayList;

import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public class KeyDoor implements Item {
	private XY[] b = new XY[2]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = Rarity.COMMON;
	private final int id = 1;
	private final int score = -1;
	private final String description = "Une clé toute mignonne !";
	private final String effect = "Ouvre une porte verrouillée (1 fois)";

	
	public KeyDoor() {
		setXY(new XY(0, 0));
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
	public void setXY(XY coord) {
		if (direction() == Direction.UP || direction() == Direction.DOWN) {
			int y = direction() == Direction.UP ? 1 : -1; 
			b[0] = new XY(coord.x(), coord.y());
			b[1] = new XY(coord.x(), coord.y() - 1 * y);
		}
		else {
			int x = direction() == Direction.RIGHT ? 1 : -1; 
			b[0] = new XY(coord.x(), coord.y());
			b[1] = new XY(coord.x() + 1 * x, coord.y());
		}
	}
  
  /*
  @Override
  public boolean equals(Object obj) {
  	return switch(obj) {
	  	case KeyDoor keyDoor2 -> keyDoor2.getID() == id;
	  	default -> false;
  	};
  }
  
  // Pour garder l'unicité d'une clé
  @Override
  public int hashCode() {
  	return id;
  }
  */

  @Override
  public String getDescription() {
		return description;
	}
  
  @Override
  public String toString() {
  	return "Key";
  }
  
  @Override
  public String getEffect() {
  	return effect;
  }
  
  @Override
  public Item copy() {
  	return new KeyDoor();
  }
	
}
