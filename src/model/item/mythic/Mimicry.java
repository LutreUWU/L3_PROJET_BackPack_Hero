package model.item.mythic;

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

public record Mimicry(XY[] shape, Direction direction, int durability, ItemStats info) implements Item{
	private static final int DURABILITY = 2;
	private static final Rarity RARITY_VALUE = Rarity.MYTHIC;
	private static final int ID_VALUE = 5;
	private static final int SCORE_VALUE = 100;
	private static final int AP_VALUE = 2;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	
	public Mimicry() {
    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, DURABILITY, ITEM_STATS);
  }
	
		public Mimicry(XY[] shape, Direction direction, int durability) {
      this(shape, direction, durability, ITEM_STATS);
    }
		
		public Mimicry(XY coord, Direction direction, int durability) {
      this(initShape(coord, direction), direction, durability, ITEM_STATS);
    }

    private static XY[] initShape(XY coord, Direction direction) {
      XY[] b = new XY[3];
      b[0] = new XY(coord.x(), coord.y());
      b[1] = new XY(coord.x(), coord.y() - 1);
      b[2] = new XY(coord.x(), coord.y() + 1);
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
    	return new Mimicry(shape, direction, durability + nb); 
    }
    
    @Override
    public Item subDurability(int nb) {
    	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
    	return new Mimicry(shape, direction, durability - nb); 
    }
    
    @Override
    public boolean canMerge() {
    	return false;
    }
    
    @Override
    public Mimicry setXY(XY coord) {
      return new Mimicry(coord, direction, durability);
    }
    
    @Override
    public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    	return new Mimicry(shape, direction, durability);
    }


    @Override
    public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    	var dmg = (int) (30 * (1 + data.hero().getBoostDmg() / 100));
      GameDataCombat.addLog("Le héro FOUDROIE " + enemy + " (-" + dmg + "HP), mais en échange de 5PV");
      GameDataHero.sub("hp", 5);
      enemy.subHP(dmg);
      return subDurability(1);
    }
    
    @Override
    public Mimicry rotateXY() {
      return new Mimicry(rotate90(shape(), shape()[0]), direction.next(), durability);
    }


    @Override
    public String toString() {
      return "Mimicry";
    }
}
