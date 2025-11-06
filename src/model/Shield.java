package model;

import java.util.Objects;

public class Shield extends ItemBase implements Item {
	private int protection;
	
	public Shield(String name, int[][] shape, String rarity, int energy_use, int protection) {
		super(name, shape, rarity, energy_use);
		this.protection = protection;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Protection : " + protection + "\n";
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
			hero.add("protection", protection);
			hero.sub("energy", getEnergy_use());
		}
	}
}
