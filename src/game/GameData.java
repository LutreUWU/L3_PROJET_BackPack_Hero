package game;
import java.util.Map;

import com.github.forax.zen.ScreenInfo;

import item.Backpack;
import item.Hero;
import item.Item_Object;
import item.MapGame;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
  /**
   * - User's backpack
   * - User's profile
   */
  private static Backpack backpack;
  private static MapGame map;
  private static Hero hero;
  private static int floor;
  /**
   * To know if we're adding an item.
   * null if we're not adding.
   */
  private Item_Object weapon = null; 
  /**
   * To know if we display map or bag
   * 
   * - false : map
   * - true : bag
   */
  private static boolean mapOrBag = true;
  
  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(int gridSize) {
	  backpack = new Backpack(gridSize);
	  map = new MapGame(gridSize / 2);
	  hero = new Hero(); 
	  floor = 1;
	}
  
  /**
   * Methods to check if we click inside the bag
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * @param screenInfo Width and Height of the screen
   * 
   * @return -2 if we click a lock case, -1 if we click a free case else, ID of the weapon
   * 				  0 if we click outside of the bag.
   */
  private static int bag_click(int x, int y, ScreenInfo screenInfo) {
  	int grid_size = backpack.grid_size();
  	double left_grid = (screenInfo.width() / 2) - 3.5 * grid_size;
  	double up_grid = (screenInfo.height() / 3.5) - 2.5 * grid_size;
  	if(x < left_grid || x > (left_grid + 7 * grid_size) ||
  		 y < up_grid   || y > (up_grid + 5 * grid_size)
  		) {
  		return 0;
  	}
  	int new_x = (int) (x - left_grid) / grid_size;
  	int new_y = (int) (y - up_grid) / grid_size;
  	return backpack.grid()[new_y][new_x];
  }
  
  /**
   * To know if we click the button to switch the display of the map/bag
   * 
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * @param screenInfo Width and Height of the screen
   * 
   * @return 1 if we click in the button, else 0
   */
  private static int mapOrBag_click(int x, int y, ScreenInfo screenInfo) {
  	int grid_size = backpack.grid_size();
  	if (x < screenInfo.width() - grid_size / 2 || x > screenInfo.width() ||
  			y < screenInfo.height()/3.5 - 2.5* grid_size || y > screenInfo.height()/3.5 - 2.5*grid_size + grid_size / 2) {
  		return 0;
  	}	
  	return 1;
  }
  
  /**
   * Return what we click on the screen
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * @param centerX center of the windows
   * 
   * @return Map<String, Integer> String give the information of what we clicks, Integer that can be usefull dependent on what we click
   */
  public static Map<String, Integer> item_click(int x, int y, ScreenInfo screenInfo) {
  	// Here we add other click info
  	Map<String, Integer> res = null;
  	if (bag_click(x, y, screenInfo) != 0) { // If we click the bag
  		res = Map.of("Bag", bag_click(x, y, screenInfo)) ;
  	}
  	else if (mapOrBag_click(x, y, screenInfo) != 0) {
  		res = Map.of("MapOrBag", 1);
  	}
  	else {
  		res = Map.of("Nothing", 0);
  	}
  	return res;
  }

  /**
   * Switch the current value of the var mapOrBag
   * - true : We wants to display Bag
   * - false : We wants to display Map
   * 
   * 
   */
  public void swapMapOrBag() {
  	if (mapOrBag) {
  		mapOrBag = false;
  	} else {
  		mapOrBag = true;
  	}
  }
  
  // ============
  // == GETTER ==
  // ============
  
  /**
   * Return the weapon we wants to move / add
   * 
   * @return Item_Object weapon
   */
  public Item_Object weapon() {
    return weapon;
  }
  
  /**
   * Return the status of the button
   * 
   * @return true :  we display map
   * 				 false : we display bag
   */
  public boolean mapOrBag() {
  	return mapOrBag;
  }
 
  /**
   * Add the weapon we wants to move in the data.
   * If we don't add an item, this information is null.
   * 
   * @param item
   */
  public void setWeapon(Item_Object item) {
    this.weapon = item;
  }
  
  /**
   * Return the current backpack of the player from data
   * @return Objects backpack
   */
  public Backpack bag() {
    return backpack;
  }
  /**
   * Return the current map of the player from data
   * @return Objects MapGame
   */
  public MapGame map() {
    return map;
  }
  
  /**
   * Return the current floor of the player from data
   * @return int floor
   */
  public int floor() {
    return floor;
  }

  /**
   * Return the current hero's statue from data
   * @return
   */
  public Hero hero() {
    return hero;
  }
}
