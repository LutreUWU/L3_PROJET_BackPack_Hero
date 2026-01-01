package model.item.rare;

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

public record ManaStone(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int maxMana, int value, int AP) implements Item {
	
	public ManaStone(int value) {
  this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.RARE, 16, 15, value, value, 0);
  }

	public ManaStone(XY[] shape, Direction direction, int maxMana, int value) {
		this(shape, direction, Rarity.RARE, 16, 15, maxMana, value, 1);
	}
	
	public ManaStone(XY coord, Direction direction, int maxMana, int value) {
    this(initShape(coord, direction), direction, Rarity.RARE, 16, 15, maxMana, value, 0);
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
  	return true;
  }
  
  @Override
  public ManaStone setXY(XY coord) {
    return new ManaStone(coord, direction, maxMana, value);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new ManaStone(shape, direction, maxMana, value);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	GameDataCombat.addLog("Tu comptes lui jeter du mana dessus ????");
  	return new ManaStone(shape, direction, maxMana, value);
  }
  
  @Override
  public ManaStone rotateXY() {
    return new ManaStone(rotate90(shape(), shape()[0]), direction.next(), maxMana, value);
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
