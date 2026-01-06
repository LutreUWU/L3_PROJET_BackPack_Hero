package model.item;

import model.Rarity;

public record ItemStats(Rarity rarity, int ID, int score, int AP, int mana) {
	public ItemStats{
		if (ID < 0) {
			throw new IllegalArgumentException(ID + "is not a valid ID");
		}
		// We don't check score since it can be negative for Gold and KeyDoor
		if (AP < 0) {
			throw new IllegalArgumentException(AP + "is not a valid AP");
		}
		if (mana < 0) {
			throw new IllegalArgumentException(mana + "is not a valid mana");
		}
	}
}
