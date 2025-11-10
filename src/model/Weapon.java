package model;

import java.util.Objects;

public class Weapon implements Item {
	private String type; // Melee weapon, ranged weapon,...
	private int damage;
	private ItemBase stats_base;
	
	public Weapon(String name, int[][] shape, String rarity, int energy_use, String type, int damage, Direction direction) {
		this.stats_base = new ItemBase(name, shape, rarity, energy_use, direction);
		this.type = type;
		this.damage = damage;
	}
	
	
	
	


	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Type : " + type + "\n• Damage : " + damage + "\n";
	}
	
	
	@Override
	public void use(Hero hero, Object target) {
		/**
		 * Function to use the weapon
		 * @param hero : The hero
		 * @param target : Object -> Enemy
		 */
		Objects.requireNonNull(hero);
		Objects.requireNonNull(target);
		if (!stats_base.isUsable(hero)) {
			if (target instanceof Enemy e) { // Cast 
				e.subHP(damage);
				hero.sub("energy", stats_base.energy_use());
				if (e.isDead(hero)) {
					IO.println("// Faut qu'on se débarrasse de l'ennemi ici !!\n");
				} 
			} else throw new IllegalArgumentException("The target is not an enemy !");
		}
	}
}
