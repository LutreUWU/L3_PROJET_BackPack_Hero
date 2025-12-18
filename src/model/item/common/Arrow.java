package model.item.common;

import java.util.ArrayList;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.monster.Enemy;

public record Arrow(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability) implements Item{
	public Arrow() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 9, 10, 5);
  }

	public Arrow(XY coord, Direction direction, int durability) {
    this(initShape(coord, direction), direction, Rarity.COMMON, 9, 10, durability);
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
  public Item addDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Arrow(shape, direction, rarity, ID, score, durability + nb); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Arrow(shape, direction, rarity, ID, score, durability - nb); 
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
  public Item use(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
  	if (Synergy.checkSynergie(data, "arrow")) {
  		GameDataCombat.addLog("Vous tirez sur l'ennemi (-8HP) !");
      GameDataHero.sub("energy", 1);
      enemy.subHP(8);
      return subDurability(1);
  	}
  	GameDataCombat.addLog("Vous devez avoir un arc pour tirer !");
  	return new Arrow(shape, direction, rarity, ID, score, durability);
  }
  
  @Override
  public Arrow rotateXY() {
    return new Arrow(rotate90(shape(), shape()[0]), direction.next(), rarity, ID, score, durability);
  }

  @Override
  public String toString() {
    return "Arrow";
  }
}
