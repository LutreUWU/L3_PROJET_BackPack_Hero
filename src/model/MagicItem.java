package model;

public class MagicItem extends ItemBase implements Item {
	private int protection;
	private int damage;
	private int mana_use;
	
	public MagicItem(String name, int[] location, int[][] shape, String rarity, int energy_use, int mana_use, int protection, int damage) {
		super(location, shape, name, rarity, energy_use);
		this.protection = protection;
		this.damage = damage;
		this.mana_use = mana_use;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Mana needs : " + mana_use + "\n• Protection : " + protection + "\n• Damage : " + damage + "\n";
	}
}
