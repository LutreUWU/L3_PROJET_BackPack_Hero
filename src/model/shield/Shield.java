package model.shield;

import java.util.Objects;

public class Shield implements BlockItem {
	private int protection;
	private ItemBase stats_base;
	
	public Shield(String name, int[][] shape, String rarity, int energy_use, int protection, Direction direction) {
		this.stats_base = new ItemBase(name, shape, rarity, energy_use, direction);
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
		if (stats_base.isUsable(hero)) {
			hero.add("protection", protection);
			hero.sub("energy", stats_base.energy_use());
		}
	}
}
