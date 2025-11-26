package game.data;

import java.util.Objects;

import model.map.Floor;

/**
 * The game data with all methods for hero manipulation. 
 * Link to the map from GameData
 * 
 */
public class GameDataMap {
	private static Floor map;
	
  /**
   * Link the map with the one in GameData
   * 
   * @param data_hero Hero's data from GameData
   * @throws Objects.requireNonNull if no hero is initialize
   */
	public GameDataMap(Floor data_map) { 
		Objects.requireNonNull(data_map);
		map = data_map;
  }
	
	/**
	 * create the map of the current floor
	 * 
	 */

}
