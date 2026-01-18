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
 * Represents a FireBall item in the game.
 * 
 * A FireBall has a position (defined by its shape and coordinates),
 * a facing direction, a durability value, item statistics, and an associated effect.
 * 
 * Implements the Item interface, so it can be placed in the backpack,
 * used in combat, rotated, and interact with other game mechanics.
 * 
 * Each FireBall instance is immutable (as it is a record), so using or rotating
 * it will return a new instance rather than modifying the original.
 * 
 * @param shape 			the array of XY coordinates that define the item's shape in the grid
 * @param direction 	the current facing direction of the item
 * @param durability	the current durability of the item
 * @param info 				the item statistics (ID, rarity, score, AP, mana, etc.)
 * @param effect 			the effect applied by this item (e.g., FIRE)
 */
public record FireBall(XY[] shape, Direction direction, int durability, ItemStats info, Effect effect) implements Item{
	private static final int DURABILITY = 2;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 15;
	private static final int SCORE_VALUE = 20;
	private static final int AP_VALUE = 2;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public FireBall() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS, Effect.FIRE);
  }

	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 */
	public FireBall(XY[] shape, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(shape);
  	if (durability < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
		this(shape, direction, durability, ITEM_STATS, effect);
  }
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 */
	public FireBall(XY coord, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(coord);
  	if (durability < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
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
  	if (nb < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
  	return new FireBall(shape, direction, durability + nb, effect); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
  	return new FireBall(shape, direction, durability - nb, effect); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public FireBall setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new FireBall(coord, direction, durability, effect);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	return new FireBall(shape, direction, durability, effect);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	var dmg = (int) (6 * (1 + data.hero().getBoostDmg() / 100)); 
		GameDataCombat.addLog("Tous les ennemies perdent de la vie (-" + dmg + "HP) et sont brulés");
		for (var target : lstEnemy) {
			target.subHP(dmg);
			target.addEffect(effect, 3);
		}
    return subDurability(1);
  }
  
  @Override
  public FireBall rotateXY() {
    return new FireBall(rotate90(shape(), shape()[0]), direction.next(), durability, effect);
  }

  @Override
  public String toString() {
    return "Boule de feu";
  }
}
