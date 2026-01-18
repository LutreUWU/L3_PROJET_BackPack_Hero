package model.item.epic;

import java.util.List;
import java.util.Objects;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

public record Shield(XY[] shape, Direction direction, int durability, ItemStats info) implements Item{
	private static final int DURABILITY = 10;
	private static final Rarity RARITY_VALUE = Rarity.EPIC;
	private static final int ID_VALUE = 14;
	private static final int SCORE_VALUE = 30;
	private static final int AP_VALUE = 1;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default item positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public Shield() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS);
  }
	
	/**
	 * Creates an item with a predefined shape, direction and durability.
	 * The item stats are automatically set to the item default stats.
	 *
	 * @param shape 		  The grid cells occupied by the item
	 * @param direction 	The orientation of the item
	 */
	public Shield(XY[] shape, Direction direction, int durability) {
		Objects.requireNonNull(shape);
  	if (durability <= 0) throw new IllegalArgumentException("! Not Negative value !");
		this(shape, direction, durability, ITEM_STATS);
	}
	
	/**
	 * Creates an item at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the item
	 * @param direction  The orientation of the item
	 */
	public Shield(XY coord, Direction direction, int durability) {
		Objects.requireNonNull(coord);
  	if (durability <= 0) throw new IllegalArgumentException("! Not Negative value !");
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
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Shield(shape, direction, durability + nb); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Shield(shape, direction, durability - nb); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public Shield setXY(XY coord) {
  	Objects.requireNonNull(coord);
    return new Shield(coord, direction, durability);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	Synergy.checkSynergie(data, this);
  	data.hero().add("protection", 1 + Synergy.getBonusDmg());
  	return subDurability(1);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	Objects.requireNonNull(enemy);
  	Objects.requireNonNull(lstEnemy);
  	Objects.requireNonNull(data);
  	GameDataCombat.addLog("Cet objet est seulement passif ! On ne peut pas l'utiliser !");
  	return new Shield(shape, direction, durability);
  }
  
  @Override
  public Shield rotateXY() {
    return new Shield(rotate90(shape(), shape()[0]), direction.next(), durability);
  }

  @Override
  public String toString() {
    return "Shield";
  }
}
