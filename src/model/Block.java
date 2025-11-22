package model;

/**
 * Block is what all items is made.
 * For example if an item take 3 blocks in the backpack,
 * then we create 3 blocks, and each blocks has his own coordinate (x, y)
 * 
 * @param x Coordinate x 
 * @param y Coordinate y
 */
public record Block(int x, int y) {
	@Override
	public String toString(){
		return "x : " + x + ", y : " + y;
	}
}
