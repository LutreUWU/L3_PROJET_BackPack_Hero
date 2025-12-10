package model.item.epic;

import java.util.ArrayList;

import game.data.GameDataCombat;
import game.data.GameDataHero;
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
	private XY[] b = new XY[4]; 
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
		b[0] = new XY(coord.x(), coord.y());
		b[1] = new XY(coord.x() + 1, coord.y());
		b[2] = new XY(coord.x(), coord.y() + 1);
		b[3] = new XY(coord.x() + 1, coord.y() + 1);
	}
	
	/**
	 * Use this item on a enemy
	 * 
	 * @param enemy The enemy
	 * 
	 */
	@Override
	public void use(Enemy enemy, ArrayList<Enemy> lstEnemy) {
		GameDataCombat.setLog("Le héro gagne 10 Shield, mais au prix de -3 PV ...");
		GameDataHero.sub("energy", 1);
		GameDataHero.add("protection", 10);
		GameDataHero.sub("hp", 3 );
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
  	return "Despair";
  }
  
  @Override
  public String getEffect() {
  	return effect;
  }  
 
  @Override
  public Item copy() {
  	return new DespairShield();
  }
	
}



