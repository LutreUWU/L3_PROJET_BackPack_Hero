package model.item.rare;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Curse;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.item.ItemStats;
import model.item.legendary.Axe;
import model.monster.Enemy;

public record Gant(XY[] shape, Direction direction, int durability, ItemStats info) implements Item{
	private static final int DURABILITY = 5;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 7;
	private static final int SCORE_VALUE = 10;
	private static final int AP_VALUE = 1;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	public Gant() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS);
  }

	public Gant(XY[] shape, Direction direction, int durability) {
    this(shape, direction, DURABILITY, ITEM_STATS);
  }
	
	public Gant(XY coord, Direction direction, int durability) {
    this(initShape(coord, direction), direction, DURABILITY, ITEM_STATS);
  }

  private static XY[] initShape(XY coord, Direction direction) {
    XY[] b = new XY[2];
    b[0] = new XY(coord.x(), coord.y());
    b[1] = new XY(coord.x(), coord.y() - 1);
    for (int i = 0; i < direction.ordinal(); i++) {
    	b = rotate90(b, b[0]);
    }
    return b;
  }
  
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
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Gant(shape, direction, durability + nb); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new Gant(shape, direction, durability - nb); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public Gant setXY(XY coord) {
    return new Gant(coord, direction, durability);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new Gant(shape, direction, durability);
  }


  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    GameDataCombat.addLog("Le héro porte de super gant ! Il gagne 10 PV");
    GameDataHero.add("hp", 10);
    return subDurability(1);
  }
  
  @Override
  public Gant rotateXY() {
    return new Gant(rotate90(shape(), shape()[0]), direction.next(), durability);
  }


  @Override
  public String toString() {
    return "Gant";
  }
}
