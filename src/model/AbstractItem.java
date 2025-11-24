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
    public void rotateXY(Backpack bag) {
    	int new_x, new_y;
  		int cx = b[0].x(), cy = b[0].y();
  		Block[] b_rotated = b.clone();
  		for (int i = 0; i < b.length; i++) {
  			new_x = (int) Math.round(cx + (b[i].x() - cx) * Math.cos(angle) - (b[i].y() - cy) * Math.sin(angle));
  			new_y = (int) Math.round(cy + (b[i].x() - cx) * Math.sin(angle) + (b[i].y() - cy) * Math.cos(angle));
  			if (new_x < 0 || new_x > 6 || new_y < 0 || new_y > 4 || bag.grid()[new_y][new_x] == -2) { // -2 = Inaccessible
  				return;
  			}
  			b_rotated[i] = new Block(new_x, new_y);
  		}
  		direction = direction.next();
  		b = b_rotated;
    }
}
