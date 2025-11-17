package game;
import java.util.ArrayList;

import com.github.forax.zen.ScreenInfo;

import item.Backpack;
import item.Hero;
import item.Item_Object;
import monster.Enemy;

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
  private static Hero hero;
  /**
   * To know if we're adding an item.
   * null if we're not adding.
   */
  private Item_Object weapon = null; 
  
  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(int gridSize) {
	  backpack = new Backpack(gridSize);
	  hero = new Hero(); 
	}
  
  /**
   * Methods to check if we click inside the bag
   * @param x
   * @param y
   * @param screenInfo
   * 
   * @return -2 if we click a lock case, -1 if we click a free case else, ID of the weapon
   * 				  0 if we click outside of the bag.
   */
  private static int bag_click(int x, int y, ScreenInfo screenInfo) {
  	int grid_size = backpack.grid_size();
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
  
  /**
   * Return what we click on the screen
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * @param centerX center of the windows
   * 
   */
  public static int item_click(int x, int y, ScreenInfo screenInfo) {
  	// Here we add other click info
  	// TO FINISH
  	return bag_click(x, y, screenInfo);
  }
  //==============================
  //   		  ACCESSEUR
  //==============================
  
  /**
   * Return the weapon we wants to move / add
   * 
   * @return Item_Object weapon
   */
  public Item_Object weapon() {
    return weapon;
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
   * Return the size of a grid in the backpack
   * 
   * @return size of a grid
   */
  public int grid_size() {
    return backpack.grid_size();
  }

  /**
   * Return the current backpack of the player from data
   * @return Objects backpack
   */
  public Backpack bag() {
    return backpack;
  }

  /**
   * Return the current hero's statue from data
   * @return
   */
  public Hero hero() {
    return hero;
  }
}
