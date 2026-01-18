package model;

/**
 * Represents the rarity level of an item in the game.
 *
 * The rarity defines how uncommon an item is and is generally used
 * to determine its strength, score, or availability.
 *
 * Rarity levels (from lowest to highest):
 * - COMMON: very common
 * - RARE: uncommon items 
 * - SUPERARE: very rare items 
 * - EPIC: extremely powerful
 * - LEGENDARY: exceptional items 
 * - MYTHIC: mythical items 
 */
public enum Rarity {
	 /** Basic and common item */
  COMMON,
  /** Uncommon item with improved effects */
  RARE,
  /** Very rare item with strong effects */
  SUPERARE,
  /** Powerful item with unique abilities */
  EPIC,
  /** Extremely powerful and hard-to-find item */
  LEGENDARY,
  /** Unique and exceptional item */
  MYTHIC;
}
