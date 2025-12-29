package model;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public record Curse(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability, int AP) implements Item{

		public Curse() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 13, 0, 1, 3);
	  }
		public Curse(XY[] shape, Direction direction, int durability) {
      this(shape, direction, Rarity.COMMON, 13, 0, durability, 3);
    }
		
		public Curse(XY coord, Direction direction, int durability) {
      this(initShape(coord, direction), direction, Rarity.COMMON, 13, 0, durability, 3);
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
