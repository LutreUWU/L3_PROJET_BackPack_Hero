package model;

import model.monster.Enemy;

/**
 * Since every Item has a different shape, ID, setXY, use
 * we use an interface.
 * 
 * They all have the same rotate function, but we put here to avoid copy paste in every function.
 */
public interface Item {
	Block[] shape();
  int id();
	void setXY(XY coord);
	void rotateXY();
	void use(Enemy enemy);
	Direction direction();
	int getWidth();
	int getHeight();
}
