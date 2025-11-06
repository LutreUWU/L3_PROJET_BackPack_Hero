package model;

import java.util.Objects;

public class ManaStone extends ItemBase implements Item {
	private int mana_gain;
	
	public ManaStone(String name, int[][] shape, String rarity, int energy_use, int mana_gain) {
		super(name, shape, rarity, energy_use);
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
		if (isUsable(hero)) {
			hero.add("mana", mana_gain);
			hero.sub("energy", getEnergy_use());
		}
	}
}
