package model;

import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import model.item.ItemStats;
import model.monster.Enemy;

public record Curse(XY[] shape, Direction direction, ItemStats info, int durability) implements Item{
	private static final int DURABILITY = 1;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 13;
	private static final int SCORE_VALUE = 0;
	private static final int AP_VALUE = 3;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
		public Curse() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS, DURABILITY);
	  }
		public Curse(XY[] shape, Direction direction, int durability) {
      this(shape, direction, ITEM_STATS, durability);
    }
		
		public Curse(XY coord, Direction direction, int durability) {
      this(initShape(coord, direction), direction, ITEM_STATS, durability);
    }
    private static XY[] initShape(XY coord, Direction direction) {
      XY[] b = new XY[4];
      b[0] = new XY(coord.x(), coord.y());
      b[1] = new XY(coord.x(), coord.y() + 1);
      b[2] = new XY(coord.x() + 1, coord.y());
      b[3] = new XY(coord.x() - 1, coord.y() + 1);

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
    	return false;
    }
    
    @Override
    public Item addDurability(int nb) {
    	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
    	return new Curse(shape, direction, durability + nb); 
    }
    
    @Override
    public Item subDurability(int nb) {
    	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
    	return new Curse(shape, direction, durability - nb); 
    }
    
    @Override
    public boolean canMerge() {
    	return false;
    }
    
    @Override
    public Curse setXY(XY coord) {
      return new Curse(coord, direction, durability);
    }
    
    @Override
    public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    	return new Curse(shape, direction, durability);
    }

    @Override
    public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
      GameDataCombat.addLog("MUAAAHAAAHAAA ! J'espère que tu as aimé la malédiction !");
      return subDurability(1);
    }
    
    @Override
    public Curse rotateXY() {
      return new Curse(shape, direction, durability); 
    }

    @Override
    public String toString() {
      return "Malédiction";
    }
}
