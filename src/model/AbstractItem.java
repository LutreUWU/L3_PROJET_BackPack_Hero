package model;

import java.util.Objects;

public abstract class AbstractItem implements Item {
    protected Block[] b;
    protected double angle = Math.toRadians(90);
    protected Direction direction = Direction.UP;

    public void create(int item_size) {
        if (item_size < 1) {
            throw new IllegalArgumentException("size invalid");
        }
        b = new Block[item_size];
        for (int i = 0; i < item_size; i++) {
            b[i] = new Block(-1, -1);
        }
    }
    @Override
    public int getWidth() {
      int minX = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;

      for (Block block : b) {
          int x = block.x();
          if (x < minX) minX = x;
          if (x > maxX) maxX = x;
      }

      return maxX - minX + 1;
  }

  @Override
  public int getHeight() {
  	int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;

    for (Block block : b) {
        int y = block.y();
        if (y < minY) minY = y;
        if (y > maxY) maxY = y;
    }

    return maxY - minY + 1;
  }
  
  	/**
  	 * Shape of the weapon
  	 * 
  	 * @return shape of the weapon
  	 */
    @Override
    public Block[] shape() {
        return b;
    }

    @Override
    public Direction direction() {
        return direction;
    }
  	
  	/**
  	 * Rotate the item in the backpack
  	 * 
  	 * @param bag user Backpack
  	 * @throws Objects.requireNonNull if bag is null
  	 */
    @Override
    public void rotateXY() {
    	if (b == null || b.length == 0) return;
      int cx = b[0].x();
      int cy = b[0].y();
      for (int i = 0; i < b.length; i++) {
          int x = b[i].x() - cx;
          int y = b[i].y() - cy;

          // Rotation 90° clockwise : (x, y) -> (-y, x)
          int newX = -y;
          int newY = x;
          b[i] = new Block(cx + newX, cy + newY);
      }
  		direction = direction.next();
    }
}
