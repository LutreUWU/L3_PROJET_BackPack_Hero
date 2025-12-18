package model.item.legendary;

import java.util.ArrayList;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public record Axe(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability) implements Item{

		public Axe() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.LEGENDARY, 8, 15, 3);
	  }
	
		public Axe(XY coord, Direction direction, int durability) {
      this(initShape(coord, direction), direction, Rarity.LEGENDARY, 8, 15, durability);
    }

    private static XY[] initShape(XY coord, Direction direction) {
      XY[] b = new XY[4];
      b[0] = new XY(coord.x(), coord.y());
      b[1] = new XY(coord.x(), coord.y() + 1);
      b[2] = new XY(coord.x(), coord.y() - 1);
      b[3] = new XY(coord.x() + 1, coord.y() - 1);

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
    	return new Axe(shape, direction, rarity, ID, score, durability + nb); 
    }
    
    @Override
    public Item subDurability(int nb) {
    	if (nb <= 0) throw new IllegalArgumentException("! Not Negative value !");
    	return new Axe(shape, direction, rarity, ID, score, durability - nb); 
    }
    
    @Override
    public boolean canMerge() {
    	return false;
    }
    
    @Override
    public Axe setXY(XY coord) {
      return new Axe(coord, direction, durability);
    }

    @Override
    public Item use(Enemy enemy, ArrayList<Enemy> lstEnemy, GameData data) {
      GameDataCombat.addLog("Le héro EXPLOSE " + enemy + " avec SA GROSSE HACHE (-10PV)");
      GameDataHero.sub("energy", 1);
      enemy.subHP(10);
      return subDurability(1);
    }
    
    @Override
    public Axe rotateXY() {
      return new Axe(rotate90(shape(), shape()[0]), direction.next(), rarity, ID, score, durability);
    }


    @Override
    public String toString() {
      return "Axe";
    }
}
