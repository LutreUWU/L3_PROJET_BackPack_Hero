package game.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import com.github.forax.zen.ScreenInfo;

import game.GameData;
import loader.MathLoader;
import model.Backpack;
import model.BoundingBox;
import model.Curse;
import model.Item;
import model.XY;
import model.item.common.Gold;

public class GameDataClick {
	private static GameData data;
	private static Backpack backpack;
	private static ScreenInfo screenInfo;
  private static XY oldPosition; // contains the mouse coordinate before moving the cursor.
  private static LinkedHashMap<Item, BoundingBox> dragItemMap; // Contains all movable items on the screen.
	
	public GameDataClick(GameData dataGame) {
		Objects.requireNonNull(dataGame);
		data = dataGame;
		backpack = data.bag();
		screenInfo = data.screenInfo();
		dragItemMap = new LinkedHashMap<>();
	}
	
	/**
   * Methods to check if we click inside the bag
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * 
   * @return  {@code XY} containing the coordinate of the tile we click.
   * 				  null if we click outside of the bag.
   */
  public static XY bagClick(int x, int y) {
  	int size = backpack.getGridSize();
  	double coordLeftGrid = (screenInfo.width() / 2) - 3.5 * size;
  	double coordTopGrid = (screenInfo.height() / 4.5) - 2.5 * size;
  	if (data.mapOrBag() == false) {
  	  return null;
  	}
  	if(x < coordLeftGrid || x > (coordLeftGrid + 7 * size) ||
  		 y < coordTopGrid   || y > (coordTopGrid + 5 * size)
  		) {
  		return null;
  	}
  	int newX = (int) (x - coordLeftGrid) / size;
  	int newY = (int) (y - coordTopGrid) / size;
  	return new XY(newX, newY);
  }
  
  
  /**
	 * Check the column of the clicked position inside the map grid
	 * 
	 * The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.
	 * This method determines in which row the x-coordinate of a mouse click falls.
	 * 
	 * @param leftGrid The x-coordinate of the top of the grid.
	 * @param gridSize The height of each grid cell.
	 * @param gap      The horizontal gap between two grid cells.
	 * @param x        The x-coordinate of the click.
	
	 * @return The column index (0 to 11), or -1 if the click is outside the grid cells.
	 */
	private static int checkMapAbsClick(double leftGrid, double gridSize, double gap, int x) {
		double positionX = leftGrid;
		int newX = -1;
		for (var i = 0; i < 11; i++) {
			if (positionX <= x && x <= positionX + gridSize) {
				newX = i;
				break;
			}
			positionX += gap + gridSize;
		}
		return newX;
	}
	
  /**
	 * Check the row of the the clicked position inside the map grid
	 * 
	 * The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.
	 * This method determines in which row the y-coordinate of a mouse click falls.
	 * 
	 * @param topGrid   The y-coordinate of the top of the grid.
	 * @param gridSize  The height of each grid cell.
	 * @param gap       The horizontal gap between two grid cells.
	 * @param xy        The y-coordinate of the click.
	
	 * @return The column index (0 to 11), or -1 if the click is outside the grid cells.
	 */
	private static int checkMapOrdClick(double topGrid, double gridSize, double gap, int y) {
		double positionY = topGrid;
		int newY = -1;
		for (var i = 0; i < 5; i++) {
			if (positionY <= y && y <= positionY + gridSize) {
				newY = i;
				break;
			}
			positionY += gap + gridSize;
		}
		return newY;
	}
	
  /**
   * Methods to check if we click inside the floor map
   * 
   * @param x Coordinate x we click
   * @param y	Coordinate y we click
   * 
   * @return {@code XY} containing the coordinate of the tile we click in the map.
   * 				 null if we click outside of the map.
   */
  private static XY floorClick(int x, int y) {
  	int size = backpack.getGridSize();
  	var gap = size * 0.1;
  	double leftGrid = MathLoader.getMapEvent().get("BG_MAP").box().northWest().x();
  	double topGrid = MathLoader.getMapEvent().get("BG_MAP").box().northWest().y();
  	if(x < leftGrid || x > (leftGrid + 11 * size + 10 * gap) ||
   		 y < topGrid   || y > (topGrid + 5 * size + 4 * gap)
   		) {
   		return null;
   	}
  	return new XY(checkMapAbsClick(leftGrid, size, gap, x), checkMapOrdClick(topGrid, size, gap, y));
  }
  
