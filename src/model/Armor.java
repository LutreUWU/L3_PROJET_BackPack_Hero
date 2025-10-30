package model;

public class Armor extends ItemBase implements Item {
	private int protection;
	
	public Armor(String name, int[] location, int[][] shape, String rarity, int energy_use, int protection) {
		super(location, shape, name, rarity, energy_use);
		this.protection = protection;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Protection : " + protection + "\n";
	}
}
