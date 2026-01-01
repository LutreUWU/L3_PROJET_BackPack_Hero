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
import model.monster.Enemy;

public record PoisonArrow(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability, int AP, Effect effect) implements Item{
	public PoisonArrow() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.RARE, 11, 15, 5, 1, Effect.POISON);
  }

	public PoisonArrow(XY[] shape, Direction direction, int durability, Effect effect) {
    this(shape, direction, Rarity.RARE, 11, 15, durability, 1, effect);
  }
	
	public PoisonArrow(XY coord, Direction direction, int durability, Effect effect) {
    this(initShape(coord, direction), direction, Rarity.RARE, 11, 15, durability, 1, effect);
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
  	return new PoisonArrow(shape, direction, durability + nb, effect); 
  }
  
  @Override
  public Item subDurability(int nb) {
  	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
  	return new PoisonArrow(shape, direction, durability - nb, effect); 
  }
  
  @Override
  public boolean canMerge() {
  	return true;
  }
  
  @Override
  public PoisonArrow setXY(XY coord) {
    return new PoisonArrow(coord, direction, durability, effect);
  }
  
  @Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new PoisonArrow(shape, direction, durability, effect);
  }

  @Override
  public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	if (Synergy.checkSynergie(data, this)) {
  		GameDataCombat.addLog("Vous tirez sur l'ennemi (-6HP) ! Et vous l'empoisonnez !");
      enemy.subHP(6);
      enemy.addEffect(effect, 3);
      // Sub durability to the bow
      var bow = data.bag().bagItemLst().stream()
																				.filter(item -> item.ID() == 10)
																				.min(Comparator.comparingInt(Item::durability))
																				.orElseThrow();
      																						
      data.bag().removeItemFromBackpack(bow);
      if (bow.durability() - bow.AP() > 0) data.bag().addItemToBackpack(bow.subDurability(bow.AP()));
      data.hero().sub("energy", AP);
      return subDurability(1);
  	}
  	GameDataCombat.addLog("Vous devez avoir un arc pour tirer !");
  	return new PoisonArrow(shape, direction, durability, effect);
  }
  
  @Override
  public PoisonArrow rotateXY() {
    return new PoisonArrow(rotate90(shape(), shape()[0]), direction.next(), durability, effect);
  }

  @Override
  public String toString() {
    return "Flèche empoisonée";
  }
}
