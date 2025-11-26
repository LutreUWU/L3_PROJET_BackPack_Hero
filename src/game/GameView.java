package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.github.forax.zen.ApplicationContext;

import game.ViewWeapon.SwordView;
import game.data.GameDataCombat;
import model.Block;
import model.BoundingBox;
import model.Direction;
import model.Item;
import model.XY;
import model.monster.Enemy;
import model.weapon.Sword;

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
   * Draw the grid of the Backpack
   * @param context Which window to draw
   * @param data	  Data of the game
   */	
  private static void drawGrid(Graphics2D graphics, GameData data) {
    int size = data.bag().grid_size();
		int [][] grid = data.bag().grid();
		drawElement(graphics, data.img_map().get("bag"), data.screenInfo().width() * 0.5 - size * 4.5, 10, size * 9, size * 6, Direction.UP);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
	    	final int fi = i;
	    	final int fj = j;				  
	    		// TO DO (David) : instead of rectangle, change image of the grid
			  	if (grid[fi][fj] >= -1) {
			  		graphics.setColor(Color.GRAY);
			  		graphics.fill(new Rectangle2D.Double((data.screenInfo().width() / 2) - 3.5 * size + (size * fj), 
																				    	   (data.screenInfo().height()/4.5) - 2.5*size + (size * fi), 
																									size, size));
			  		graphics.setColor(Color.BLACK);
			  	}
			  	if (grid[fi][fj] == -2) {
			  		graphics.setColor(Color.RED);
			  	}
			    graphics.draw(new Rectangle2D.Double((data.screenInfo().width() / 2) - 3.5 * size + (size * fj), 
																			    	   (data.screenInfo().height()/4.5) - 2.5*size + (size * fi), 
																								size, size));
			}
    }
  }
  
  /**
   * 
   * 
   */
  /**
   * Draw all items inside the bag.
   * 
   * @param context Which window to draw
   * @param data	  Data of the game
   */
  private static void drawItemBag(Graphics2D graphics, GameData data) {
		var item_list = data.bag().item_lst();
		for (var item : item_list) {
			Block coordinate = item.shape()[0];
		  switch (item.id()) {
				case 1 -> new SwordView(graphics, data, item.direction(), coordinate.x(), coordinate.y()).drawInBag();
			  default ->{}
		  }
		}
  }
	
  /**
   * Draw a virtual weapon in the grid of the backpack.
   * The weapon IS NOT in the bag.
   * It's for helping the user to choose where he wants to place his weapon.
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data	  Data of the game
   */
