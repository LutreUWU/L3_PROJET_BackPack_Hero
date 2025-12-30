package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.forax.zen.ScreenInfo;

import game.data.GameDataClick;
import game.data.GameDataHero;
import model.Backpack;
import model.EnemyRepository;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.XY;
import model.map.Floor;
import model.map.Shop;
import model.map.eventManager.LinkedEvent;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
  private static Backpack backpack;
  private static Floor map;
  private static Hero hero;
  private static int floor;
  private static ScreenInfo screenInfo;

  private Item dragItem = null; 
  private boolean onBin = false;

  private boolean mapOrBag = true;
  private boolean shop = false;
  private Shop shopLst;

  private LinkedEvent event;
  private XY mouseCoord;
  /**
   * Sortest Path
   */
  private List<XY> shortestPath = new ArrayList<>();
  
  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(ScreenInfo screenInfo_) {
  	Objects.requireNonNull(screenInfo_);
	  backpack = new Backpack(screenInfo_.height());
	  hero = new Hero(); 
	  floor = 1;
	  ItemRepository.createItemRepository();
	  EnemyRepository.createEnemyRepository();
	  map = new Floor(floor);
	  screenInfo = screenInfo_;
    new GameDataHero(hero);
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
  
  public List<XY> getShortestPath () {
  	return shortestPath;
  }
  
  public void setShortestPath(List<XY> shortestPath2) {
  	Objects.requireNonNull(shortestPath2);
  	shortestPath = shortestPath2;
  }
  
  public static Item rotateItem(Item item) {
  	Objects.requireNonNull(item);
  	return item.rotateXY();
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
   * Go up one floor
   */
  public void newFloor() {
  	floor++;
    map = new Floor(floor);
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
  
  public void inEvent(LinkedEvent new_event) {
  	event = new_event;
  	if (!mapOrBag) {
  		this.swapMapOrBag();
  	}
  }
  
  public void outEvent() {
  	event = null;
  }
  
  
  /**
   * Boolean to know if we're in a event or no
   * 
   * @return
   */
  public LinkedEvent event() {
    return event;
  }
  
  /**
   * Return the width and height of the screen
   * 
   * @return
   */
  public ScreenInfo screenInfo() {
    return screenInfo;
  }
 
	public XY getMouseCoord() {
		return mouseCoord;
	}

	public void setMouseCoord(XY mouse_coord) {
		this.mouseCoord = mouse_coord;
	}
	
	public void setBin(boolean statut) {
		this.onBin = statut;
	}
	
	public boolean getShop() {
		return shop;
	}
	
	
	public void setShop(boolean statut, Shop shop) {
		if (statut) swapMapOrBag();
		this.shop = statut;
		this.shopLst = shop;
	}
	
	public Shop getShopLst() {
		return shopLst;
	}
	
	public boolean getBin() {
		return onBin;
	}
}
