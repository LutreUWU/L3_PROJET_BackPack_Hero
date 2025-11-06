package model;

import java.util.Objects;

public class MagicItem extends ItemBase implements Item {
	private int protection;
	private int protection_armor;
	private int damage;
	private int mana_use;
	
	public MagicItem(String name, int[][] shape, String rarity, int energy_use, int mana_use, int protection, int damage) {
		super(name, shape, rarity, energy_use);
		this.protection = protection;
		this.damage = damage;
		this.mana_use = mana_use;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Mana needs : " + mana_use + "\n• Protection : " + protection + "\n• Damage : " + damage + "\n";
	}
	
	
	@Override
	public void use(Hero hero, Object unused) {
		/**
		 * Function to use the shield
		 * @param hero : The hero
		 * @param unused
		 */
		Objects.requireNonNull(hero);
		if (isUsable(hero) && mana_use >= hero.getManaPoint()) {
			hero.add("armor", protection_armor);
			hero.add("protection", protection);
			hero.sub("mana", mana_use);
			hero.sub("energy", getEnergy_use());
		}
	}
}
