package game.data;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.forax.zen.ScreenInfo;

import game.GameData;
import game.GameMath;
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
  private static XY old_position;
	/**
   * Contains all items movable in the screen.<br>
   * For exemple, if there's one item, we can only only move this item. <br>
   * If {@code itemAdd} is empty, no items can be move
   */
  private static LinkedHashMap<Item, BoundingBox> itemAdd;
	
	public GameDataClick(GameData data_) {
		data = data_;
		backpack = data.bag();
		screenInfo = data.screenInfo();
		itemAdd = data.map_item();
	}
	
	/**
   * Methods to check if we click inside the bag
   * @param x					 Coordinate x we click
   * @param y 				 Coordinate y we click
   * 
   * @return -2 if we click a lock case, -1 if we click a free case else, ID of the weapon
   * 				  0 if we click outside of the bag.
   */
  public static XY bag_click(int x, int y) {
  	int grid_size = backpack.grid_size();
  	double left_grid = (screenInfo.width() / 2) - 3.5 * grid_size;
  	double up_grid = (screenInfo.height() / 4.5) - 2.5 * grid_size;
  	if (data.mapOrBag() == false) {
  	  return new XY(-1, -1);
  	}
  	if(x < left_grid || x > (left_grid + 7 * grid_size) ||
  		 y < up_grid   || y > (up_grid + 5 * grid_size)
  		) {
  		return new XY(-1, -1);
  	}
  	int new_x = (int) (x - left_grid) / grid_size;
  	int new_y = (int) (y - up_grid) / grid_size;
  	return new XY(new_x, new_y);
  }
  
  
  /**
	 * Check the column of the the clicked position inside the grid
	 * 
	 * <p>The grid is composed of square cells of size {@code grid_size}, separated by a constant gap.</br>
	 * This method determines in which row the x-coordinate of a mouse click falls.</p>
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
	 * @param left_grid The y-coordinate of the top of the grid.
	 * @param grid_size The height of each grid cell.
	 * @param gap       The horizontal gap between two grid cells.
	 * @param xy        The y-coordinate of the click.
	
	 * @return The column index (0 to 11), or -1 if the click is outside the grid cells.
	 */
	private static int check_mapYclick(double up_grid, double grid_size, double gap, int y) {
		double positionY = up_grid;
		int newY = -1;
		for (var i = 0; i < 5; i++) {
			if (positionY <= y && y <= positionY + grid_size) {
				newY = i;
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
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
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
   * Main function that look if we click on a items draggable on the screen
   * 
   * @param x	Coordinate x we click
   * @param y Coordinate y we click
   * 
   * @return {@code Item} 
   */
  private static Item item_click(int x, int y) {
  	if (GameDataCombat.combat()) {
  		return null;
  	}
  	Item item;
  	for (Map.Entry<Item, BoundingBox> entry : itemAdd.entrySet()) {
      item = entry.getKey();
      BoundingBox box = entry.getValue();
      if (x >= box.northWest().x() && x <= box.southEast().x() &&
          y >= box.northWest().y() && y <= box.southEast().y()) {
          return item;
      }
  	}
  	var res = bag_click(x,y);
  	if (res.x() != -1 && res.y() != -1) {
  		if ((item = backpack.get_item(res.x(), res.y())) == null) {
  	    return null;
  		}
  		GameDataBackpack.remove_itemFromBackpack(item);
  		add_itemFromBag(item, res.x(), res.y());
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
  public static void set_oldPosition(int x, int y) {
  	old_position = new XY(x, y);
  }
  
  /**
   * Add an item in the list of items draggable.
   * 
   * @param item item we wants to move
   */
  public static void add_item(Item item) {
  	itemAdd.put(item, new BoundingBox(
											new XY(screenInfo.width() / 2 - (item.getWidth() * backpack.grid_size() / 2) , screenInfo.height() / 2 - (item.getHeight() * backpack.grid_size() / 2) ),
											new XY(screenInfo.width() / 2 + (item.getWidth() * backpack.grid_size() / 2) , screenInfo.height() / 2 + (item.getHeight() * backpack.grid_size() / 2) ))
							 );
  }
  
  public static void update_boundingBox(Item item, int x, int y) {
  	int minX = x - (item.getWidth() * backpack.grid_size() / 2);
    int minY = y - (item.getHeight() * backpack.grid_size() / 2);
  	int maxX = x + (item.getWidth() * backpack.grid_size() / 2);
    int maxY = y + (item.getHeight() * backpack.grid_size() / 2);
    itemAdd.put(item, new BoundingBox(new XY(minX, minY), new XY(maxX, maxY)));
  }
  
  /**
   * Add an item from the bag in the list of items draggable.
   * 
   * @param item item we wants to move
   */
  public static void add_itemFromBag(Item item, int x, int y) {
  	var size = backpack.grid_size();
  	var NW = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)), (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * (y - 1))));
  	var SE = new XY((int) (screenInfo.width() / 2 - 3.5 * size + (size * x)) + item.getWidth() * size, (int) (data.screenInfo().height()/4.5 - 2.5 * size + (size * y) + item.getHeight() * size));
  	itemAdd.put(item, new BoundingBox(NW, SE));
  }
  
  /**
   * Updata the BoundingBox of the items we're currently moving. 
   * 
   * @param item item we wants to move
   * @param x	New coordinate x when moving. 
   * @param y New coordinate y when moving.
   */
  public static void move_item(Item item, int x, int y) {
  	int addX = x - old_position.x();
  	int addY = y - old_position.y();
  	var new_NW = new XY(itemAdd.get(item).northWest().x() + addX, itemAdd.get(item).northWest().y() + addY);
  	var new_SE = new XY(itemAdd.get(item).southEast().x() + addX, itemAdd.get(item).southEast().y() + addY);
  	itemAdd.put(item, new BoundingBox(new_NW, new_SE));
  }
  
  public static int EventChoice_click(int x, int y) {
  	if (data.event() == null) {
  		return -1;
  	}
  	for (int i = 0; i < 2; i++) {
  		String key = "BG_CHOICE" + Integer.toString(i + 1);
  		var boundingBox = GameMath.getMapEvent().get(key).box();
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
  	Item item = item_click(x, y);
    if (item != null) {
        return new ClickResult(ClickType.ITEM, item);
    }

    XY bag = bag_click(x, y);
    if (bag.x() != -1) {
        return new ClickResult(ClickType.BAG, bag);
    }

    XY mapPos = map_click(x, y);
    if (mapPos.x() != -1) {
        return new ClickResult(ClickType.MAP, mapPos);
    }

    int mob = mapOrBag_click(x, y);
    if (mob != 0) {
        return new ClickResult(ClickType.MAP_OR_BAG, mob);
    }
    int choiceNumber = EventChoice_click(x, y);
    if (choiceNumber != -1) {
    	return new ClickResult(ClickType.EVENT_CHOICE, choiceNumber);
    }
    
    return new ClickResult(ClickType.NOTHING, null);
  }
}
