package model;

import java.util.Objects;

public class Armor extends ItemBase implements Item {
	private int protection_armor;
	
	public Armor(String name, int[][] shape, String rarity, int energy_use, int protection_armor) {
		super(name, shape, rarity, energy_use);
		this.protection_armor = protection_armor;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Protection : " + protection_armor + "\n";
	}
	
	@Override
	public void use(Hero hero, Object unused) {
		/**
		 * Function to use the shield
		 * @param hero : The hero
		 * @param unused
		 */
		Objects.requireNonNull(hero);
		if (isUsable(hero)) {
			hero.add("armor", protection_armor);
			hero.sub("energy", getEnergy_use());
		}
	}
}
