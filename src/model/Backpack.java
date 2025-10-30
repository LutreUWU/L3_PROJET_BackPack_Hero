package model;

import java.util.ArrayList;

public class Backpack {
	final private int[][] array_backpack = {{-1, -1, -1, -1, -1},
											{-1, -1, -1, -1, -1},
											{-1, -1, -1, -1, -1}}; // -1 = no item, else ID
	final private ArrayList<Item> items_list = new ArrayList<>(); // List of items I have (Index = ID)
}
