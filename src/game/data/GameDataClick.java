package game.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.forax.zen.ScreenInfo;

import game.GameData;
import loader.MathLoader;
import model.Backpack;
import model.BoundingBox;
import model.Item;
import model.XY;

public class GameDataClick {
	private static GameData data;
	private static Backpack backpack;
	private static ScreenInfo screenInfo;
	/**
   * Has a record {@code XY} containing the mouse coordinate before moving the cursor.
   * 
   */
  private static XY oldPosition;
	/**
   * Contains all items movable in the screen.<br>
   * For exemple, if there's one item, we can only only move this item. <br>
   * If {@code itemAdd} is empty, no items can be move
   */
  private static LinkedHashMap<Item, BoundingBox> dragItemMap;
	
	public GameDataClick(GameData dataGame) {
		data = dataGame;
		backpack = data.bag();
		screenInfo = data.screenInfo();
		dragItemMap = data.dragItemLst();
	}
	
	/**
   * Methods to check if we click inside the bag
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * 
   * @return -2 if we click a lock case, -1 if we click a free case else, ID of the weapon
   * 				  0 if we click outside of the bag.
   */
  public static XY bagClick(int x, int y) {
  	int size = backpack.getGridSize();
  	double coordLeftGrid = (screenInfo.width() / 2) - 3.5 * size;
  	double coordTopGrid = (screenInfo.height() / 4.5) - 2.5 * size;
  	if (data.mapOrBag() == false) {
  	  return new XY(-1, -1);
  	}
  	if(x < coordLeftGrid || x > (coordLeftGrid + 7 * size) ||
  		 y < coordTopGrid   || y > (coordTopGrid + 5 * size)
  		) {
  		return new XY(-1, -1);
  	}
  	int newX = (int) (x - coordLeftGrid) / size;
  	int newY = (int) (y - coordTopGrid) / size;
  	return new XY(newX, newY);
  }
  
  
  /**
	 * Check the column of the the clicked position inside the grid
	 * 
	 * <p>The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.</br>
	 * This method determines in which row the x-coordinate of a mouse click falls.</p>
	 * 
	 * @param leftGrid The x-coordinate of the top of the grid.
	 * @param gridSize The height of each grid cell.
	 * @param gap       The horizontal gap between two grid cells.
	 * @param x         The x-coordinate of the click.
	
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
	 * Check the row of the the clicked position inside the grid
	 * 
	 * <p>The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.</br>
	 * This method determines in which row the y-coordinate of a mouse click falls.</p>
	 * 
	 * @param topGrid   The y-coordinate of the top of the grid.
	 * @param gridSize The height of each grid cell.
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
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   */
  private static XY floorClick(int x, int y) {
  	int size = backpack.getGridSize();
  	var gap = size * 0.1;
  	double leftGrid = (screenInfo.width() / 2) - 5.5 * size;
  	double topGrid = (screenInfo.height() / 5.5) - 2.5 * size;
  	if(x < leftGrid || x > (leftGrid + 11 * size + 10 * gap) ||
   		 y < topGrid   || y > (topGrid + 5 * size + 4 * gap)
   		) {
   		return new XY(-1, -1);
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
   * Main function that look if we click on a items draggable on the screen
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return {@code Item} 
   */
  private static Item itemClick(int x, int y) {
  	if (GameDataCombat.combat()) {
  		return null;
  	}
  	Item item;
  	for (Map.Entry<Item, BoundingBox> entry : dragItemMap.entrySet()) {
      item = entry.getKey();
      BoundingBox box = entry.getValue();
      if (x >= box.northWest().x() && x <= box.southEast().x() &&
          y >= box.northWest().y() && y <= box.southEast().y()) {
          return item;
      }
  	}
  	var res = bagClick(x,y);
  	if (res.x() != -1 && res.y() != -1) {
  		if ((item = backpack.getItem(res.x(), res.y())) == null) {
  	    return null;
  		}
  		GameDataBackpack.removeItemFromBackpack(item);
  		addDragItemFromBag(item, res.x(), res.y());
  		return item;
  	}
  	return null;
  }
  
  /**
   * Register the the position of the mouse before moving the cursor
   * 
   * @param x	Old coordinate x before we move
   * @param y Old coordinate y before we move
   */
  public static void setOldPosition(int x, int y) {
  	oldPosition = new XY(x, y);
  }
  
  /**
   * Add an item in the list of items draggable.
   * 
   * @param item item we wants to move
   */
  public static void addDragItem(Item item) {
  	dragItemMap.put(item, new BoundingBox(
											new XY(screenInfo.width() / 2 - (item.getWidth() * backpack.getGridSize() / 2) , screenInfo.height() / 2 - (item.getHeight() * backpack.getGridSize() / 2) ),
											new XY(screenInfo.width() / 2 + (item.getWidth() * backpack.getGridSize() / 2) , screenInfo.height() / 2 + (item.getHeight() * backpack.getGridSize() / 2) ))
							 );
  }
  
  public static void updateBoundingBox(Item item, int x, int y) {
  	int minX = x - (item.getWidth() * backpack.getGridSize() / 2);
    int minY = y - (item.getHeight() * backpack.getGridSize() / 2);
  	int maxX = x + (item.getWidth() * backpack.getGridSize() / 2);
    int maxY = y + (item.getHeight() * backpack.getGridSize() / 2);
    dragItemMap.put(item, new BoundingBox(new XY(minX, minY), new XY(maxX, maxY)));
  }
  
  /**
   * Add an item from the bag in the list of items draggable.
   * 
   * @param item item we wants to move
   */
  public static void addDragItemFromBag(Item item, int x, int y) {
  	var size = backpack.getGridSize();
  	var northWest = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)), (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * (y - 1))));
  	var southEast = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)) + item.getWidth() * size, (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * y) + item.getHeight() * size));
  	dragItemMap.put(item, new BoundingBox(northWest, southEast));
  }
  
  /**
   * Updata the BoundingBox of the items we're currently moving. 
   * 
   * @param item item we wants to move
   * @param x	New coordinate x when moving. 
   * @param y New coordinate y when moving.
   */
  public static void moveDragItem(Item item, int x, int y) {
  	int addX = x - oldPosition.x();
  	int addY = y - oldPosition.y();
  	var newNorthWest = new XY(dragItemMap.get(item).northWest().x() + addX, dragItemMap.get(item).northWest().y() + addY);
  	var newSouthEast = new XY(dragItemMap.get(item).southEast().x() + addX, dragItemMap.get(item).southEast().y() + addY);
  	dragItemMap.put(item, new BoundingBox(newNorthWest, newSouthEast));
  }
  
  public static int eventChoiceClick(int x, int y) {
  	if (data.event() == null) {
  		return -1;
  	}
  	var boundingBox = MathLoader.getMapEvent().get("BG_CHOICE_END").box();
  	if (data.event().getRoot().getChoice2() == null) {
    	if (boundingBox.northWest().x() <= x  && x <= boundingBox.southEast().x()) {
    		if (boundingBox.northWest().y() <= y  && y <= boundingBox.southEast().y()) {
    			return 3;
    		}
    	}
  	}
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
   * Main function treating the click and returning information about what we clicks.
   * 
   * @param x coordinate x of the mouse click
   * @param y coordinate y of the mouse click
   * 
   * @return Map<String, Integer> String give the information of what we clicks, Integer that can be usefull dependent on what we click
   */
  public static ClickResult click(int x, int y) {
  	// Here we add other click info
  	Item item = itemClick(x, y);
    if (item != null) {
        return new ClickResult(ClickType.ITEM, item);
    }

    XY bag = bagClick(x, y);
    if (bag.x() != -1) {
        return new ClickResult(ClickType.BAG, bag);
    }

    XY mapPos = floorClick(x, y);
    if (mapPos.x() != -1) {
        return new ClickResult(ClickType.MAP, mapPos);
    }
    

    int mob = mapOrBagClick(x, y);
    if (mob != 0) {
        return new ClickResult(ClickType.MAP_OR_BAG, mob);
    }
    int choiceNumber = eventChoiceClick(x, y);
    if (choiceNumber != -1) {
    	return new ClickResult(ClickType.EVENT_CHOICE, choiceNumber);
    }
    
    return new ClickResult(ClickType.NOTHING, null);
  }
  
  public static LinkedHashMap<Item, BoundingBox> getDragItemMap() {
  	return dragItemMap;
  }
}
