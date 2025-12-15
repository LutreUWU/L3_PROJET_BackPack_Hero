package model.item.common;

import java.util.ArrayList;

import model.Direction;
import model.Item;
import model.Rarity;
import model.XY;
import model.monster.Enemy;

public record Gold(XY[] shape, Direction direction, Rarity rarity, int ID, int score, int value, int sizeCount) implements Item{
		public Gold(int value) {
	    this(initShape(new XY(0, 0), Direction.UP), Direction.UP, Rarity.COMMON, 2, -1, value, 1);
	  }
	
		public Gold(XY coord, Direction direction, int value) {
      this(initShape(coord, direction), direction, Rarity.COMMON, 2, -1, value, getSizeValue(value));
    }
		
		public Gold(XY[] shape, Direction direction, int value) {
      this(shape, direction, Rarity.COMMON, 2, -1, value, getSizeValue(value));
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
    
    private static int getSizeValue(int value) {
    	if (value <= 15) return 1;
    	else if (value <= 50) return 2;
    	else return 3;
    }
    
    public Gold changeGoldValue(int value2) {
    	int finalValue = value + value2;
    	return new Gold(shape, direction, rarity, ID, score, finalValue, getSizeValue(finalValue));
    }
    
    @Override
    public Gold setXY(XY coord) {
      return new Gold(coord, direction, value);
    }

    @Override
    public void use(Enemy enemy, ArrayList<Enemy> lstEnemy) {
      //
    }
    
    @Override
    public Gold rotateXY() {
      return new Gold(rotate90(shape(), shape()[0]), direction.next(), rarity, ID, score, value, sizeCount);
    }


    @Override
    public String toString() {
      return "Gold";
    }
}
