package model.item.common;

import java.util.List;
import java.util.Objects;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

/**
 * Represents a Sword item in the game.
 * 
 * A Sword has a position (shape and coordinates), a direction, 
 * and item statistics. It implements the Item interface, allowing
 * it to be placed, rotated, and interact with game mechanics.
 * 
 * Item starter when launching the game
 * 
 * @param shape array of XY coordinates defining the item's shape
 * @param direction the current facing direction of the item
 * @param durability the current durability of the item
 * @param info item statistics (ID, rarity, score, AP, mana, etc.)
 */
public record Sword(XY[] shape, Direction direction, int durability, ItemStats info) implements Item{
	private static final int DURABILITY = 99;
	private static final Rarity RARITY_VALUE = Rarity.COMMON;
	private static final int ID_VALUE = 3;
	private static final int SCORE_VALUE = 10;
	private static final int MANA_VALUE = 0;
	private static final int AP_VALUE = 1;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public Sword() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS);
  }
	
	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 * @param durability the current durability of the item
	 */
	public Sword(XY[] shape, Direction direction, int durability) {
		Objects.requireNonNull(shape);
  	if (durability < 0) throw new IllegalArgumentException("! Not Negative value !");
    this(shape, direction, durability, ITEM_STATS);
  }

	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 * @param durability the current durability of the item
	 */
	public Sword(XY coord, Direction direction, int durability) {
		Objects.requireNonNull(coord);
  	if (durability < 0) throw new IllegalArgumentException("! Not Negative value !");
    this(initShape(coord, direction), direction, durability, ITEM_STATS);
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
    XY[] b = new XY[3];
    b[0] = new XY(coord.x(), coord.y());
    b[1] = new XY(coord.x(), coord.y() - 1);
    b[2] = new XY(coord.x(), coord.y() + 1);
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
  public Item addDurability(int nb) {
  	if (nb < 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Sword(shape, direction, durability + nb); 
  }
  
  @Override
  public boolean isConductive() {
  	return true;
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb < 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Sword(shape, direction, durability - nb); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public Sword setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new Sword(coord, direction, durability);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	return new Sword(shape, direction, durability);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
    var dmg = (int) (3 * (1 + data.hero().getBoostDmg() / 100)); 
    enemy.subHP(dmg);
    GameDataCombat.addLog("Le héro tranche " + enemy + " avec l'épée (-" + dmg + "HP)");
    return subDurability(1);
  }
  
  @Override
  public Sword rotateXY() {
    return new Sword(rotate90(shape(), shape()[0]), direction.next(), durability);
  }

  @Override
  public String toString() {
    return "Sword";
  }
}
