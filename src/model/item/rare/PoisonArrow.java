package model.item.rare;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Effect;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

/**
 * Represents a PoisonArrow item in the game.
 * 
 * A PoisonArrow is an offensive item that can inflict a poison effect on enemies.
 * It has a position (defined by its shape and coordinates), a facing direction,
 * item statistics, durability, and an associated effect.
 * 
 * Implements the Item interface, so it can be placed in the backpack,
 * rotated, used actively or passively, and interact with other game mechanics.
 * 
 * @param shape array of XY coordinates defining the item's shape in the grid
 * @param direction the current facing direction of the item
 * @param info item statistics (ID, rarity, score, AP, mana, etc.)
 * @param durability number of uses before the item breaks
 * @param effect the effect applied by the PoisonArrow (e.g., POISON)
 */

public record PoisonArrow(XY[] shape, Direction direction, ItemStats info, int durability, Effect effect) implements Item{
	private static final int DURABILITY = 5;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 11;
	private static final int SCORE_VALUE = 15;
	private static final int AP_VALUE = 1;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public PoisonArrow() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS, DURABILITY, Effect.POISON);
  }

	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 * @param durability number of uses before the item breaks
	 * @param effect the effect applied by the PoisonArrow (e.g., POISON)
	 */
	public PoisonArrow(XY[] shape, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(shape);
  	if (durability < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
    this(shape, direction, ITEM_STATS, durability, effect);
  }
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 * @param durability number of uses before the item breaks
	 * @param effect the effect applied by the PoisonArrow (e.g., POISON)
	 */
	public PoisonArrow(XY coord, Direction direction, int durability, Effect effect) {
		Objects.requireNonNull(coord);
  	if (durability < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
    this(initShape(coord, direction), direction, ITEM_STATS, durability, effect);
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
		XY[] rotated = new XY[shape.length];
    for (int i = 0; i < shape.length; i++) {
      int dx = shape[i].x() - pivot.x();
      int dy = shape[i].y() - pivot.y();
      int newX = -dy;
      int newY = dx;
      rotated[i] = new XY(pivot.x() + newX, pivot.y() + newY);
    }
    return rotated;
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
  	return new PoisonArrow(shape, direction, durability + nb, effect); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb < 0) {
  		throw new IllegalArgumentException("! Not Negative value !");
  	}
  	return new PoisonArrow(shape, direction, durability - nb, effect); 
  }
  
  @Override
  public boolean canMerge() {
  	return true;
  }
  
  @Override
  public PoisonArrow setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new PoisonArrow(coord, direction, durability, effect);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	return new PoisonArrow(shape, direction, durability, effect);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	if (Synergy.checkSynergie(data, this)) {
  		var dmg = (int) (6 * (1 + data.hero().getBoostDmg() / 100)); 
  		GameDataCombat.addLog("Vous tirez sur l'ennemi (-" + dmg + "HP) ! Et vous l'empoisonnez !");
      enemy.subHP(dmg);
      enemy.addEffect(effect, 3);
      // Sub durability to the bow
      var bow = data.bag().bagItemLst().stream()
																				.filter(item -> item.info().ID() == 10)
																				.min(Comparator.comparingInt(Item::durability))
																				.orElseThrow();
      																						
      data.bag().removeItemFromBackpack(bow);
      if (bow.durability() - bow.info().AP() > 0) data.bag().addItemToBackpack(bow.subDurability(bow.info().AP()));
      return subDurability(1);
  	}
  	GameDataCombat.addLog("Vous devez avoir un arc pour tirer !");
  	return new PoisonArrow(shape, direction, durability, effect);
  }
  
  @Override
  public PoisonArrow rotateXY() {
    return new PoisonArrow(rotate90(shape(), shape()[0]), direction.next(), durability, effect);
  }

  @Override
  public String toString() {
    return "Flèche empoisonée";
  }
}