  /**
   * To know if we click the button to switch the display of the map/bag
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return 1 if we click in the button, else 0
   */
  private static int mapOrBagClick(int x, int y) {
  	int size = backpack.getGridSize();
  	if (x < screenInfo.width() - size / 2 || x > screenInfo.width() ||
  			y < screenInfo.height()/3.5 - 2.5* size || y > screenInfo.height()/3.5 - 2.5*size + size / 2) {
  		return 0;
  	}	
  	return 1;
  }
  
  /**
   * Method that look if we click on a draggable item on screen.
   * This method is used for dragging item on the screen.
   * 
   * If we click on a item on the screen, simply return it.
   * If we click on a item in the bag, remove it from the bag and add it to the screen.
   * 
   * Since when we're in combat, we don't want to drag item (except if it's a curse event),
   * so we add a condition to check if we're in combat and in curse event.
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return {@code Item} we click
   * 				 null if no item was clicked
   */
  private static Item itemClick(int x, int y) {
  	if (GameDataCombat.combat() && !GameDataCombat.getCurseEvent()) { // If we're in combat and not in a curse event
  		return null;
  	}
  	Item item;
  	// For each item outside the bag
  	item = itemOutBagClick(x, y);
  	if (item == null) {
  		// For each item inside the bag 
    	item = itemInBagClick(x, y);
  	}
  	return item;
  }
  
  /**
   * Take a mouse coordinate (x, y) and check for all items on the screen to see if we're inside.
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return {@code Item} we click
   * 				 null if no item was clicked
   */
  private static Item itemOutBagClick(int x, int y) {
  	Item item;
  	for (Map.Entry<Item, BoundingBox> entry : dragItemMap.entrySet()) {
      item = entry.getKey();
      BoundingBox box = entry.getValue();
      if (x >= box.northWest().x() && x <= box.southEast().x() &&
          y >= box.northWest().y() && y <= box.southEast().y()) {
          return item;
      }
  	}
  	return null;
  }
  
  /**
   * Take a mouse coordinate (x, y) and check all items in the bag to see if we're inside.
   * If we click on a item, we drag it, so we remove it from the bag.
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return {@code Item} we click
   * 				 null if no item was clicked
   */
  private static Item itemInBagClick(int x, int y) {
  	Item item;
  	var res = bagClick(x,y);
  	if (res != null) {
  		item = backpack.getItem(res.x(), res.y());
  		if (item != null) {
	  		switch(item) {
	  		case Curse _ -> {return null;}
	  		default -> {
	  			data.bag().removeItemFromBackpack(item);
	    		addDragItemFromBag(item, res.x(), res.y());
	    		return item;
	  		}
	  		}
  		}
  	}
  	return null;
  }
  
  /**
   * Register the position of the mouse before moving the cursor
   * 
   * @param x	Old coordinate x before we move
   * @param y Old coordinate y before we move
   */
  public static void setOldPosition(int x, int y) {
    if (x < 0 || y < 0) {
        throw new IllegalArgumentException("x and y are negatives");
    }
    oldPosition = new XY(x, y);
  }
  
  /**
   * Get the number of tile horizontally that the item held in the backpack
   * 
   * @param item we wants to check the width
   * 
   * @return number of tile horizontally
   */
  private static int getWidth(Item item) {
    XY[] b = item.shape();
    int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
    for (XY block : b) {
      int x = block.x();
      if (x < minX) minX = x;
      if (x > maxX) maxX = x;
    }
    return maxX - minX + 1;
  }

  /**
   * Get the number of tile vertically that the item held in the backpack
   * 
   * @param item we wants to check the height
   * 
   * @return number of tile vertically
   */
  private static int getHeight(Item item) {
    XY[] b = item.shape();
    int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
    for (XY block : b) {
        int y = block.y();
        if (y < minY) minY = y;
        if (y > maxY) maxY = y;
    }
    return maxY - minY + 1;
}
  
