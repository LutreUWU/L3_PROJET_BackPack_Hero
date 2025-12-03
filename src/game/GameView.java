package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import game.data.GameDataCombat;
import loader.MathLoader;
import model.Block;
import model.BoundingBox;
import model.Direction;
import model.Item;
import model.XY;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Shop;
import model.map.Start;
import model.map.Treasure;
import model.monster.Enemy;

 /**
  * The SimpleGameView class deals with the display of the game the screen, and
  * with the interpretation of which zones were clicked on by the user.
  * 
  * @param width    	Width of the windows screen
  * @param height	Height of the windows screen
  * @param grid_size Size of a grid in the bag
  *
  */
public record GameView(int width, int height, int grid_size) {	
  /**
   * Create a new GameView
   * 
   * @param width    	Width of the windows screen
   * @param height	Height of the windows screen
   * @param grid_size Size of a grid in the bag
   * @return SimpleGameView
   */
  public static GameView initGameGraphics(int width, int height, int grid_size) {
  	return new GameView(width, height, grid_size);
  }
  
  /**
	 * Draw the background of the game
	 * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
	 */
	private static void drawBG(Graphics2D graphics, GameData data) {
    // A ENLEVER
		graphics.setColor(Color.GRAY);
		graphics.fill(new Rectangle2D.Double(0, 0, data.screenInfo().width(), data.screenInfo().height()));
		/////////////
		BufferedImage img = data.imgMap().get("BG1");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG1").transform(), null);
	}
	
