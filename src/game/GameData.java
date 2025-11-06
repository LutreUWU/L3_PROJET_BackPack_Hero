package game;

import java.util.Objects;
import item.Backpack;
import item.Block;
import item.Item_Object;
import model.Hero;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
  /**
   * User's backpack
   */
  private final Backpack backpack;
  /**
   * User's profile
   */
  private final Hero hero;
  /**
   * Size of a grid in the backpack
   */
  private final int grid_size;
  /**
   * To know if we're adding an item.
   * null if we're not adding.
   */
  private Item_Object weapon = null; 

  /**
   * Creates and initializes a new GameData with
   * the grid size of a tile in a bag.
   * 
   * @param gridSize  size of a grid (in pixel)
   */
  public GameData(int gridSize) {
    this.backpack = new Backpack();
    this.hero = new Hero(); // TO DO 
    this.grid_size = gridSize;
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
  private boolean check_place(Item_Object item) {
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
  public boolean add_ItemToBackpack(Item_Object item) {
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
  public void remove_itemFromBackpack(Item_Object item) {
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
  private boolean border_backpack(Item_Object item, int addX, int addY) {
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
  public void move_item(Item_Object item, int addX, int addY) {
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
  public void rotate_item(Item_Object item) {
    if (item != null) {
      item.rotateXY(backpack);
    }
  }
  
  //==============================
  //   METHODS FOR HERO
  //==============================
  
  /**
   * Simply add PV to the hero.
   * Can't exceed max HP.
   * 
   * @param hero user's profile 
   */
  public void add_PV(Hero hero) {
	  // TO DO
  }
  
  /**
   * Simply remove PV to the hero.
   * When reach 0 or below, return false, else return true
   * 
   * @param hero user's profile 
   */
  public boolean remove_PV(Hero hero) {
	  // TO DO
	  return true;
  }
  
  /**
   * Simply add Shield to the hero.
   * 
   * @param hero user's profile 
   */
  public void add_Shield(Hero hero) {
	  // TO DO
  }
  
  /**
   * Simply remove Shield to the hero.
   * 
   * @param hero user's profile 
   */
  public void remove_Shield(Hero hero) {
	  // TO DO 
  }
  
  /**
   * Reset shield value to 0.
   * Shield is reset every turn.
   * 
   * @param hero user's profile 
   */
  public void reset_Shield(Hero hero) {
	  // TO DO
  }
  
  /**
   * Simply add Action Point to the hero.
   * 
   * @param hero user's profile 
   */
  public void add_AP(Hero hero) {
	  // TO DO 
  }
  
  /**
   * Simply remove Action Point to the hero.
   * 
   * @param hero user's profile 
   */
  public void remove_AP(Hero hero) {
	  // TO DO 
  }
  
  /**
   * Use an item in the backpack.
   * 
   * @param id ID of the item we wants to use
   */
  public void use_item(int id) {
	  // TO DO
	  // You will need to 
	  // You can modify Sword class to add all the data of the weapon
  }
  
  // If you have other idea add, add it :)
  
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
