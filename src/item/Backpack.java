package item;

import java.util.ArrayList;


public class Backpack {
	/**
	 * Backpack
	 */
	private int[][] backpack = {  // -2 : not unlock, -1 empty, else ID of item
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -2, -1, -1, -1, -2, -2}
											};
	/**
	 * All items in the bag
	 */
	private ArrayList<Item_Object> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
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
