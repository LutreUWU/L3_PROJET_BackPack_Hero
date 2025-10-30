package model;

public abstract class ItemBase {
	private int[] location; // (x,y) in the backpack
	private int[][] shape; // matrix : 1 = item
	private String name;
	private String rarity;
	private int energy_use;
	

	public ItemBase(int[] location, int[][] shape, String name, String rarity, int energy_use) {
		this.location = location;
		this.shape = shape;
		this.name = name;
		this.rarity = rarity;
		this.energy_use = energy_use;
	}


	@Override
	public String toString() {
		// No loop = "+" :
		return name + " :\n• " + rarity + "\n• Energy needs : " + energy_use;
	}
}
