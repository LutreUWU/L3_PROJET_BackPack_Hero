package game.data;

import java.util.Objects;

import item.MapGame;

/**
 * The game data with all methods for hero manipulation. 
 * Link to the map from GameData
 * 
 */
public class GameDataMap {
	private static MapGame map;
	
  /**
   * Link the map with the one in GameData
   * 
   * @param data_hero Hero's data from GameData
   * @throws Objects.requireNonNull if no hero is initialize
   */
	public GameDataMap(MapGame data_map) { 
		Objects.requireNonNull(data_map);
		map = data_map;
  }
	
	/**
	 * create the map of the current floor
	 * 
	 */
	public static void create_map(int floor) {
		map.create_map();
	}

}
