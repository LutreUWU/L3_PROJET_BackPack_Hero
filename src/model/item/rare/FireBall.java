package model.item.rare;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
import model.item.ItemStats;
import model.monster.Enemy;

public record FireBall(XY[] shape, Direction direction, int durability, ItemStats info, Effect effect) implements Item{
	private static final int DURABILITY = 2;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 15;
	private static final int SCORE_VALUE = 20;
	private static final int AP_VALUE = 2;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	public FireBall() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS, Effect.FIRE);
  }

	public FireBall(XY[] shape, Direction direction, int durability, Effect effect) {
    this(shape, direction, durability, ITEM_STATS, effect);
  }
	
	public FireBall(XY coord, Direction direction, int durability, Effect effect) {
    this(initShape(coord, direction), direction, durability, ITEM_STATS, effect);
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
  	return false;
  }
  
  @Override
  public Item addDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new FireBall(shape, direction, durability + nb, effect); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new FireBall(shape, direction, durability - nb, effect); 
  }
  
  @Override
  public boolean canMerge() {
  	return false;
  }
  
  @Override
  public FireBall setXY(XY coord) {
    return new FireBall(coord, direction, durability, effect);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new FireBall(shape, direction, durability, effect);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
		GameDataCombat.addLog("Tous les ennemies perdent de la vie (-6HP) et sont brulés");
		for (var target : lstEnemy) {
			target.subHP(6);
			target.addEffect(effect, 3);
		}
    return subDurability(1);
  }
  
  @Override
  public FireBall rotateXY() {
    return new FireBall(rotate90(shape(), shape()[0]), direction.next(), durability, effect);
  }

  @Override
  public String toString() {
    return "Boule de feu";
  }
}
