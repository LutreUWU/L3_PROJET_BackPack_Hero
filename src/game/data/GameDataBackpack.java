package game.data;

import java.util.Objects;

import model.Backpack;
import model.Block;
import model.Item;

 /**
  * The game data with all methods for backpack manipulation. 
  * It's separated from GameData.java for easier read
  * Link to the backpack from GameData
  * 
  */
public class GameDataBackpack {
  private static Backpack backpack;
  
  /**
   * Link the backpack with the one in GameData
   * @param data_backpack Backpack's data from GameData
   * @throws Objects.requireNonNull if no backpack is initialize
   */
  public GameDataBackpack(Backpack data_backpack) { 
		Objects.requireNonNull(data_backpack);
		backpack = data_backpack;
  }

  /**
   * Check for each block of the item if it can fit in the backpack
   * 
   * @param item Item we wants to check
   * @return true if we can, else false if we can't
   */
  private static boolean check_place(Item item) {
		if (item == null) {
		  return false;
		}
		var b = item.shape();
	    for (var block : b) {
	      int y = block.y();
	      int x = block.x();
	      if (y < 0 || y >= backpack.grid().length || x < 0 || x >= backpack.grid()[0].length) {
	        return false;
	      }
	      if (backpack.grid()[y][x] != -1) {
	        return false;
	      }
	    }
    return true;
  }
	  
  /**
   * Add an item in the backpack of the game.
   * If item is null, it means that we just press the button for adding. 
   * 
   * @param item Item we wants to check
   * @return true if we can add it, else false if we can't
   */
  public static boolean add_ItemToBackpack(Item item) {
    if (item == null) {
      return false;
    }
    if (check_place(item)) {
      var b = item.shape();
      for (var block : b) {
        backpack.grid()[block.y()][block.x()] = item.id();
      }
      backpack.item_lst().add(item);
      return true;
    }
    return false;
  }
	  
  /**
   * Remove an item from the backpack
   * 
   * @param item Item we wants to remove
   * @throws Objects.requireNonNull if item is null
   */
  public static void remove_itemFromBackpack(Item item) {
    Objects.requireNonNull(item);
    var b = item.shape();
    for (var block : b) {
      backpack.grid()[block.y()][block.x()] = -1;
    }
    backpack.item_lst().remove(item);
  }
}
