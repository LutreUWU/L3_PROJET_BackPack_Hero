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
import model.item.legendary.Axe;
import model.monster.Enemy;

public record Mimicry(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int durability, int AP) implements Item{
		public Mimicry() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.MYTHIC, 5, 100, 2, 2);
	  }
	
		public Mimicry(XY[] shape, Direction direction, int durability) {
      this(shape, direction, Rarity.MYTHIC, 5, 100, durability, 2);
    }
		
		public Mimicry(XY coord, Direction direction, int durability) {
      this(initShape(coord, direction), direction, Rarity.MYTHIC, 5, 100, durability, 2);
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
      GameDataCombat.addLog("Le héro FOUDROIE " + enemy + " (-30PV), mais en échange de 5PV");
      GameDataHero.sub("hp", 5);
      enemy.subHP(30);
      data.hero().sub("energy", AP);
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
