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
import model.item.legendary.Axe;
import model.monster.Enemy;

public record Gold(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int value, int AP) implements Item{
	
	public Gold(int value) {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 2, -1, value, 0);
  }

	public Gold(XY[] shape, Direction direction, int value) {
		this(shape, direction, Rarity.COMMON, 2, -1, value, 1);
	}
	
	public Gold(XY coord, Direction direction, int value) {
    this(initShape(coord, direction), direction, Rarity.COMMON, 2, -1, value, 0);
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
  
  public Gold changeGoldValue(int value2) {
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
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new Curse(shape, direction, value);
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
