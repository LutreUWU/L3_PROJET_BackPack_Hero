package model;

/**
 * Represents a 2D coordinate in the game grid.
 * 
 * This record stores an (x, y) position, typically used to locate
 * items, entities, or cells within the backpack or game world.
 * 
 * The x coordinate usually represents the column,
 * while the y coordinate represents the row.
 * 
 * @param x the horizontal coordinate
 * @param y the vertical coordinate
 */
public record XY(int x, int y) {
	
	/**
	 * default constructor 
	 * 
   * @throws IllegalArgumentException if x or y is negative
	 */
	public XY{
		if (x < 0) {
	  	throw new IllegalArgumentException("! x Is a negative value !");
		}
		if (y < 0) {
	  	throw new IllegalArgumentException("! y is a negative value !");
		}
	}
}