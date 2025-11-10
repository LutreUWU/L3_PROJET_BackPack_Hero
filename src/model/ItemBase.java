package model;

import java.util.Objects;

public record ItemBase(String name, int[][] shape, String rarity, int energy_use, Direction direction) {

	boolean isUsable(Hero hero) {
		Objects.requireNonNull(hero);
		if (hero.getEnergyPoint() < energy_use) {
			IO.println("Pas assez d'énergie pour utiliser l'objet !");
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		// No loop = "+" :
		return name + " :\n• " + rarity + "\n• Energy needs : " + energy_use;
	}
	
	public ItemBase rotation() {
		int l = shape[0].length;
		int L = shape.length;
		int[][] new_shape = new int [l][L];
		for (int i = 0; i < L; i++) {
			for (int j = 0; j < l; j++) {
				new_shape[j][L - 1 - i] = shape[i][j];
			}
		}
		Direction[] all_direction = Direction.values();
		Direction new_direction = all_direction[(direction.ordinal() + 1) % all_direction.length];
		return new ItemBase(name, new_shape, rarity, energy_use, new_direction);
	}
}
