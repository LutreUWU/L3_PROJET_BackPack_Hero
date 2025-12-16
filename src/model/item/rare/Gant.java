package model.item.rare;

import java.util.ArrayList;

import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public record Gant(XY[] shape, Direction direction, Rarity rarity, int ID, int score) implements Item{
		public Gant() {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.RARE, 7, 10);
	  }
	
		public Gant(XY coord, Direction direction) {
      this(initShape(coord, direction), direction, Rarity.RARE, 7, 10);
    }
		
		public Gant(XY[] shape, Direction direction) {
      this(shape, direction, Rarity.RARE, 7, 10);
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
    public Gant setXY(XY coord) {
      return new Gant(coord, direction);
    }

    @Override
    public void use(Enemy enemy, ArrayList<Enemy> lstEnemy) {
      GameDataCombat.addLog("Le héro porte de super gant ! Il gagne 10 PV");
      GameDataHero.sub("energy", 1);
      GameDataHero.add("hp", 10);
      enemy.subHP(30);
    }
    
    @Override
    public Gant rotateXY() {
      return new Gant(rotate90(shape(), shape()[0]), direction.next(), rarity, ID, score);
    }


    @Override
    public String toString() {
      return "Gant";
    }
}
