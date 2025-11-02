package item;

import java.util.ArrayList;
import item.*;


public class Backpack {
	final private int[][] array_backpack = {  // -2 : not unlock, -1 empty, else ID of item
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -1, -1, -1, -1, -1, -2},
											    {-2, -2, -1, -1, -1, -2, -2}
											};
	
	final private ArrayList<Item_Object> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
	public int[][] grid(){
		/* Return the grid of the backpack.
		 * 
		 * @return integer[][] 
		 */
		return array_backpack;
	}
	
	public ArrayList<Item_Object> all_item(){
		/* Return an ArrayList of all item in the backpack
		 * 
		 * @return ArrayList<Item_Object>
		 */
		return items_list;
	}
	
	private boolean check_place(Item_Object item) {
		/* Check if at the new coordinate, there's another item.
		 * 
		 * @param  item Item we wants to check
		 * @return true if available, else false
		 */
		if (item == null) {
			return false;
		}
		var b = item.shape();
		for (var block : b) {
			 var y = block.y();
			 var x = block.x();
			 if (array_backpack[y][x] != -1){
				 return false;
			 }
		 }
		return true;
	}
	
	public boolean add_ItemToBackpack(Item_Object item){
		/* Modify the grid in the backpack and add it in the list. 
		 * 
		 * @param item  Item we wants to add
		 * @throw IllegalArgumentException if item is null
		 */
		if (check_place(item)) {
			var b = item.shape();
			for (var block : b) {
				array_backpack[block.y()][block.x()] = item.id();
			}
			items_list.add(item);
			return true;
		}
		return false;
	}
	
	public void remove_itemFromBackpack(Item_Object item){
		/* Remove item from the list and modify the grid of backpack in consequence 
		 * 
		 * @param item  Item we wants to remove
		 * @throw IllegalArgumentException if item is null
		 */
		if (item == null) {
	        throw new IllegalArgumentException("item is null");
	    }
		var b = item.shape();
		for (var block : b) {
			array_backpack[block.y()][block.x()] = -1;
		}
		items_list.remove(item);
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
