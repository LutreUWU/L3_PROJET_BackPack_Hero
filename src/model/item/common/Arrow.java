package model.item.common;

import java.util.Comparator;
import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import model.Direction;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

public record Arrow(XY[] shape, Direction direction, int durability, ItemStats info) implements Item{
	private static final int DURABILITY = 3;
	private static final Rarity RARITY_VALUE = Rarity.COMMON;
	private static final int ID_VALUE = 9;
	private static final int SCORE_VALUE = 10;
	private static final int AP_VALUE = 1;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	/**
	 * Creates a default Arrow positioned at (0, 0),
	 * oriented upwards, with its default durability and item stats.
	 */
	public Arrow() {
		this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS);
  }
	
	/**
	 * Creates an Arrow with a predefined shape, direction and durability.
	 * The item stats are automatically set to the Arrow default stats.
	 *
	 * @param shape 		  The grid cells occupied by the arrow
	 * @param direction 	The orientation of the arrow
	 * @param durability  The current durability of the arrow
	 */
	public Arrow(XY[] shape, Direction direction, int durability) {
		this(shape, direction, durability, ITEM_STATS);
	}
	
	/**
	 * Creates an Arrow at the given grid coordinate, oriented in the given direction,
	 * with the specified durability.
	 *
	 * @param coord 		 The pivot coordinate of the arrow
	 * @param direction  The orientation of the arrow
	 * @param durability The current durability of the arrow
	 */
	public Arrow(XY coord, Direction direction, int durability) {
    this(initShape(coord, direction), direction, durability, ITEM_STATS);
  }
	
	/**
	 * Initializes the shape of the arrow based on a pivot coordinate and a direction.
	 * The shape is rotated clockwise according to the direction ordinal.
	 *
	 * @param coord 		The pivot coordinate of the arrow
	 * @param direction The initial orientation of the arrow
	 * @return an array of grid coordinates representing the arrow shape
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
  public Item addDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Arrow(shape, direction, durability + nb); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Arrow(shape, direction, durability - nb); 
  }
  
  @Override
  public boolean canMerge() {
  	return true;
  }
  
  @Override
  public Arrow setXY(XY coord) {
    return new Arrow(coord, direction, durability);
  }
  
  @Override
  public boolean isConductive() {
  	return false;
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new Arrow(shape, direction, durability);
  }
  

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	if (Synergy.checkSynergie(data, this)) {
  		var dmg = (int) (8 * ( 1 + data.hero().getBoostDmg() / 100)); 
  		enemy.subHP(dmg);
  		GameDataCombat.addLog("Vous tirez sur l'ennemi (-" + dmg + "HP) !");
  		
      
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
  	return new Arrow(shape, direction, durability);
  }
  
  @Override
  public Arrow rotateXY() {
    return new Arrow(rotate90(shape(), shape()[0]), direction.next(), durability);
  }

  @Override
  public String toString() {
    return "Arrow";
  }
}
