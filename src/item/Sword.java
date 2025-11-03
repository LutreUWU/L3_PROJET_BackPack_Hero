package item;

import java.util.Objects;

/**
 * Class for the Sword item
 */
public class Sword implements Item_Object{
	/**
	 * Item weapon
	 */
	private Item weapon = new Item();
	/**
	 * ID of the weapon.
	 * Every weapon has a unique ID
	 */
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
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Name : Sword\n").append("ID : 1\n");
		for (var block : weapon.shape()) {
			builder.append(block).append("\n");
		}
		
		return builder.toString();
	}
}



