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
 * If x or y is negative, it means that we're outside of the backpack (when dragging item on the screen)
 * 
 * @param x the horizontal coordinate
 * @param y the vertical coordinate
 */
public record XY(int x, int y) {
	
	/**
	 * default constructor 
	 */
	public XY{}
}