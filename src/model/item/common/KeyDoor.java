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
import model.item.ItemStats;
import model.item.legendary.Axe;
import model.monster.Enemy;

public record KeyDoor(XY[] shape, Direction direction, ItemStats info) implements Item{		
	private static final Rarity RARITY_VALUE = Rarity.COMMON;
	private static final int ID_VALUE = 1;
	private static final int SCORE_VALUE = -1;
	private static final int MANA_VALUE = 0;
	private static final int AP_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	public KeyDoor() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS);
	  }
	
		public KeyDoor(XY[] shape, Direction direction) {
      this(shape, direction, ITEM_STATS);
    }
		
		public KeyDoor(XY coord, Direction direction) {
      this(initShape(coord, direction), direction, ITEM_STATS);
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
    	return null; 
    }
    
    @Override
    public Item subDurability(int nb) {
    	return null;
    }
    
    @Override
    public boolean canMerge() {
    	return false;
    }
    
    @Override
    public KeyDoor setXY(XY coord) {
      return new KeyDoor(coord, direction);
    }
    
    @Override
    public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    	return new KeyDoor(shape, direction);
    }


    @Override
    public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
    	GameDataCombat.addLog("Garde ta clef précieusement au lieu de jouer avec !");
      return new KeyDoor(shape, direction);
    }
    
    @Override
    public int durability() {
      return -1;
    }
    
    @Override
    public KeyDoor rotateXY() {
      return new KeyDoor(rotate90(shape(), shape()[0]), direction.next());
    }


    @Override
    public String toString() {
      return "Key";
    }
}
