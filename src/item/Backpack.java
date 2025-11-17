package item;

import java.util.ArrayList;


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
	private int grid_size;
	private ArrayList<Item_Object> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
	/**
	 * Register the grid size of each tile in the backpack
	 * 
	 * @param gridSize
	 */
	public Backpack(int gridSize) {
		grid_size = gridSize;
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
	public ArrayList<Item_Object> item_lst(){
		return items_list;
	}
	
	/**
	 * Return an ArrayList of all item in the backpack
	 * 
	 * @return ArrayList<Item_Object>
	 */
	public int grid_size(){
		return grid_size;
	}
	
	@Override
	public String toString(){
		var builder = new StringBuilder();
		builder.append("Liste items : \n");
		for (var item : items_list) {
			builder.append(item);
		}
		return builder.toString();
	}
}
