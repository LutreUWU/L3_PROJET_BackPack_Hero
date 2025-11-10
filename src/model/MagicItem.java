package model;

import java.util.Objects;

public class MagicItem implements Item {
	private int protection;
	private int protection_armor;
	private int damage;
	private int mana_use;
	private ItemBase stats_base;
	
	public MagicItem(String name, int[][] shape, String rarity, int energy_use, int mana_use, int protection, int damage, Direction direction) {
		this.stats_base = new ItemBase(name, shape, rarity, energy_use, direction);
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
		if (stats_base.isUsable(hero) && mana_use >= hero.getManaPoint()) {
			hero.add("armor", protection_armor);
			hero.add("protection", protection);
			hero.sub("mana", mana_use);
			hero.sub("energy", stats_base.energy_use());
		}
	}
}
