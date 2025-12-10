package model.item.mythic;

import java.util.ArrayList;

import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.legendary.Axe;
import model.monster.Enemy;

/**
 * Class for the Sword item
 */
public class Mimicry implements Item{
	/**
	 * ID of the weapon (Every weapon has a unique ID)
	 */
	private XY[] b = new XY[3]; 
	private Direction direction = Direction.UP;
	private final Rarity rarity = Rarity.MYTHIC; 
	private final int id = 5;
	private final int score = 100;
	private final String description = "L'épée emblématique de Red Mist, gare à vous !";
	private final String effect = "2AP : Perd 5PV et inflige -30PV à l'ennemi";
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
	@Override
	public void setXY(XY coord) {
		if (direction() == Direction.UP || direction() == Direction.DOWN) {
			b[0] = new XY(coord.x(), coord.y());
			b[1] = new XY(coord.x(), coord.y() - 1);
			b[2] = new XY(coord.x(), coord.y() + 1);
		}
		else {
			b[0] = new XY(coord.x(), coord.y());
			b[1] = new XY(coord.x() - 1, coord.y());
			b[2] = new XY(coord.x() + 1, coord.y());
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
		if (GameDataHero.hero().getEnergy_point() >= 2) {
			GameDataHero.sub("energy", 2);
			GameDataHero.sub("hp", 5);
			enemy.subHP(30);
			GameDataCombat.setLog("En échange de 5PV, " + enemy + " se fait FOUDROYER par Red Mist (-30PV) ");
		}
		else {
			GameDataCombat.setLog("Vous n'avez pas assez d'AP pour utiliser Mimicry ...");
		}
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
  	return "Mimicry";
  }
  
  @Override
  public String getEffect() {
  	return effect;
  } 
  
  @Override
  public Item copy() {
  	return new Mimicry();
  }  
  
 
}