  /**
   * Add an item in the list of draggable item.
   * 
   * @param item we wants to move
   */
  public static void addDragItem(Item item) {
  	Objects.requireNonNull(item);
  	dragItemMap.put(item, new BoundingBox(
											new XY(screenInfo.width() / 2 - (getWidth(item) * backpack.getGridSize() / 2) , (int) (screenInfo.height() / 1.6) - (getHeight(item) * backpack.getGridSize() / 2) ),
											new XY(screenInfo.width() / 2 + (getWidth(item) * backpack.getGridSize() / 2) , (int) (screenInfo.height() / 1.6) + (getHeight(item) * backpack.getGridSize() / 2) ))
							 		 );
  }
  
  /**
   * Remove an item from the list of draggable item.
   * 
   * @param item we wants to remove
   */
  public static void removeItemFromDrag(Item item) {
  	Objects.requireNonNull(item);
  	dragItemMap.remove(item);
  }
  
  /**
   * Reset the list of draggable item. 
   */
  public static void resetDragItemLst() {
  	dragItemMap = new LinkedHashMap<>();
  }
  
  /**
   * Update the boundingBox of the {@code Item} in argument at
   * the designed location.
   * 
   * We call this method when rotating an {@code Item}.
   * 
   * @param item item we wants to modify the boundingBox
   * @param x		 x location
   * @param y		 y location
   */
  public static void updateBoundingBox(Item item, int x, int y) {
  	Objects.requireNonNull(item);
  	int minX = x - (getWidth(item) * backpack.getGridSize() / 2);
    int minY = y - (getHeight(item) * backpack.getGridSize() / 2);
  	int maxX = x + (getWidth(item) * backpack.getGridSize() / 2);
    int maxY = y + (getHeight(item) * backpack.getGridSize() / 2);
    dragItemMap.put(item, new BoundingBox(new XY(minX, minY), new XY(maxX, maxY)));
  }
  
