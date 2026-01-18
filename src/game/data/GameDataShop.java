package game.data;

import java.util.Objects;

import game.GameData;
import model.map.Shop;

/**
 * Represents the current state of the shop in the game.
 * 
 * This record holds information about a shop session, including:
 * - the Shop instance being accessed,
 * - whether the player is currently inside the shop,
 * - and the associated game data.
 * 
 * It is typically used to manage shop interactions, purchases, or exits.
 * 
 * @param shop the Shop instance being interacted with, must not be null
 * @param inside true if the player is currently inside the shop, false otherwise
 * @param data the current GameData, must not be null
 */
public record GameDataShop(Shop shop, boolean inside, GameData data) {
	
	/**
	 * Records a shop game state.
	 * Ensures that the shop and game data are not null.
	 */
	public GameDataShop {
    Objects.requireNonNull(shop);
    Objects.requireNonNull(data);
	}
	
	/**
	 * Creates a new shop game state with the given shop and game data.
	 * By default, the shop is considered active.
	 *
	 * @param shop The shop being accessed
	 * @param data The current game data
	 */
	public GameDataShop(Shop shop, GameData data) {
		this(shop, true, data);
	}
  
	/**
	 * Returns a new GameDataShop instance representing exiting the shop.
	 * The shop is marked as inactive.
	 *
	 * @return a new GameDataShop with the shop inactive
	 */
  public GameDataShop exit() {
    return new GameDataShop(shop, false, data);
  }
}