	/**
	 * Draw an image in the window
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
	 */
	public static void drawElement(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX / 2.0;
	  double centerY = y + dimY / 2.0;
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width / 2, -height / 2);
	  graphics.drawImage(img, transform, null);
	}
	
	/**
   * <p>
   * Draw an image but with a specifity.<br>
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
   * </p>
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
	 */
	public static void drawSpecialElement(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction, double marginX, double marginY) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX / 2.0;
	  double centerY = y + dimY / 2.0;
	  // J'ai aucune idée pq ça marche, mais ça a l'air de marché ???
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width * marginX, -height * marginY); 
	  graphics.drawImage(img, transform, null);
	}
	
	/**
   * <p>
   * Draw an item in the bag but with a specifity.<br>
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
   * </p>
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
	 */
	public static void drawSpecialElementInBag(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction, double marginX, double marginY) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX * marginX;
	  double centerY = y + dimY * marginY;
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width * marginX, -height * marginY); // We modify this
	  graphics.drawImage(img, transform, null);
	}
	
  /** 
   * Draw the grid of the Backpack
   * @param context Which window to draw
   * @param data	  Data of the game
   */	
  private static void drawGrid(Graphics2D graphics, GameData data) {
    int size = data.bag().getGridSize();
		int [][] grid = data.bag().grid();
		BufferedImage imgBackpack = data.imgMap().get("bag");
		BoundingBox boundingBox = MathLoader.getMapEvent().get("BG_BACKPACK").box(); 
		graphics.drawImage(imgBackpack, MathLoader.getMapEvent().get("BG_BACKPACK").transform(), null);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
	    	final int fi = i;
	    	final int fj = j;				  
		  	if (grid[fi][fj] >= -1) {
		  		graphics.drawImage(data.imgMap().get("BG_BAG_UNLOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  		
		  	}
		  	if (grid[fi][fj] == -2) {
		  		graphics.drawImage(data.imgMap().get("BG_BAG_LOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  	}
			}
    }
  }
  
  /**
   * Check the id int the bag we wants to draw and calls the appropriate method.<br>
   * This method use a switch on the item id to know which item we wants to draw.
   * 
   * @param graphics {@Code Graphics2D} of the game
   * @param data	   Data of the game
   */
  private static void drawItemBag(Graphics2D graphics, GameData data) {
		var itemLst = data.bag().bagItemLst();
		for (var item : itemLst) {
			Block coordinate = item.shape()[0];
		  switch (item.getID()) {
			  case 1 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 2, item.direction(), data.imgMap().get("keyDoor"));
			  case 2 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 1, item.direction(), data.imgMap().get("gold"));
				case 3 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("sword")); 
				case 4 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), data.bag().getGridSize(), 2, 2, item.direction(), data.imgMap().get("despairShield")); 
				case 5 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("mimicry")); 
				case 6 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("massue")); 
				case 7 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 1, 2, item.direction(), data.imgMap().get("gant"), 0.5, 0.75); 
				case 8 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), data.bag().getGridSize(), 2, 3, item.direction(), data.imgMap().get("axe"), 0.20, 0.5); 

				default ->{}
		  }
		}
  }
  
  /**
   * Draw an item in the backpack
   * 
   * @param graphics	{@code Graphics2D} of the game
   * @param pos				{@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param size			Size of a tile in the backpack
   * @param width			Number of tile horizontally
   * @param height		Number of tile vertically
   * @param direction	Direction the img aim
   * @param img				{@code BufferedImage} of the item
   */
	private static void drawInBag(Graphics2D graphics, XY pos, int size, int width, int height, Direction direction, BufferedImage img){
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		GameView.drawElement(graphics, img, 
																	 coord.northWest().x() + (size * pos.x()) + centerX,
																	 coord.northWest().y() + (size * pos.y()) - centerY, 
																	 size * width, size * height, direction);
	}
	
	/**
   * Draw an item with a special shape in the backpack
   * 
   * @param graphics	{@code Graphics2D} of the game
   * @param pos				{@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param size			Size of a tile in the backpack
   * @param width			Number of tile horizontally
   * @param height		Number of tile vertically
   * @param direction	Direction the img aim
   * @param img				{@code BufferedImage} of the item
   * @param marginX		Value between 0.0 and 1.0 indicating the gap horizontally
   * @param marginY		Value between 0.0 and 1.0 indicating the gap vertically
   */
	private static void drawInBagSpecial(Graphics2D graphics, XY pos, int size, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY){
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		GameView.drawSpecialElementInBag(graphics, img, 
																	 coord.northWest().x() + (size * pos.x()) + centerX,
																	 coord.northWest().y() + (size * pos.y()) - centerY, 
																	 size * width, size * height, direction, marginX, marginY);
	}
	
	/**
   * Draws the hero in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   */
  private static void drawHero(Graphics2D graphics, GameData data) {
  	double size_x = data.hero().getSizeX();
  	double size_y = data.hero().getSizeY();
		BufferedImage img = data.imgMap().get("Roland");
		drawElement(graphics, img, data.screenInfo().width() * 0.20, data.screenInfo().height() * 0.50, size_x, size_y, Direction.UP);
		drawHeroStats(graphics, data, (int) (data.screenInfo().width() * 0.20 + size_x/2),  (int) (data.screenInfo().height() * 0.50 + size_y));
  }
  
  /**
   * Draws all the information about the hero
   * 
   * @param graphics {@code Graphics2D} object for drawing.
   * @param data 		 GameData containing the game data. 
   * @param x				 coordinate x where we wants to draw.
   * @param y				 coordinate y where we wants to draw.
   */
  private static void drawHeroStats(Graphics2D graphics, GameData data, int x, int y) {
  	int size = 14;
    Font font = new Font("Arial", Font.PLAIN, size);
    graphics.setColor(Color.BLUE);
		graphics.setFont(font);
	  graphics.drawString("PV : " + data.hero().getHP() + "/" + data.hero().getMax_HP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(data.hero().getCurrent_protection()), x,	y + size*2);
	  graphics.drawString("AP : " + String.valueOf(data.hero().getEnergy_point()), x,	y + size*3);
	  graphics.drawString("MANA : " + String.valueOf(data.hero().getMana_point()), x,	y + size*4);
	  graphics.drawString("EXP : " + String.valueOf(data.hero().getXp()) + "/" + String.valueOf(10 + ((data.hero().getLevel() - 1) * 2)), x,	y + size*5);
	  graphics.drawString("Gold : " + String.valueOf(data.hero().getGold()), x,	y + size*6);
  }
  
  /**
   * Draw the button for switching between map and bag
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private static void drawButton(Graphics2D graphics, GameData data) {
		graphics.setColor(Color.RED);  		
		graphics.setColor(data.mapOrBag() ? Color.ORANGE : Color.CYAN);
    graphics.fill(new Rectangle2D.Double(data.screenInfo().width() - data.bag().getGridSize() / 2, data.screenInfo().height()/3.5 - 2.5*data.bag().getGridSize(), 
    																		 data.bag().getGridSize() / 2,data.bag().getGridSize() / 2));
  }
  
  /**
   * Draw the map in the screen
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private static void drawMap(Graphics2D graphics, GameData data) {
  	BufferedImage imgMap = data.imgMap().get("BG_MAP");
  	var leftGrid = MathLoader.getMapEvent().get("BG_MAP").box();
  	var size = data.bag().getGridSize();  	
  	var gap = size * 0.1;
  	graphics.drawImage(imgMap, MathLoader.getMapEvent().get("BG_MAP").transform(), null);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 11; j++) {
	    	final int fi = i;
	    	final int fj = j;
		  	var coordXY = new XY(fj, fi);
		  	int newX = (int) (gap * fj) + leftGrid.northWest().x() + (size * fj);
		  	int newY = (int) (gap * fi) + leftGrid.northWest().y() + (size * fi);
		  	if (data.map().getHeroVisible().contains(coordXY)) {
		  		graphics.drawImage(data.imgMap().get("BG_MAP_TILE"), newX, newY, size, size, null);
			  	switch(data.map().getGrid()[fi][fj]) {
			  		case Shop _ -> graphics.setColor(Color.YELLOW);
			  		case EnemyRoom _ -> graphics.setColor(Color.RED);
			  		case EventRoom _ -> graphics.setColor(Color.PINK);
			  		case Healer _ -> graphics.setColor(Color.GREEN);
			  		case Start _ -> graphics.setColor(Color.BLUE);
			  		case Exit _ -> graphics.setColor(Color.CYAN);
			  		case LockedDoor _ -> graphics.setColor(Color.BLACK);
			  		case Treasure _ -> graphics.setColor(Color.LIGHT_GRAY);
			  		default ->  graphics.setColor(Color.GRAY);
			  	}
		  	} else graphics.setColor(Color.DARK_GRAY);
		  	// Finir les images quand Arthur aura réglé le bug d'actualisation là
		  	// graphics.drawImage(data.imgMap().get("ICON_EVENT"),newX, newY, size, size, null);
		    graphics.fill(new Rectangle2D.Double((gap * fj) + leftGrid.northWest().x() + (size * fj), 
		    																		 (gap * fi) + leftGrid.northWest().y() + (size * fi), 
		    																			size, size));
		    graphics.setColor(Color.BLACK);
		    graphics.draw(new Rectangle2D.Double((gap * fj) + leftGrid.northWest().x() + (size * fj), 
		    																		 (gap * fi) + leftGrid.northWest().y() + (size * fi), 
																							size, size));
		    
		    if (data.map().getHeroAccessible().contains(coordXY)) {
		    	graphics.setColor(Color.MAGENTA);
		      graphics.fill(new Rectangle2D.Double((gap * fj) + leftGrid.northWest().x() + (size * fj) + size/4, 
		  																		  	 (gap * fi) + leftGrid.northWest().y() + (size * fi) + size/4, 
		  																					size/2, size/2));
		      graphics.draw(new Rectangle2D.Double((gap * fj) + leftGrid.northWest().x() + (size * fj) + size/4, 
																					  	 (gap * fi) + leftGrid.northWest().y() + (size * fi) + size/4, 
																								size/2, size/2));
		    }
      }
		}
		
		// A RETIRER QUAND ON AURA FINIT DE CRER LES MAPS
  	graphics.setColor(Color.ORANGE);
  	graphics.setStroke(new BasicStroke(5));
  	for (var coord : data.map().getHeroVisibleLine()) {
  		for (var coord_acc : data.map().getGrid()[coord.y()][coord.x()].getAccessible()) {
  			graphics.drawLine((int) ((gap * coord.x()) + (leftGrid.northWest().x() + (size * coord.x() + size/2))), 
													(int) ((gap * coord.y()) + (leftGrid.northWest().y() + (size * coord.y() + size/2))), 
													(int) ((gap * coord_acc.x()) + (leftGrid.northWest().x() + (size * coord_acc.x()) + size/2)), 
													(int) ((gap * coord_acc.y()) + (leftGrid.northWest().y() + (size * coord_acc.y()) + size/2)));
  		}
  	}
		
		graphics.setColor(Color.WHITE);
		var coord = data.map().getHeroPos();
    graphics.fill(new Rectangle2D.Double(((gap * coord.x()) + leftGrid.northWest().x() + (size * coord.x()) + size/4), 
																		  	 ((gap * coord.y()) + leftGrid.northWest().y() + (size * coord.y()) + size/4), 
																					size/2, size/2));
		///////////////////////////////////
  }

	
  /**
   * Update the position of each weapons we can move in the screen.
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data     GameData containing the game data.
   */	
  public static void updateDragItem(Graphics2D graphics, GameData data) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(data);
		data.dragItemLst().forEach((item, box) -> drawDrag(graphics, data, item, box));
  }
  
  /**
   * Check the id of the item we're dragging and calls the appropriate method.<br>
   * This method use a switch on the item id to know which item we wants to draw.
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data		 {@code GameData} containing the game data.
   * @param item		 The Item we're currently dragging.
   * @param box		 	 The boundingbox (border) of the item.
   */
  private static void drawDrag(Graphics2D graphics, GameData data, Item item, BoundingBox box) {
	  switch (item.getID()) {
		  case 1 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 2, item.direction(), data.imgMap().get("keyDoor")); 
		  case 2 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 1, item.direction(), data.imgMap().get("gold")); 
			case 3 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("sword")); 
			case 4 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 2, 2, item.direction(), data.imgMap().get("despairShield")); 
			case 5 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("mimicry")); 
			case 6 -> drawDragItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 3, item.direction(), data.imgMap().get("massue")); 
			case 7 -> drawDragSpecialItem(graphics, box.northWest(), data.bag().getGridSize(), 1, 2, item.direction(), data.imgMap().get("gant"), 0.5, 0.9); 
			case 8 -> drawDragSpecialItem(graphics, box.northWest(), data.bag().getGridSize(), 2, 3, item.direction(), data.imgMap().get("axe"), 0.20, 0.5); 
			default ->{}
	  }
	}
  
  /**
   * Draw the current item we're dragging.
   * 
   * @param graphics  {@code ApplicationContext} of the game.
   * @param coord		  {@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param size		  Size of a tile.
   * @param width		  Number of tile horizontally. 
   * @param height 	  Number of tile vertically.
   * @param direction {@code Direction} the item will aim.
   * @param img				{@code BufferedImage} of the item.
   */
  private static void drawDragItem(Graphics2D graphics, XY coord, int size, int width, int height, Direction direction, BufferedImage img){
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = size*width, dimY = size*height;
		// If direction is left or right, the North West coordinate need to be update in order to draw correctly
	  if (direction == Direction.LEFT || direction == Direction.RIGHT) { 
	  	centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			x = centerX - dimX / 2.0;
			y = centerY - dimY / 2.0;
		}
		GameView.drawElement(graphics, img, x, y, dimX, dimY, direction);
	}
  
  /**
   * <p>
   * Draw the current item we're dragging but with a specifity.<br>
   * Initially, the other method {@code drawDragItem} draw the item base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5
   * </p>
   * 
   * @param graphics  {@code ApplicationContext} of the game.
   * @param coord		  {@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param size		  Size of a tile.
   * @param width		  Number of tile horizontally. 
   * @param height 	  Number of tile vertically.
   * @param direction {@code Direction} the item will aim.
   * @param img				{@code BufferedImage} of the item.
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
   */
  private static void drawDragSpecialItem(Graphics2D graphics, XY coord, int size, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY){
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = size*width, dimY = size*height;
	  if (direction == Direction.LEFT || direction == Direction.RIGHT) {
	  	centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			x = centerX - dimX / 2.0;
			y = centerY - dimY / 2.0;
		}
		GameView.drawSpecialElement(graphics, img, x, y, dimX, dimY, direction, marginX, marginY);
	}
  
  /**
   * Update the state of the combat.
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   * @param lstEnemy List of all enemy we fight
   */
  public static void updateEnemy(Graphics2D graphics, GameData data,  ArrayList<Enemy> lstEnemy) {
  	Objects.requireNonNull(graphics);
  	Objects.requireNonNull(data);
  	Objects.requireNonNull(lstEnemy);
  	for (int i = 0; i < lstEnemy.size(); i++) {
  		var enemy = lstEnemy.get(i);
  		drawEnemy(graphics, data, enemy, lstEnemy.size(), i);
  	}
  }
  
  /**
   * Draws the enemy in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   * @param enemy 	Data of the enemy.
   */
  private static void drawEnemy(Graphics2D graphics, GameData data, Enemy enemy, int nb, int ind) {
  	double sizeX = data.hero().getSizeX() * enemy.getSizeX();
  	double sizeY = data.hero().getSizeY() * enemy.getSizeY();
  	double northWestX =  data.screenInfo().width() * 0.80 - data.hero().getSizeX() + (ind - (nb - 1) / 2.0) * data.hero().getSizeX() ;
  	double northWestY =  data.screenInfo().height() * (0.5 - (0.1 * (ind%2))) + (data.hero().getSizeY() - sizeY);
		drawElement(graphics, data.imgMap().get(enemy.getImg()), northWestX, northWestY, sizeX, sizeY, Direction.UP);
		drawEnemyStats(graphics, enemy, (int) northWestX, (int) (northWestY + sizeY));
  }
  
  /**
   * Draws all the information about the enemy
   * 
   * @param graphics {@code Graphics2D} object for drawing.
   * @param enemy 	 Data of the enemy.
   * @param x				 Coordinate x where we wants to draw.
   * @param y				 Coordinate y where we wants to draw.
   */
  private static void drawEnemyStats(Graphics2D graphics, Enemy enemy, int x, int y) {
  	int size = 14;
    Font font = new Font("Arial", Font.PLAIN, size);
    graphics.setColor(Color.WHITE);
		graphics.setFont(font);
	  graphics.drawString("PV : " + enemy.getHP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(enemy.getShield()), x,	y + size*2);
	  graphics.drawString("ACTION : " + String.valueOf(enemy.getAction()), x,	y + size * 3);
  }
  
  /**
   * Draw an event and their choices
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private static void drawEvent(Graphics2D graphics, GameData data) {
  	drawBgEvent(graphics, data);
  	drawTextEvent(graphics, data);
  	drawChoiceEvent(graphics, data);
  }
  
  /**
   * Draw the background image of the event
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private static void drawBgEvent(Graphics2D graphics, GameData data) {
  	BufferedImage img = data.imgMap().get("BG_EVENT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_EVENT").transform(), null);
  }
  
  /**
   * Draw the text of the event in the screen
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private static void drawTextEvent(Graphics2D graphics, GameData data){
  	int size = 30;
	  double top = MathLoader.getMapEvent().get("BG_EVENT").box().northWest().y();
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setColor(Color.WHITE);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    int textWidth = fm.stringWidth(data.event().getRoot().getQuestion());
		int x = data.screenInfo().width() / 2 - textWidth / 2;
		int y = (int) (top * 1.05)  + fm.getAscent() ;
	  graphics.drawString(data.event().getRoot().getQuestion(), x,	y);
  }
  
  /**
   * Draw the background image of a choice<br>
   * Also called the fonction to write the choice inside.
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private static void drawChoiceEvent(Graphics2D graphics, GameData data) {
  	var img1 = data.imgMap().get("BG_CHOICE1");
  	var img2 = data.imgMap().get("BG_CHOICE2");
  	var event1 = MathLoader.getMapEvent().get("BG_CHOICE1");
  	var event2 = MathLoader.getMapEvent().get("BG_CHOICE2");
  	int width = event1.box().southEast().x() - event1.box().northWest().x();
  	int height = event1.box().southEast().y() - event1.box().northWest().y();
  	if (data.event().getRoot().getChoice2() == null){
  		var img3 = data.imgMap().get("BG_CHOICE_END");
  		var event3 = MathLoader.getMapEvent().get("BG_CHOICE_END");
  		graphics.drawImage(img3, event3.transform(), null);
		  drawTextChoiceEvent(graphics, data, data.event().getRoot().getChoice1().getAnswer(), 
		  															(int) event3.box().northWest().x() + width / 2, 
		  															(int) (event3.box().northWest().y() + height * 0.5));
  	}
  	else {
  		graphics.drawImage(img1, event1.transform(), null);
  	  drawTextChoiceEvent(graphics, data, data.event().getRoot().getChoice1().getAnswer(), 
  	  															(int) event1.box().northWest().x() + width / 2, 
  	  															(int) (event1.box().northWest().y() + height * 0.5));
  		graphics.drawImage(img2, event2.transform(), null);
  	  drawTextChoiceEvent(graphics, data, data.event().getRoot().getChoice2().getAnswer(), 
  	  															(int) event2.box().northWest().x() + width / 2, 
  	  															(int) (event2.box().northWest().y() + height * 0.5));
  	}
  }
  
  /**
   * Draw the text inside each choice. <br>
   * The text is draw at the center of the box choice.
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   * @param content	 Text we wants to write.
   * @param x				 The x coordinate of the text
   * @param y				 The y coordinate of the text
   */
  private static void drawTextChoiceEvent(Graphics2D graphics, GameData data, String content, int x, int y) {
    int size = 20;
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    graphics.setColor(Color.WHITE);
    int maxCharsPerLine = 30;
    String[] words = content.split(" ");
    StringBuilder line = new StringBuilder();
    int lineCount = 0;
    for (String word : words) {
        if (line.length() + word.length() + 1 > maxCharsPerLine) {
            int lineWidth = fm.stringWidth(line.toString());
            graphics.drawString(line.toString(), x - lineWidth / 2, y + lineCount * fm.getHeight());
            line = new StringBuilder(word);
            lineCount++;
        } else {
            if (line.length() > 0) line.append(" ");
            line.append(word);
        }
    }
    if (line.length() > 0) {
        int lineWidth = fm.stringWidth(line.toString());
        graphics.drawString(line.toString(), x - lineWidth / 2, y + lineCount * fm.getHeight());
    }
  }
  
  /**
   * Methods for drawing the game 
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  public static void draw(ApplicationContext context, GameData data) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		context.renderFrame(graphics -> {
			drawBG(graphics, data);
			if (data.mapOrBag()) {
				drawGrid(graphics, data);
				drawItemBag(graphics, data);
				if(!data.dragItemLst().isEmpty()) {
					GameView.updateDragItem(graphics, data);
				}
			} else {
				drawMap(graphics, data);
			}
			drawHero(graphics, data);
			drawButton(graphics, data);
			// Draw enemy if we're in combat
			if (GameDataCombat.combat()) {
				updateEnemy(graphics, data, GameDataCombat.getLstEnemy());
			}
			if (data.event() != null) {
				drawEvent(graphics, data);	
			}
	  });
  }
}
