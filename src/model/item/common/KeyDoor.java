package model.item.common;

import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

/**
 * Represents a KeyDoor item in the game.
 * 
 * A KeyDoor item has a position (defined by its shape and coordinates),
 * a facing direction, and associated item statistics.
 * 
 * Implements the Item interface, so it can be placed in the backpack,
 * rotated, and interact with other game mechanics.
 * 
 * This item is typically used to unlock doors or trigger events
 * within the game.
 * 
 * @param shape the array of XY coordinates that define the item's shape in the grid
 * @param direction the current facing direction of the item
 * @param info the item statistics (ID, rarity, score, AP, mana, etc.)
 */
public record KeyDoor(XY[] shape, Direction direction, ItemStats info) implements Item{		
	private static final Rarity RARITY_VALUE = Rarity.COMMON;
	private static final int ID_VALUE = 1;
	private static final int SCORE_VALUE = -1;
	private static final int MANA_VALUE = 0;
	private static final int AP_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public KeyDoor() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS);
  }
	
	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 */
	public KeyDoor(XY[] shape, Direction direction) {
    this(shape, direction, ITEM_STATS);
  }
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 */
	public KeyDoor(XY coord, Direction direction) {
    this(initShape(coord, direction), direction, ITEM_STATS);
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
    XY[] b = new XY[2];
    b[0] = new XY(coord.x(), coord.y());
    b[1] = new XY(coord.x(), coord.y() - 1);
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
  	return true;
  }
  
  @Override
  public Item addDurability(int nb) {
  	return null; 
  }
  
  @Override
  public Item subDurability(int nb) {
  	return null;
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public KeyDoor setXY(XY coord) {
    return new KeyDoor(coord, direction);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new KeyDoor(shape, direction);
  }


  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	GameDataCombat.addLog("Garde ta clef précieusement au lieu de jouer avec !");
    return new KeyDoor(shape, direction);
  }
  
  @Override
  public int durability() {
    return -1;
  }
  
  @Override
  public KeyDoor rotateXY() {
    return new KeyDoor(rotate90(shape(), shape()[0]), direction.next());
  }


  @Override
  public String toString() {
    return "Key";
  }
}
