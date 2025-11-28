package game;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

import com.github.forax.zen.ScreenInfo;

import game.data.GameDataBackpack;
import game.data.GameDataClick;
import game.data.GameDataHero;
import game.data.GameDataMap;
import game.data.ImageLoader;
import model.Backpack;
import model.BoundingBox;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.XY;
import model.map.Floor;
import model.weapon.Sword;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
  /**
   * - User's backpack information
   * - Map of the game
   * - User's hero information
   * - Floor number
   * - Width and height of the screen
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
  private Item dragItem = null; 
  /**
   * To know if we display map or bag
   * 
   * - false : map
   * - true : bag
   */
  private boolean mapOrBag = true;
  private static LinkedHashMap<Item, BoundingBox> itemAdd;
  private XY mouse_coord;
  /**
   * Map used for drawing an image <br>
   * The keys are the name of the image and the value his {@code BufferedImage}
   * 
   */
  private Map<String, BufferedImage> img_map;

  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(ScreenInfo screenInfo_) {
	  backpack = new Backpack(screenInfo_.height());
	  hero = new Hero(); 
	  floor = 1;
	  map = new Floor(floor, hero);
	  screenInfo = screenInfo_;
	  itemAdd = new LinkedHashMap<>();
	  img_map = ImageLoader.load_image();
	  ItemRepository.registerWeapon(new Sword());
	  new GameDataBackpack(backpack);
    new GameDataHero(hero);
    new GameDataMap(map);
    new GameDataClick(this);
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
  
  public void remove_itemMap(Item item) {
  	itemAdd.remove(item);
  }
  
  public static void rotate_item(Item item) {
  	item.rotateXY();
  }
  // ============
  // == GETTER ==
  // ============
  
  /**
   * Return the weapon we drags
   * 
   * @return Item_Object weapon
   */
  public Item dragItem() {
    return dragItem;
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
  public void setDragItem(Item item) {
    this.dragItem = item;
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
  
  public LinkedHashMap<Item, BoundingBox> map_item(){
  	return itemAdd;
  }
  
  public Map<String, BufferedImage> img_map(){
  	return img_map;
  }

	public XY getMouse_coord() {
		return mouse_coord;
	}

	public void setMouse_coord(XY mouse_coord) {
		this.mouse_coord = mouse_coord;
	}
}
