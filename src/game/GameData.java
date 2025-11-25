package game;
import java.util.HashMap;
import java.util.Map;

import com.github.forax.zen.ScreenInfo;

import game.data.GameDataBackpack;
import game.data.GameDataHero;
import game.data.GameDataMap;
import model.Backpack;
import model.Hero;
import model.Item;
import model.map.Floor;
import model.map.XY;

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
  private static Floor map;
  private static Hero hero;
  private static int floor;
  private static ScreenInfo screenInfo;
  /**
   * To know if we're adding an item.
   * null if we're not adding.
   */
  private Item weapon = null; 
  /**
   * To know if we display map or bag
   * 
   * - false : map
   * - true : bag
   */
  private static boolean mapOrBag = false;
  
  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(ScreenInfo screenInfo_) {
	  backpack = new Backpack(screenInfo_.height());
	  hero = new Hero(); 
	  floor = 1;
	  map = new Floor(floor);
	  screenInfo = screenInfo_;
	  new GameDataBackpack(backpack);
    new GameDataHero(hero);
    new GameDataMap(map);
	}
  
  /**
   * Methods to check if we click inside the bag
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * 
   * @return -2 if we click a lock case, -1 if we click a free case else, ID of the weapon
   * 				  0 if we click outside of the bag.
   */
  private static int bag_click(int x, int y) {
  	int grid_size = backpack.grid_size();
  	double left_grid = (screenInfo.width() / 2) - 3.5 * grid_size;
  	double up_grid = (screenInfo.height() / 4.5) - 2.5 * grid_size;
  	if (mapOrBag == false) {
  	  return 0;
  	}
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
   * Check the column of the the clicked position inside the grid
   * 
   * <p>The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.</br>
   * This method determines in which row the y-coordinate of a mouse click falls.</p>
   * 
	 * @param left_grid The x-coordinate of the top of the grid.
	 * @param grid_size The height of each grid cell.
	 * @param gap       The horizontal gap between two grid cells.
	 * @param x         The x-coordinate of the click.

	 * @return The column index (0 to 11), or -1 if the click is outside the grid cells.
   */
  private static int check_mapXclick(double left_grid, double grid_size, double gap, int x) {
  	double positionX = left_grid;
  	int newX = -1;
  	for (var i = 0; i < 11; i++) {
  		if (positionX <= x && x <= positionX + grid_size) {
  			newX = i;
  			break;
  		}
  		positionX += gap + grid_size;
  	}
  	return newX;
  }
  
  /**
   * Check the row of the the clicked position inside the grid
   * 
   * <p>The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.</br>
   * This method determines in which row the y-coordinate of a mouse click falls.</p>
   * 
	 * @param y         The y-coordinate of the click.
	 * @param up_grid   The y-coordinate of the top of the grid.
	 * @param grid_size The height of each grid cell.
	 * @param gap       The vertical gap between two grid cells.
	 * 
	 * @return The row index (0 to 4), or -1 if the click is outside the grid cells.
   */
  private static int check_mapYclick(double up_grid, double grid_size, double gap, int y) {
  	double positionY = up_grid;
  	int newY = -1;
  	for (var j = 0; j < 5; j++) {
  		if (positionY <= y && y <= positionY + grid_size) {
  			newY = j;
  			break;
  		}
  		positionY += gap + grid_size;
  	}  	
  	return newY;
  }
  
  /**
   * Methods to check if we click inside the map
   * 
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   */
  private static XY map_click(int x, int y) {
  	int grid_size = backpack.grid_size();
  	var gap = grid_size * 0.1;
  	double left_grid = (screenInfo.width() / 2) - 5.5 * grid_size;
  	double up_grid = (screenInfo.height() / 5.5) - 2.5 * grid_size;
  	if(x < left_grid || x > (left_grid + 11 * grid_size + 10 * gap) ||
   		 y < up_grid   || y > (up_grid + 5 * grid_size + 4 * gap)
   		) {
   		return new XY(-1, -1);
   	}
  	return new XY(check_mapXclick(left_grid, grid_size, gap, x), check_mapYclick(up_grid, grid_size, gap, y));
  }
  
  /**
   * To know if we click the button to switch the display of the map/bag
   * 
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * 
   * @return 1 if we click in the button, else 0
   */
  private static int mapOrBag_click(int x, int y) {
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
   * 
   * @return Map<String, Integer> String give the information of what we clicks, Integer that can be usefull dependent on what we click
   */
  public static Map<String, Object> item_click(int x, int y) {
  	// Here we add other click info
  	Map<String, Object> res = new HashMap<>();
  	if (bag_click(x, y) != 0) { // If we click the bag
  		res.put("Bag", bag_click(x, y));
  	}
  	else if (map_click(x, y).x() != -1) {
  		res.put("Map", map_click(x, y));
  	}
  	else if (mapOrBag_click(x, y) != 0) {
  		res.put("MapOrBag", 1);
  	}
  	else {
  		res.put("Nothing", 0);
  	}
  	return res;
  }

  /**
   * Switch the current value of the var mapOrBag
   * - true : We wants to display Bag
   * - false : We wants to display Map
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
  public Item weapon() {
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
  public void setWeapon(Item item) {
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
  public Floor map() {
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
  
  /**
   * Return the width and height of the screen
   * 
   * @return
   */
  public ScreenInfo screenInfo() {
    return screenInfo;
  }
}
