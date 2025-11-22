package model.map;

public class Floor {
	/**
	 * - Backpack
	 * - grid_size
	 * - All items in the bag
	 */
	private int[][] map = {
	    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
	    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	private int grid_size;	
	
	/**
	 * Register the grid size of each tile in the backpack
	 * 
	 * @param gridSize
	 */
	public Floor(int gridSize) {
		grid_size = gridSize;
	}
	
	/**
	 * Return the grid of the map.
	 * 
	 * @return int[][] 
	 */
	public int[][] grid(){
		return map;
	}
	
	/**
	 * Return the size of a grid map
	 * 
	 * @return Int
	 */
	public int grid_size(){
		return grid_size;
	}
	
	/**
	 * Create the map depending of the floor level
	 * 
	 * @param floor
	 */
	public void create_floor(int floor) {
		// TO DO
		
		// Quelques indications :
		// - Dans chaque floor, on a des marchands, guerisseurs ... ne les oublies pas
		// - Plus on monte dans les étages, plus le nombre d'ennemi augmente,
		// J'ai plus d'autres idées bonne chance mdr
	}
}
