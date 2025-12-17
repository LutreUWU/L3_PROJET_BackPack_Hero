package game.data;

import java.util.Objects;

import game.GameData;
import model.map.Shop;

/**
 * The game data with all methods for combat manipulation. 
 * It's separated from GameData.java for easier read
 * 
 * All methods here are used when we initiate a combat.
 */
public record GameDataShop(Shop shop, boolean inside, GameData data) {
	public GameDataShop {
    Objects.requireNonNull(shop);
    Objects.requireNonNull(data);
	}
	
	public GameDataShop(Shop shop, GameData data) {
		this(shop, true, data);
	}
  
  public GameDataShop exit() {
    return new GameDataShop(shop, false, data);
  }
}
