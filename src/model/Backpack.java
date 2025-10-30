package model;

import java.util.ArrayList;

public class Backpack {
	final private int[][] array_backpack = {  // -2 : not unlock, -1 empty, else ID of item
											    {-2, -2, -2, -2, -2, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -1, -1, -1, -2, -2},
											    {-2, -2, -2, -2, -2, -2, -2}
											};
	
	final private ArrayList<Item> items_list = new ArrayList<>(); // List of items I have (Index = ID)
	
	public int[][] grid(){
		return array_backpack;
	}
}
