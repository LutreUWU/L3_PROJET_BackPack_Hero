package model.item.common;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import model.Curse;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.ItemStats;
import model.item.legendary.Axe;
import model.monster.Enemy;

public record Gold(XY[] shape, Direction direction, ItemStats info, int value) implements Item {
	private static final Rarity RARITY_VALUE = Rarity.COMMON;
	private static final int ID_VALUE = 2;
	private static final int SCORE_VALUE = -1;
	private static final int AP_VALUE = 1;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	public Gold(int value) {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS, value);
  }

	public Gold(XY[] shape, Direction direction, int value) {
		this(shape, direction, ITEM_STATS,value);
	}
	
	public Gold(XY coord, Direction direction, int value) {
    this(initShape(coord, direction), direction, ITEM_STATS, value);
  }

  private static XY[] initShape(XY coord, Direction direction) {
    XY[] b = new XY[1];
    b[0] = new XY(coord.x(), coord.y());
    for (int i = 0; i < direction.ordinal(); i++) {
    	b = rotate90(b, b[0]);
    }
    return b;
  }
  
  private static XY[] rotate90(XY[] shape, XY pivot) {
    return shape;
  }
  
  public Gold addGoldValue(int value2) {
  	if (value2 < 0) throw new IllegalArgumentException("! MUST BE A POSITIVE VALUE !");
  	int finalValue = value + value2;
  	return new Gold(shape, direction, finalValue);
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
  	return true;
  }
  
  @Override
  public Gold setXY(XY coord) {
    return new Gold(coord, direction, value);
  }
  
  @Override
  public boolean isConductive() {
  	return true;
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new Gold(shape, direction, value);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	GameDataCombat.addLog("Tu comptes lui jeter des pièces dessus ????");
  	return new Gold(shape, direction, value);
  }
  
  @Override
  public Gold rotateXY() {
    return new Gold(rotate90(shape(), shape()[0]), direction.next(), value);
  }
  
  @Override
  public int durability() {
    return -1;
  }


  @Override
  public String toString() {
    return "Gold";
  }
}
