package model.weapon;

import java.util.Objects;

import game.data.GameDataHero;
import model.Backpack;
import model.Block;
import model.BlockItem;
import model.Direction;
import model.Object;
import model.monster.Enemy;

/**
 * Class for the Sword item
 */
public class Sword implements Object{
	/**
	 * - Item weapon
	 * - ID of the weapon (Every weapon has a unique ID)
	 */
	private BlockItem weapon = new BlockItem();
	private int id = 1;
	/**
	 * Initialize a sword. 
	 * Since every items has their own shape, we do it manually
	 */
	public Sword() {
		weapon.create(3);
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
	public void setXY(int x, int y) {
		weapon.shape()[0] = new Block(x, y);
		weapon.shape()[1] = new Block(weapon.shape()[0].x(), weapon.shape()[0].y() - 1);
		weapon.shape()[2] = new Block(weapon.shape()[0].x(), weapon.shape()[0].y() + 1);
	}
	
	/**
	 * ID of item
	 * 
	 * @return id 
	 */
	@Override
	public int id() {
		return id;
	}
	
	/**
	 * Shape of the weapon
	 * 
	 * @return shape of the weapon
	 */
	@Override
	public Block[] shape(){
		return weapon.shape();
	}
	
	/**
	 * Rotate the item in the backpack
	 * 
	 * @param bag user Backpack
	 * @throws Objects.requireNonNull if bag is null
	 */
	@Override
	public void rotateXY(Backpack bag) {
		Objects.requireNonNull(bag);
		weapon.rotateXY(bag);
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
		enemy.subHP(3);
	}
	
	@Override
	public Direction direction() {
		return weapon.direction();
	}

}



