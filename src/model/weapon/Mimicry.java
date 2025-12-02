package model.weapon;

import game.data.GameDataHero;
import model.Block;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

/**
 * Class for the Sword item
 */
public class Mimicry implements Item{
	/**
	 * ID of the weapon (Every weapon has a unique ID)
	 */
	private Block[] b = new Block[3]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = Rarity.MYTHIC; 
	private final int id = 3;
	private final int score = 100;
	/**
	 * Initialize a sword. 
	 * Since every items has their own shape, we do it manually
	 */
	public Mimicry() {
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
	public void setXY(XY coord) {
		if (direction() == Direction.UP || direction() == Direction.DOWN) {
			b[0] = new Block(coord.x(), coord.y());
			b[1] = new Block(coord.x(), coord.y() - 1);
			b[2] = new Block(coord.x(), coord.y() + 1);
		}
		else {
			b[0] = new Block(coord.x(), coord.y());
			b[1] = new Block(coord.x() - 1, coord.y());
			b[2] = new Block(coord.x() + 1, coord.y());
		}
	}
	
	/**
	 * Use this item on a enemy
	 * 
	 * @param enemy The enemy
	 * 
	 */
	@Override
	public void use(Enemy enemy) {
		GameDataHero.sub("energy", 2);
		GameDataHero.sub("hp", 5);
		enemy.subHP(30);
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
  
  
 
}



