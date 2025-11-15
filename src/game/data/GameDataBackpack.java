package game.data;

import java.util.Objects;

import com.github.forax.zen.ScreenInfo;

import item.Backpack;
import item.Block;
import item.Item_Object;

 /**
  * The game data with all methods for backpack manipulation. 
  * Link to the backpack from GameData
  * 
  */
public class GameDataBackpack {
  private static Backpack backpack;
  private static int grid_size;
 
  
  /**
   * Link the backpack with the one in GameData
   * @param data_backpack Backpack's data from GameData
   * @throws Objects.requireNonNull if no backpack is initialize
   */
  public GameDataBackpack(Backpack data_backpack, int gridSize) { 
		Objects.requireNonNull(data_backpack);
		backpack = data_backpack;
		grid_size = gridSize;
  }
  //==============================
  //   METHODS FOR BACKPACK
  //==============================
  
  /**
   * Check for each block of the item if it can fit in the backpack
   * 
   * @param item Item we wants to check
   * @return true if we can, else false if we can't
   */
  private static boolean check_place(Item_Object item) {
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
  public static boolean add_ItemToBackpack(Item_Object item) {
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
  public static void remove_itemFromBackpack(Item_Object item) {
    Objects.requireNonNull(item);
    var b = item.shape();
    for (var block : b) {
      backpack.grid()[block.y()][block.x()] = -1;
    }
    backpack.item_lst().remove(item);
  }

  /**
   * Check if the item, after moving, is outside of the backpack.
   * 
   * @param item Item we wants to check
   * @param addX How many tiles horizontally the item wants to move.
   * @param addY How many tiles vertically the item wants to move.
   * @return true if it's inside the backpack, else return false
   */
  private static boolean border_backpack(Item_Object item, int addX, int addY) {
    var b = item.shape();
    for (var block : b) {
      int y = block.y() + addY;
      int x = block.x() + addX;
      if (y < 0 || y >= backpack.grid().length || x < 0 || x >= backpack.grid()[0].length) {
        return false;
      }
      if (backpack.grid()[y][x] == -2) {
        return false;
      }
    }
    return true;
  }
	  
  /**
   * Move an item in the backpack
   * 
   * @param item Item we wants to check
   * @param addX How many tiles horizontally the item wants to move.
   * @param addY How many tiles vertically the item wants to move.
   */
  public static void move_item(Item_Object item, int addX, int addY) {
    if (item != null && border_backpack(item, addX, addY)) {
      var b = item.shape();
      for (int i = 0; i < b.length; i++) {
        b[i] = new Block(b[i].x() + addX, b[i].y() + addY);
      }
    }
  }

  /**
   * Rotate an item in the backpack by calling 
   * the methods rotateXY inside the Item class
   * 
   * @param item
   */
  public static void rotate_item(Item_Object item) {
    if (item != null) {
      item.rotateXY(backpack);
    }
  }
  
  /**
   * Return which item in the backpack we click with the mouse
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * @param centerX center of the windows
   */
  public static int item_click(int x, int y, ScreenInfo screenInfo) {
  	double left_grid = (screenInfo.width() / 2) - 3.5 * grid_size;
  	double up_grid = (screenInfo.height() / 3.5) - 2.5 * grid_size;
  	if(x < left_grid || x > left_grid * 7*grid_size ||
  		 y < up_grid   || y > up_grid * 5*grid_size) {
  		return -1;
  	}
  	int new_x = (int) (x - left_grid) / grid_size;
  	int new_y = (int) (y - up_grid) / grid_size;
  	return backpack.grid()[new_y][new_x];
  }
  
  /*
   * 			    graphics.fill(new Rectangle2D.Double((screenInfo.width() / 2) - 3.5 * data.grid_size() + (data.grid_size() * fj), 
										        							  	 (screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
										        							  	 data.grid_size(), data.grid_size()));
   */

}
