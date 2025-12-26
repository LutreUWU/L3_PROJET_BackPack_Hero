package model.item.epic;

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

public record Shield(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability, int AP) implements Item{
	public Shield() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 14, 30, 10, 1);
  }
	
	public Shield(XY[] shape, Direction direction, int durability) {
		this(shape, direction, Rarity.COMMON, 14, 30, durability, 1);
	}
	
	public Shield(XY coord, Direction direction, int durability) {
    this(initShape(coord, direction), direction, Rarity.COMMON, 14, 30, durability, 1);
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
    return new Shield(coord, direction, durability);
  }
  
  @Override
  public Item usePassive(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
  	Synergy.checkSynergie(data, this);
  	data.hero().add("protection", 1 + Synergy.getBonusDmg());
  	return subDurability(1);
  }

  @Override
  public Item use(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
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
