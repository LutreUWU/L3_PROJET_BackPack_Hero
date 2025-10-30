package model;

public class Weapon extends ItemBase implements Item {
	private String type; // Melee weapon, ranged weapon,...
	private int damage;
	
	public Weapon(String name, int[] location, int[][] shape, String rarity, int energy_use, String type, int damage) {
		super(location, shape, name, rarity, energy_use);
		this.type = type;
		this.damage = damage;
	}
	
	@Override
	public String toString() {
		// No loop = "+" :
		return super.toString() + "\n• Type : " + type + "\n• Damage : " + damage + "\n";
	}
	
	@Override
	public void use(Object target) {
		if (target instanceof Enemy e) { // Cast 
			e.subHP(damage);
			if (e.isDead()) {
				IO.println("Faut qu'on s'en débarasse !\n");
			} else {
				throw new IllegalArgumentException("The target is not an enemy !");
			}
		}
		
	}
}
