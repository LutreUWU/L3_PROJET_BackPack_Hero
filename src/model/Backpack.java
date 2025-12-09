package model;

import java.util.ArrayList;
import java.util.Arrays;


public class Backpack {
	/**
	 * - Backpack
	 * - grid_size
	 * - All items in the bag
	 */
	private int[][] backpack = {  // -2 : not unlock, -1 empty, else ID of item
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -2, -1, -1, -1, -2, -2}
											};
	
	private int gridSize;
	private static ArrayList<Item> bagItemLst = new ArrayList<>(); // List of items I have (Index = ID)
	
	public Item getItem(int x, int y) {
		var itemFromBag = bagItemLst.stream().filter(item -> Arrays.stream(item.shape()).anyMatch(b -> (b.x() == x && b.y() == y))).findFirst().orElse(null);
		return itemFromBag;
	}
	
	/**
	 * Register the grid size of each tile in the backpack
	 * 
	 * @param gridSize
	 */
	public Backpack(int screenHeight) {
		gridSize = screenHeight / 15;
	}
	
	/**
	 * Return the grid of the backpack.
	 * 
	 * @return integer[][] 
	 */
	public int[][] grid(){
		return backpack;
	}
	
	/**
	 * Return an ArrayList of all item in the backpack
	 * 
	 * @return ArrayList<Item_Object>
	 */
	public ArrayList<Item> bagItemLst(){
		return bagItemLst;
	}
	
	/**
	 * Return an ArrayList of all item in the backpack
	 * 
	 * @return ArrayList<Item_Object>
	 */
	public int getGridSize(){
		return gridSize;
	}
	
}
