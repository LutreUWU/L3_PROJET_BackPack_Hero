package model;

import java.util.ArrayList;
import item.*;


public class Backpack {
	final private int[][] array_backpack = {  // -2 : not unlock, -1 empty, else ID of item
											    {-2, -2, -2, -2, -2, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -2, -2, -2, -2, -2}
											};
	
	final private ArrayList<Object> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
	public int[][] grid(){
		return array_backpack;
	}
	
	public void add_itemToGrid(Object item, int id) implements Interface{
		var b = 
		for (var block : b) {
			array_backpack[block.y()][block.x()] = id;
		}
		items_list.add();
	}
}
