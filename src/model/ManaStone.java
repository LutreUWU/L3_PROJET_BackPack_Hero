package model;

import java.util.Objects;

public class ManaStone implements Item {
	private int mana_gain;
	private ItemBase stats_base;
	
	public ManaStone(String name, int[][] shape, String rarity, int energy_use, int mana_gain, Direction direction) {
		this.stats_base = new ItemBase(name, shape, rarity, energy_use, direction);
		this.mana_gain = mana_gain;
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
			hero.add("mana", mana_gain);
			hero.sub("energy", stats_base.energy_use());
		}
	}
}
