package model.item.rare;

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

public record ManaStone(XY[] shape, Direction direction, ItemStats info, int value) implements Item {
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 16;
	private static final int SCORE_VALUE = 15;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, 0, 0);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public ManaStone(int value) {
		this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS, value);
  }

	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 */
	public ManaStone(XY[] shape, Direction direction, int value) {
		Objects.requireNonNull(shape);
  	if (value <= 0) throw new IllegalArgumentException("! Not Negative value !");
		this(shape, direction, ITEM_STATS, value);
	}
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 */
	public ManaStone(XY coord, Direction direction, int value) {
		Objects.requireNonNull(coord);
  	if (value <= 0) throw new IllegalArgumentException("! Not Negative value !");
    this(initShape(coord, direction), direction, ITEM_STATS, value);
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
  public ManaStone setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new ManaStone(coord, direction, value);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	return new ManaStone(shape, direction, value);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	GameDataCombat.addLog("Tu comptes lui jeter du mana dessus ????");
  	return new ManaStone(shape, direction, value);
  }
  
  @Override
  public ManaStone rotateXY() {
    return new ManaStone(rotate90(shape(), shape()[0]), direction.next(), value);
  }
  
  @Override
  public int durability() {
    return -1;
  }


  @Override
  public String toString() {
    return "ManaStone";
  }
}
