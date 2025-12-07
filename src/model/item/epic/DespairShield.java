package model.item.epic;

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
public class DespairShield implements Item{
	/**
	 * ID of the weapon (Every weapon has a unique ID)
	 */
	private Block[] b = new Block[4]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = Rarity.EPIC; 
	private final int id = 4;
	private final int score = 10;
	private final String description = "Un bouclier qui représente l'espoir et le desespoir";
	private final String effect = "1AP : Perd 3PV et gagne 10 Shield";
	/**
	 * Initialize a sword. 
	 * Since every items has their own shape, we do it manually
	 */
	public DespairShield() {
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
//		var currentDir = direction;
//		direction = Direction.UP;
		b[0] = new Block(coord.x(), coord.y());
		b[1] = new Block(coord.x() + 1, coord.y());
		b[2] = new Block(coord.x(), coord.y() + 1);
		b[3] = new Block(coord.x() + 1, coord.y() + 1);
//		for (int i = 0; i < currentDir.ordinal(); i++) {
//			rotateXY();
//		}
//		IO.println("OUIUI");
//		IO.println(b[0]);
//		IO.println(b[1]);
//		IO.println(b[2]);
//		IO.println(b[3]);
//		IO.println("OUIUI");
	}
	
	/**
	 * Use this item on a enemy
	 * 
	 * @param enemy The enemy
	 * 
	 */
	@Override
	public void use(Enemy enemy) {
		GameDataHero.sub("energy", 1);
		GameDataHero.add("shield", 10);
		GameDataHero.sub("hp", 3 );
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
  public String getDescription() {
		return description;
	}
  
  @Override
  public String toString() {
  	return "Despair";
  }
  
  @Override
  public String getEffect() {
  	return effect;
  }  
 
}



