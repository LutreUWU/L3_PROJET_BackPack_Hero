package model;

import java.util.ArrayList;

public class Backpack {
	final private int[][] array_backpack = {  // -1 : not unlock, 0 empty, else ID of item
											    {-1, -1, -1, -1, -1, -1, -1},
											    {-1, -1,  0,  0,  0, -1, -1},
											    {-1, -1,  0,  0,  0, -1, -1},
											    {-1, -1,  0,  0,  0, -1, -1},
											    {-1, -1, -1, -1, -1, -1, -1}
											}; 
	
	final private ArrayList<Item> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
	public int[][] grid(){
		return array_backpack;
	}
}
