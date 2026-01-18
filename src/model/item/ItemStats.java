package model.item;

import model.Rarity;

/**
 * Represents the immutable statistics of an item.
 *
 * ItemStats groups all static properties of an item, such as its rarity,
 * unique identifier, score value, action points cost, and mana cost.
 * These values are validated at construction time to ensure consistency.
 *
 * @param rarity the rarity level of the item
 * @param ID the unique identifier of the item, must be non-negative
 * @param score the score value of the item (may be negative for special items)
 * @param AP the action point cost of using the item, must be non-negative
 * @param mana the mana cost of using the item, must be non-negative
 */
public record ItemStats(Rarity rarity, int ID, int score, int AP, int mana) {
	/**
	 * Default constructor that check if the value is correct
	 * 
	 * @param rarity the rarity level of the item
	 * @param ID the unique identifier of the item, must be non-negative
	 * @param score the score value of the item (may be negative for special items)
	 * @param AP the action point cost of using the item, must be non-negative
	 * @param mana the mana cost of using the item, must be non-negative
	 * 
	 * @throws IllegalArgumentException if ID, AP, or mana is negative
	 */
	public ItemStats{
		if (ID < 0) {
			throw new IllegalArgumentException(ID + "is not a valid ID");
		}
		if (score < 0) {
			throw new IllegalArgumentException(score + "is not a valid score");
		}
		if (AP < 0) {
			throw new IllegalArgumentException(AP + "is not a valid AP");
		}
		if (mana < 0) {
			throw new IllegalArgumentException(mana + "is not a valid mana");
		}
	}
}