//  private static void drawWeaponGrid(Graphics2D graphics, GameData data) {
//		var item = data.weapon();
//		if (item == null) {
//	        return;
//	  }
//		Block coordinate = item.shape()[0];
//	  switch (item.id()) {
//			case 1 -> new SwordView(graphics, data, item.direction(), coordinate.x(), coordinate.y()).draw();
//		  default ->{}
//	  }
//  }
		
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
    graphics.setColor(Color.WHITE);
		graphics.setFont(font);
	  graphics.drawString("PV : " + data.hero().getHP() + "/" + data.hero().getMax_HP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(data.hero().getCurrent_protection()), x,	y + size*2);
	  graphics.drawString("AP : " + String.valueOf(data.hero().getEnergy_point()), x,	y + size*3);
	  graphics.drawString("MANA : " + String.valueOf(data.hero().getMana_point()), x,	y + size*4);
	  graphics.drawString("EXP : " + String.valueOf(data.hero().getXp()) + "/" + String.valueOf(10 + ((data.hero().getLevel() - 1) * 2)), x,	y + size*5);
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
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("data/Roland.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		drawElement(graphics, img, data.screenInfo().width() * 0.20, data.screenInfo().height() * 0.50, size_x, size_y, Direction.UP);
		drawHeroStats(graphics, data, (int) (data.screenInfo().width() * 0.20 + size_x/2),  (int) (data.screenInfo().height() * 0.50 + size_y));
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
	  graphics.drawString("NEXT ATK : " + String.valueOf(enemy.getAction()), x,	y + size * 3);
  }
  
  /**
   * Draws the enemy in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   * @param enemy 	Data of the enemy.
   */
  private static void drawEnemy(Graphics2D graphics, GameData data, Enemy enemy) {
  	double size_x = data.hero().getSizeX() * enemy.getSize();
  	double size_y = data.hero().getSizeY() * enemy.getSize();
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(enemy.getUrl()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		drawElement(graphics, img, data.screenInfo().width() * 0.80 - size_x, data.screenInfo().height() * 0.5 + (data.hero().getSizeY() - size_y), size_x, size_y, Direction.UP);
		drawEnemyStats(graphics, enemy, (int) (data.screenInfo().width() * 0.75 - size_x), (int) (data.screenInfo().height() * 0.5 + data.hero().getSizeY()));
  	
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
    graphics.fill(new Rectangle2D.Double(data.screenInfo().width() - data.bag().grid_size() / 2, data.screenInfo().height()/3.5 - 2.5*data.bag().grid_size(), 
    																		 data.bag().grid_size() / 2,data.bag().grid_size() / 2));
  }
  
  /**
   * Draw the map in the screen
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private static void drawMap(Graphics2D graphics, GameData data) {
  	var size = data.bag().grid_size();  	
  	var gap = size * 0.1;
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 11; j++) {
	    	final int fi = i;
	    	final int fj = j;
		  	var coordXY = new XY(fj, fi);
		  	if (data.map().getHeroVisible().contains(coordXY)) {
			  	switch(data.map().getGrid()[fi][fj].letterRoom()) {
			  		case 'S' -> graphics.setColor(Color.YELLOW);
			  		case 'O' -> graphics.setColor(Color.RED);
			  		case 'T' -> graphics.setColor(Color.PINK);
			  		case 'H' -> graphics.setColor(Color.GREEN);
			  		case '$' -> graphics.setColor(Color.BLUE);
			  		case 'E' -> graphics.setColor(Color.CYAN);
			  		default ->  graphics.setColor(Color.GRAY);
			  	}
		  	} else graphics.setColor(Color.DARK_GRAY);
		    graphics.fill(new Rectangle2D.Double((gap * fj) + (data.screenInfo().width() / 2) - 5.5 * size + (size * fj), 
		    																		 (gap * fi) + (data.screenInfo().height()/ 5.5) - 2.5* size + (size * fi), 
		    																			size, size));
		    graphics.setColor(Color.BLACK);
		    graphics.draw(new Rectangle2D.Double((gap * fj) + (data.screenInfo().width() / 2) - 5.5 * size + (size * fj), 
		    																		 (gap * fi) + (data.screenInfo().height()/ 5.5) - 2.5* size + (size * fi), 
																							size, size));
		    
		    if (data.map().getHeroAccessible().contains(coordXY)) {
		    	graphics.setColor(Color.MAGENTA);
		      graphics.fill(new Rectangle2D.Double(((gap * fj) + data.screenInfo().width() / 2) - 5.5 * size + (size * fj) + size/4, 
		  																		  	 ((gap * fi) + data.screenInfo().height()/ 5.5) - 2.5* size + (size * fi) + size/4, 
		  																					size/2, size/2));
		      graphics.draw(new Rectangle2D.Double(((gap * fj) + data.screenInfo().width() / 2) - 5.5 * size + (size * fj) + size/4, 
					  	 ((gap * fi) + data.screenInfo().height()/ 5.5) - 2.5* size + (size * fi) + size/4, 
								size/2, size/2));
		    }
      }
		}
		
		// A RETIRER QUAND ON AURA FINIT DE CRER LES MAPS
  	graphics.setColor(Color.ORANGE);
  	graphics.setStroke(new BasicStroke(5));
  	for (var coord : data.map().getHeroVisibleLine()) {
  		for (var coord_acc : data.map().getGrid()[coord.y()][coord.x()].get_accessible()) {
  			graphics.drawLine((int) ((gap * coord.x()) + (data.screenInfo().width() / 2) - 5.5 * size + (size * coord.x() + size/2)), 
													(int) ((gap * coord.y()) + (data.screenInfo().height()/ 5.5) - 2.5* size + (size * coord.y() + size/2)), 
													(int) ((gap * coord_acc.x()) + (data.screenInfo().width() / 2) - 5.5 * size + (size * coord_acc.x()) + size/2), 
													(int) ((gap * coord_acc.y()) + (data.screenInfo().height()/ 5.5) - 2.5* size + (size * coord_acc.y()) + size/2));
  		}
  	}
		
		graphics.setColor(Color.WHITE);
		var coord = data.map().get_heroPos();
    graphics.fill(new Rectangle2D.Double(((gap * coord.x()) + data.screenInfo().width() / 2) - 5.5 * size + (size * coord.x()) + size/4, 
																		  	 ((gap * coord.y()) + data.screenInfo().height()/ 5.5) - 2.5* size + (size * coord.y()) + size/4, 
																					size/2, size/2));
		///////////////////////////////////
  }
  
	/**
	 * Draw the background of the game
	 * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
	 */
	private static void drawBG(Graphics2D graphics, GameData data) {
    // Put a background, FAIRE LA FONCTIION POUR INSERER UNE IMG
		BufferedImage img = data.img_map().get("BG1");
		var width = img.getWidth();
		var height = img.getHeight();
		double scale = Math.max(width / data.screenInfo().width(), height / data.screenInfo().height());
		var transform = new AffineTransform(scale, 0, 0, scale, (data.screenInfo().width() - scale * width) / 2, (data.screenInfo().height() - scale * height) / 2);
		graphics.drawImage(img, transform, null);
	}
	
	/**
	 * Draw an image in the window
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
	  transform.translate(-width / 2.0, -height / 2.0);
	  graphics.drawImage(img, transform, null);
	}
	
	public static void drawItem(Graphics2D graphics, GameData data, Item item, BoundingBox box) {
		switch (item) {
			case Sword c -> new SwordView(graphics, data, item.direction(), box.northWest().x(), box.northWest().y()).draw();
			default -> throw new IllegalArgumentException("Unexpected value: " + item);
		}
	}
	
  /**
   * Update the position of the weapon if we move an item
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data     GameData containing the game data.
   */	
  public static void updateWeaponDraw(Graphics2D graphics, GameData data) {
		Objects.requireNonNull(graphics);
		Objects.requireNonNull(data);
		data.map_item().forEach((item, box) -> drawItem(graphics, data, item, box));
		//drawWeaponGrid(graphics, data);
  }
  
  /**
   * Update the state of the combat.
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   * @param lst_enemy List of all enemy we fight
   */
  public static void update_combat(Graphics2D graphics, GameData data,  ArrayList<Enemy> lst_enemy) {
  	Objects.requireNonNull(graphics);
  	Objects.requireNonNull(data);
  	Objects.requireNonNull(lst_enemy);
  	lst_enemy.forEach(enemy -> drawEnemy(graphics, data, enemy));
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
				if(!data.map_item().isEmpty()) {
					GameView.updateWeaponDraw(graphics, data);
				}
			} else {
				drawMap(graphics, data);
			}
			drawHero(graphics, data);
			drawButton(graphics, data);
			// Draw enemy if we're in combat
			if (GameDataCombat.combat()) {
				update_combat(graphics, data, GameDataCombat.lst_enemy());
			}
	  });
  }
}
