package model;

import java.util.Objects;

public abstract class ItemBase {
	private int[][] shape; // matrix : 1 = item
	private String name;
	private String rarity;
	private int energy_use;
	
	// Getter
	public int getEnergy_use() {
		return energy_use;
	}


	public void setEnergy_use(int energy_use) {
		this.energy_use = energy_use;
	}

	boolean isUsable(Hero hero) {
		Objects.requireNonNull(hero);
		if (hero.getEnergyPoint() < energy_use) {
			IO.println("Pas assez d'énergie pour utiliser l'objet !");
			return false;
		}
		return true;
	}

	public ItemBase(String name, int[][] shape, String rarity, int energy_use) {
		this.shape = shape;
		this.name = name;
		this.rarity = rarity;
		this.energy_use = energy_use;
	}
	
	

	@Override
	public String toString() {
		// No loop = "+" :
		return name + " :\n• " + rarity + "\n• Energy needs : " + energy_use;
	}
}
