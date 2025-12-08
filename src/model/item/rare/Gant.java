package model.item.rare;

import java.util.ArrayList;

import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

/**
 * Class for the Sword item
 */
public class Gant implements Item{
	/**
	 * ID of the weapon (Every weapon has a unique ID)
	 */
	private XY[] b = new XY[2]; 
	private Direction direction = Direction.UP;
	private Rarity rarity = Rarity.RARE; 
	private int id = 7;
	private int score = 10;
	private final String description = "Des gants pour se protéger du froid hihihi";
	private final String effect = "2AP : Heal 10PV";
	/**
	 * Initialize a sword. 
	 * Since every items has their own shape, we do it manually
	 */
	public Gant() {
		setXY(new XY(0, 0));
	}
	
	/**
	 * Initialize the position of the item at the coordinate in parameter.
	 * The center of the item is where the initialization start.
	 * For example if we call setXY(2, 2), the methods will initialize the item at this coordinate :
	 * 
	 *  o # (2, 3)
	 *  o # (2, 2)
	 *  o # (2, 4)
	 *  
	 *  @param x Coordinate X
	 *  @param y Coordinate Y
	 */
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
	
	/**
	 * Use this item on a enemy
	 * 
	 * @param enemy The enemy
	 * 
	 */
	@Override
	public void use(Enemy enemy, ArrayList<Enemy> lstEnemy) {
		GameDataHero.sub("energy", 2);
		GameDataHero.add("PV", 10);
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
  public String toString() {
  	return "Gant";
  }
  
  @Override
  public String getEffect() {
  	return effect;
  } 
  
 
}



