package model.item.common;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Curse;
import model.Direction;
import model.Effect;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.monster.Enemy;

public record Arrow(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability, int AP) implements Item{
	public Arrow() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 9, 10, 5, 1);
  }
	
	public Arrow(XY[] shape, Direction direction, int durability) {
		this(shape, direction, Rarity.COMMON, 9, 10, durability, 1);
	}
	
	public Arrow(XY coord, Direction direction, int durability) {
    this(initShape(coord, direction), direction, Rarity.COMMON, 9, 10, durability, 1);
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
  public Item usePassive(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
  	return new Arrow(shape, direction, durability);
  }


  @Override
  public Item use(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
  	if (Synergy.checkSynergie(data, this)) {
  		GameDataCombat.addLog("Vous tirez sur l'ennemi (-8HP) !");
      enemy.subHP(8);
      // Sub durability to the bow
      var bow = data.bag().bagItemLst().stream()
																				.filter(item -> item.ID() == 10)
																				.min(Comparator.comparingInt(Item::durability))
																				.orElseThrow();
      																						
      data.bag().removeItemFromBackpack(bow);
      if (bow.durability() - bow.AP() > 0) data.bag().addItemToBackpack(bow.subDurability(bow.AP()));
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