  /**
   * Add an item from the bag in the list of items draggable.
   * 
   * @param item item we wants to add
   * @param x		 position X in the bag before dragging it.
   * @param y 	 position Y in the bag before dragging it.
   */
  public static void addDragItemFromBag(Item item, int x, int y) {
  	Objects.requireNonNull(item);
  	var size = backpack.getGridSize();
  	var northWest = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)), (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * (y - 1))));
  	var southEast = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)) + getWidth(item) * size, (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * y) + getHeight(item) * size));
  	dragItemMap.put(item, new BoundingBox(northWest, southEast));
  }
  
  /**
   * Update the BoundingBox of the item when moving it. 
   * To update the boundingbox, we use the old and new coordinate of the item to calculate the new value.
   * 
   * @param item item we wants to move
   * @param x		 New coordinate x when moving. 
   * @param y 	 New coordinate y when moving.
   */
  public static void moveDragItem(Item item, int x, int y) {
  	Objects.requireNonNull(item);
  	BoundingBox bb = dragItemMap.get(item);
  	int addX = x - oldPosition.x();
  	int addY = y - oldPosition.y();
  	var nw = bb.northWest();
  	var se = bb.southEast();
  	dragItemMap.put(item,  new BoundingBox(
								  	         new XY(nw.x() + addX, nw.y() + addY),
								  	         new XY(se.x() + addX, se.y() + addY)
										  	   )
  	);
  }
  
  /**
   * Check if we click in a choice during an event.
   * Every event has 2 buttons, and a button to end an event.
   * 
   * First we check if we click the end button event.
   * Then we check if we click a choice event.
   *  
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   * @return 1 or 2 if we click a choice
   * 				 3 if we click the end button
   * 				 else -1
   */
  private static int eventClick(int x, int y) {
  	if (data.event() == null) {
  		return -1;
  	}
  	int res;
  	if (data.event().getRoot().getChoice2() == null) { // If there's no second choice, we're in the case where it's the endbutton
  		res = eventEndClick(x, y);
  	}
  	else {
    	res = eventChoiceClick(x, y);
  	}
  	return res;
  }
  
  /**
   * Check if we click inside the end button event
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   * 
   * @return 3 if click in the endbutton
   * 				 else -1
   */
  private static int eventEndClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("BG_CHOICE_END").box();
  	if (data.event().getRoot().getChoice2() == null) {
    	if (boundingBox.northWest().x() <= x  && x <= boundingBox.southEast().x()) {
    		if (boundingBox.northWest().y() <= y  && y <= boundingBox.southEast().y()) {
    			return 3;
    		}
    	}
  	}
  	return -1;
  }
  
  /**
   * Check if we click inside the choice button event
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   * 
   * @return 1 if click the first choice endbutton
   * 				 2 if click the first choice endbutton
   * 				 else -1
   */
  private static int eventChoiceClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("BG_CHOICE_END").box();
  	for (int i = 0; i < 2; i++) {
  		String key = "BG_CHOICE" + Integer.toString(i + 1);
  		boundingBox = MathLoader.getMapEvent().get(key).box();
    	if (boundingBox.northWest().x() <= x  && x <= boundingBox.southEast().x()) {
    		if (boundingBox.northWest().y() <= y  && y <= boundingBox.southEast().y()) {
    			return i + 1;
    		}
    	}
  	}  
  	return -1;
  }
  
  /**
   * Check if we click in a mob during a combat, and swap the target to him.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void mobClick(int x, int y) {
  	GameDataCombat.getEnemyBox().forEach((enemy, boundingBox) -> {
  		var NW = boundingBox.northWest();
  		var SE = boundingBox.southEast();
  		if (x >= NW.x() && x <= SE.x()) {
  			if (y >= NW.y() && y <= SE.y()) {
  				GameDataCombat.setTarget(enemy);
  			}
  		}
  	}); ;
  }
  
  /**
   * Check if we click the end button during a combat.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   * 
   */
  private static void endButtonClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("BG_ENDTURN").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				GameDataCombat.endTour(data);
			}
		}
  }
  
  /**
   * Check if we hover the bin button.
   * We use this method to create the animation with the bin when holding an item above
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  public static void binHover(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("BG_BIN_OPEN").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.setBin(true);
				return;
			}
		}
		data.setBin(false);
  }
  
  /**
   * Check if after releasing the mouseclick, we're above the bin button.
   * If that, remove the item definetely from the game.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void binClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("BG_BIN_OPEN").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.setBin(false);
				var item = data.dragItem();
				switch(item) {
					case Curse _ -> {}
					default -> removeItemFromDrag(data.dragItem());
				}
				return;
			}
		}
  }
  
  /**
   * Check if we click the exit button in the shop.
   * If that, exit the shop.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void exitButtonClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("ICON_EXIT_SHOP").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.setShop(false, null);
				return;
			}
		}
  }
  
  /**
   * Check if we click one of the arrow button in the shop.
   * We call the method for the left and right arrow.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void arrowButtonClick(int x, int y) {
  	leftArrowButtonClick(x, y);
  	rightArrowButtonClick(x, y);
  }
  
  /**
   * Check if we click the left button in the shop.
   * If that, swipe the item lst by one in the left.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void leftArrowButtonClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("ICON_SHOP_LEFT").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.getShopLst().leftShiftShop();
			}
		}
  }
  
  /**
   * Check if we click the right button in the shop.
   * If that, swipe the item lst by one in the right.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void rightArrowButtonClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("ICON_SHOP_RIGHT").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.getShopLst().rightShiftShop();
			}
		}
  }
  
  /**
   * Check if we click the buy button in the shop.
   * If that, buy the item and remove it from the shop.
   * 
   * @param  x Coordinate x of the mouse.
   * @param  y Coordinate y of the mouse.
   */
  private static void buyButtonClick(int x, int y) {
  	var boundingBox = MathLoader.getMapEvent().get("ICON_SHOP_BUY").box();
		var NW = boundingBox.northWest();
		var SE = boundingBox.southEast();
		if (x >= NW.x() && x <= SE.x()) {
			if (y >= NW.y() && y <= SE.y()) {
				data.getShopLst().buy(data.bag());
			}
		}
  }
  
  /**
   * Check if when moving the mouse, we're above the sell button in shop.
   * This method is not triggered if we're not in the shop.
   * 
   * @param x Coordinate X of the mouse
   * @param y Coordinate Y of the mouse
   */
  public static void sellButtonHover(int x, int y) {
		if(data.getShop() && data.dragItem() != null) {
			var boundingBox = MathLoader.getMapEvent().get("SHOP_SELL_ARTICLE").box();
			var NW = boundingBox.northWest();
			var SE = boundingBox.southEast();
			if (x >= NW.x() && x <= SE.x()) {
				if (y >= NW.y() && y <= SE.y()) {
					data.getShopLst().setSellItemPrice(data.dragItem());
				}
			}
		}
	}

  /**
   * Check if when releasing the mouse, we're above the sell button in shop.
   * This method is not triggered if we're not in the shop.
   * 
   * @param x Coordinate X of the mouse
   * @param y Coordinate Y of the mouse
   */
  private static void sellButtonClick(int x, int y) {
		if(data.dragItem() != null) {
			var boundingBox = MathLoader.getMapEvent().get("SHOP_SELL_ARTICLE").box();
			var NW = boundingBox.northWest();
			var SE = boundingBox.southEast();
			if (x >= NW.x() && x <= SE.x()) {
				if (y >= NW.y() && y <= SE.y()) {
					data.getShopLst().setSellItem(data.dragItem());
					removeItemFromDrag(data.dragItem());
					addDragItem(new Gold(data.dragItem().info().score() / 2));
					data.setDragItem(null);
				}
			}
		}
	}
  
  /**
   * Main method treating the click and returning information about what we clicks.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
   * @return {@code ClickResult} that gives information of what we clicks.
   */
  public static ClickResult click(int x, int y) {
    return firstNonNull(x, y,
        GameDataClick::clickItem,
        GameDataClick::clickBag,
        GameDataClick::clickMap,
        GameDataClick::clickMapOrBag,
        GameDataClick::clickEvent,
        GameDataClick::clickCombat,
        GameDataClick::clickShop
    );
  }
  
  /**
   * Take a list of methods and for each of them test
   * if we're inside, return a {@code ClickResult}.
   * 
   * @param x 				coordinate x of the mouse click
   * @param y 				coordinate y of the mouse click
   * @param handlers	List of all methods
   * 
   * @return {@code ClickResult} containing information.
   */
  private static ClickResult firstNonNull(int x, int y, ClickHandler... handlers) {
	  for (ClickHandler h : handlers) {
	      ClickResult res = h.handle(x, y);
	      if (res != null) return res;
	  }
	  return new ClickResult(ClickType.NOTHING, null);
	}
  
  @FunctionalInterface
  interface ClickHandler {
      ClickResult handle(int x, int y);
  }
  
  /**
   * Checks whether the mouse click targets an item.
   * 
   * This method determines if an {@link Item} has been clicked, either on the screen
	 * or inside the backpack. If an item is detected, it wraps the result into a
	 * {@link ClickResult} with the type {@link ClickType#ITEM}.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code ClickResult} of type {@code ITEM} containing the clicked item,
	 *         {@code null} if no item was clicked
   */
  private static ClickResult clickItem(int x, int y){
      Item item = itemClick(x, y);
      return item == null ? null : new ClickResult(ClickType.ITEM, item);
  }
  
  /**
   * Checks whether the mouse click targets a case in the bag.
   * 
   * This method determines if a case in the {@link Backpack} has been clicked. 
   * If detected, it wraps the result into a {@link ClickResult} with the type {@link ClickType#BAG}.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code ClickResult} of type {@code BAG} containing the clicked item,
	 *         {@code null} if no item was clicked
   */
  private static ClickResult clickBag(int x, int y) {
      XY bag = bagClick(x, y);
      return bag == null ? null : new ClickResult(ClickType.BAG, bag);
  }

  /**
   * Checks whether the mouse click targets a case in the map.
   * 
   * This method determines if a case in the {@link Floor} has been clicked. 
   * If detected, it wraps the result into a {@link ClickResult} with the type {@link ClickType#MAP}.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code ClickResult} of type {@code MAP} containing the clicked item,
	 *         {@code null} if no item was clicked
   */
  private static ClickResult clickMap(int x, int y) {
      if (data.mapOrBag()) return null;
      XY map = floorClick(x, y);
      return map == null ? null : new ClickResult(ClickType.MAP, map);
  }

  /**
   * Checks whether the mouse click targets the switch button between map and bag.
   * 
   * This method determines if the switch button has been clicked. 
   * If detected, it wraps the result into a {@link ClickResult} with the type {@link ClickType#MAP}.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code ClickResult} of type {@code MAP_OR_BAG} containing the clicked item,
	 *         {@code null} if no item was clicked
   */
  private static ClickResult clickMapOrBag(int x, int y) {
      int v = mapOrBagClick(x, y);
      return v == 0 ? null : new ClickResult(ClickType.MAP_OR_BAG, v);
  }

  /**
   * Checks whether during an event, the mouse click targets a choice button.
   * 
   * If detected, it wraps the result into a {@link ClickResult} with the type {@link ClickType#EVENT_CHOICE}.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code ClickResult} of type {@code EVENT_CHOICE} containing the clicked item,
	 *         {@code null} if no item was clicked
   */
  private static ClickResult clickEvent(int x, int y) {
      int choice = eventClick(x, y);
      return choice == -1 ? null : new ClickResult(ClickType.EVENT_CHOICE, choice);
  }
  
  /**
   * Checks whether during a combat, the mouse click targets an enemy or the endturn button.
   * 
   * If detected, it apply the consequence when clicking.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code null} since the result is not usefull
   */
  private static ClickResult clickCombat(int x, int y) {
      if (!GameDataCombat.combat()) return null;
      if (!dragItemMap.isEmpty()) {
          GameDataCombat.addLog("Range tes items pour pouvoir jouer");
          return null;
      }
      mobClick(x, y);
      endButtonClick(x, y);
      return null;
  }
  
  
  /**
   * Interact with all elements in the shop
   * If detected, it apply the consequence when clicking.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
	 * @return {@code null} since the result is not usefull
   */
  private static ClickResult clickShop(int x, int y) {
      if (!data.getShop()) return null;

      if (!data.getShopLst().getCurrentShop().isEmpty()) {
          arrowButtonClick(x, y);
          buyButtonClick(x, y);
      }
      exitButtonClick(x, y);
      return null;
  }
  
	/**
   * Check where the mouse is when we release the mouse.
   * 
   * We call this method when releasing a dragging an item on something (bin button, sell button, bag)
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   */
  public static void clickUp(int x, int y) {
  	XY coord = bagClick(x, y);
  	if (coord != null) {
			var item = 	data.dragItem().setXY(GameDataClick.bagClick(x, y));
			if (data.bag().addItemToBackpack(item)) {
				removeItemFromDrag(data.dragItem());
			} else {
				var itemDrag = data.dragItem();
				var colideItem = data.bag().getItem(coord.x(), coord.y());
				if (item.canMerge()) {
					if (colideItem != null && itemDrag.info().ID() == colideItem.info().ID()) {
						switch(itemDrag) {
						case Gold goldDrag -> {
							var goldCollide = (Gold) colideItem;
							data.bag().removeItemFromBackpack(goldCollide);
							goldCollide = goldCollide.addGoldValue(goldDrag.value());
							data.bag().addItemToBackpack(goldCollide);
						}
						default -> {
							data.bag().removeItemFromBackpack(colideItem);
							colideItem = colideItem.addDurability(itemDrag.durability());
							data.bag().addItemToBackpack(colideItem);
						}
						}
						removeItemFromDrag(itemDrag);
					} else addDragItem(data.dragItem());
				} else {
					addDragItem(data.dragItem());
				}
			}
  	}
  	if (!GameDataCombat.combat()) {
  		binClick(x, y);
  	}
  	if (data.getShop()) {
  		sellButtonClick(x, y);
  	}
  }
  
  public static LinkedHashMap<Item, BoundingBox> getDragItemMap() {
  	return dragItemMap;
  }
}
