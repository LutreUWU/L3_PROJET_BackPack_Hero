package model.item.rare;

import java.util.List;
import java.util.Objects;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Effect;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

/**
 * Represents a Cookie item in the game.
 * 
 * A Cookie item has a position (defined by its shape and coordinates),
 * a facing direction, durability, item statistics, and an associated effect.
 * It implements the Item interface, so it can be placed in the backpack,
 * rotated, and interact with other game mechanics.
 * 
 * This item can boost damage 
 * 
 * @param shape array of XY coordinates defining the item's shape in the grid
 * @param direction the current facing direction of the item
 * @param durability the current durability of the item
 * @param info item statistics (ID, rarity, score, AP, mana, etc.)
 * @param effect the effect associated with the item (e.g., FIRE, POISON)
 */

public record Cookie(XY[] shape, Direction direction, int durability, ItemStats info, Effect effect) implements Item{
	private static final int DURABILITY = 1;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 18;
	private static final int SCORE_VALUE = 20;
	private static final int AP_VALUE = 0;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public Cookie() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS, Effect.FIRE);
  }

	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 */
	public Cookie(XY[] shape, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(shape);
  	if (durability < 0) throw new IllegalArgumentException("! Not Negative value !");
		this(shape, direction, durability, ITEM_STATS, effect);
  }
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 */
	public Cookie(XY coord, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(coord);
  	if (durability <= 0) throw new IllegalArgumentException("! Not Negative value !");
		this(initShape(coord, direction), direction, durability, ITEM_STATS, effect);
  }

	/**
	 * Initializes the shape of the item based on a pivot coordinate and a direction.
	 * The shape is rotated clockwise according to the direction ordinal.
	 *
	 * @param coord 		The pivot coordinate of the item
	 * @param direction The initial orientation of the item
	 * @return an array of grid coordinates representing the item shape
	 */
	private static XY[] initShape(XY coord, Direction direction) {
    XY[] b = new XY[1];
    b[0] = new XY(coord.x(), coord.y());
    for (int i = 0; i < direction.ordinal(); i++) {
    	b = rotate90(b, b[0]);
    }
    return b;
  }
  
	/**
	 * Rotates the given shape by 90 degrees clockwise around a pivot point.
	 *
	 * @param shape The current shape coordinates
	 * @param pivot The rotation pivot
	 * @return the rotated shape
	 */
	private static XY[] rotate90(XY[] shape, XY pivot) {
    return shape;
  }
	
	@Override
  public boolean isConductive() {
  	return false;
  }
  
  @Override
  public Item addDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Cookie(shape, direction, durability + nb, effect); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Cookie(shape, direction, durability - nb, effect); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public Cookie setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new Cookie(coord, direction, durability, effect);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	data.hero().addBoostDmg(10);
  	return new Cookie(shape, direction, durability, effect);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	GameDataCombat.addLog("Ce cookie est congelé, il se mange pas...");
    return new Cookie(shape, direction, durability, effect);
  }
  
  @Override
  public Cookie rotateXY() {
    return new Cookie(rotate90(shape(), shape()[0]), direction.next(), durability, effect);
  }

  @Override
  public String toString() {
    return "Cookie";
  }
}
