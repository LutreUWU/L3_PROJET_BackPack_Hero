package game;
import item.Backpack;
import item.Item_Object;
import model.Hero;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
  /**
   * - User's backpack
   * - User's profile
   * - Size of a grid in the backpack
   */
  private final Backpack backpack;
  private final Hero hero;
  private final int grid_size;
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
	    this.backpack = new Backpack();
	    this.hero = new Hero(); 
	    this.grid_size = gridSize;
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
    return grid_size;
  }

  /**
   * Return the current backpack of the player from data
   * @return
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
