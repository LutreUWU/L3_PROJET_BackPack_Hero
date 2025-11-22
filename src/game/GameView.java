package game;

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
import com.github.forax.zen.ScreenInfo;

import game.ViewWeapon.SwordView;
import game.data.GameDataCombat;
import model.Block;
import model.Direction;
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
   * Draw the grid of the Backpack
   * @param context Which window to draw
   * @param data	  Data of the game
   */	
  private static void drawGrid(ApplicationContext context, GameData data) {
    var screenInfo = context.getScreenInfo();
    int size = data.bag().grid_size();
		int [][] grid = data.bag().grid();
		context.renderFrame(graphics -> {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("data/bag.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			drawElement(graphics, img, screenInfo.width() * 0.5 - size * 4.5, 10, size * 9, size * 6, Direction.UP);
			for (int i = 0; i < 5; i++) {
	      for (int j = 0; j < 7; j++) {
		    	final int fi = i;
		    	final int fj = j;				  
		    		// TO DO (David) : instead of rectangle, change image of the grid
				  	if (grid[fi][fj] >= -1) {
				  		graphics.setColor(Color.GRAY);
				  		graphics.fill(new Rectangle2D.Double((screenInfo.width() / 2) - 3.5 * size + (size * fj), 
																					    	   (screenInfo.height()/4.5) - 2.5*size + (size * fi), 
																										size, size));
				  		graphics.setColor(Color.BLACK);
				  	}
				  	if (grid[fi][fj] == -2) {
				  		graphics.setColor(Color.RED);
				  	}
				    graphics.draw(new Rectangle2D.Double((screenInfo.width() / 2) - 3.5 * size + (size * fj), 
																				    	   (screenInfo.height()/4.5) - 2.5*size + (size * fi), 
																									size, size));
				}
	    }
		});
  }
  
  /**
   * Draw all items inside the bag.
   * 
   * @param context Which window to draw
   * @param data	  Data of the game
   */
  private static void drawItemBag(ApplicationContext context, GameData data) {
		var item_list = data.bag().item_lst();
		for (var item : item_list) {
			Block coordinate = item.shape()[0];
		  switch (item.id()) {
				case 1 -> new SwordView(context, data, item.direction(), coordinate.x(), coordinate.y()).draw();
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
  private static void drawWeaponGrid(ApplicationContext context, GameData data) {
		var item = data.weapon();
		if (item == null) {
	        return;
	  }
		Block coordinate = item.shape()[0];
	  switch (item.id()) {
			case 1 -> new SwordView(context, data, item.direction(), coordinate.x(), coordinate.y()).draw();
		  default ->{}
	  }
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
  private static void drawHero(ApplicationContext context, GameData data) {
  	var screenInfo = context.getScreenInfo();
  	double size_x = data.hero().getSizeX();
  	double size_y = data.hero().getSizeY();
  	context.renderFrame(graphics -> {
  		BufferedImage img = null;
			try {
				img = ImageIO.read(new File("data/Roland.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			drawElement(graphics, img, screenInfo.width() * 0.20, screenInfo.height() * 0.50, size_x, size_y, Direction.UP);
			drawHeroStats(graphics, data, (int) (screenInfo.width() * 0.20 + size_x/2),  (int) (screenInfo.height() * 0.50 + size_y));
  	});	
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
  private static void drawEnemy(ApplicationContext context, GameData data, Enemy enemy) {
  	var screenInfo = context.getScreenInfo();
  	double size_x = data.hero().getSizeX() * enemy.getSize();
  	double size_y = data.hero().getSizeY() * enemy.getSize();
  	context.renderFrame(graphics -> {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File(enemy.getUrl()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			drawElement(graphics, img, screenInfo.width() * 0.80 - size_x, screenInfo.height() * 0.5 + (data.hero().getSizeY() - size_y), size_x, size_y, Direction.UP);
  		drawEnemyStats(graphics, enemy, (int) (screenInfo.width() * 0.75 - size_x), (int) (screenInfo.height() * 0.5 + data.hero().getSizeY()));
  	});
  	
  }
	
  /**
   * Draw the button for switching between map and bag
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private static void drawButton(ApplicationContext context, GameData data) {
  	ScreenInfo screenInfo = context.getScreenInfo();
  	context.renderFrame(graphics -> {
  		graphics.setColor(Color.RED);  		
  		graphics.setColor(data.mapOrBag() ? Color.ORANGE : Color.CYAN);
	    graphics.fill(new Rectangle2D.Double(screenInfo.width() - data.bag().grid_size() / 2, screenInfo.height()/3.5 - 2.5*data.bag().grid_size(), 
	    																		 data.bag().grid_size() / 2,data.bag().grid_size() / 2));
  	});
  }
  
  /**
   * Draw the map in the screen
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private static void drawMap(ApplicationContext context, GameData data) {
  	ScreenInfo screenInfo = context.getScreenInfo();
  	int size = data.map().grid_size();  	
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 11; j++) {
	    	final int fi = i;
	    	final int fj = j;
			  context.renderFrame(graphics -> {
			  	graphics.setColor(Color.GRAY);
			    graphics.fill(new Rectangle2D.Double((screenInfo.width() / 2) - 5.5 * size + (size * fj), 
										        							  	 (screenInfo.height()/ 5.5) - 2.5* size + (size * fi), 
			    																			size, size));
			    graphics.setColor(Color.BLACK);
			    graphics.draw(new Rectangle2D.Double((screenInfo.width() / 2) - 5.5 * size + (size * fj), 
																					  	 (screenInfo.height()/ 5.5) - 2.5* size + (size * fi), 
																								size, size));
			  });
      }
		}
  }
  
	/**
	 * Draw the background of the game
	 * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
	 */
	private static void drawBG(ApplicationContext context, GameData data) {
		context.renderFrame(graphics -> {
	    // Put a background, FAIRE LA FONCTIION POUR INSERER UNE IMG
	    ScreenInfo screenInfo = context.getScreenInfo();
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("data/BG/BG1.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			var width = img.getWidth();
			var height = img.getHeight();
			double scale = Math.max(width / screenInfo.width(), height / screenInfo.height());
			var transform = new AffineTransform(scale, 0, 0, scale, (screenInfo.width() - scale * width) / 2, (screenInfo.height() - scale * height) / 2);
			graphics.drawImage(img, transform, null);
		});	
	}
	
	/**
	 * Draw an image in the window
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x t				The x coordinate of the location in user space where
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

	
  /**
   * Update the position of the weapon if we move an item
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data     GameData containing the game data.
   */	
  public static void updateWeaponDraw(ApplicationContext context, GameData data) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		drawWeaponGrid(context, data);
  }
  
  /**
   * Update the state of the combat.
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   * @param lst_enemy List of all enemy we fight
   */
  public static void update_combat(ApplicationContext context, GameData data,  ArrayList<Enemy> lst_enemy) {
  	Objects.requireNonNull(context);
  	Objects.requireNonNull(data);
  	Objects.requireNonNull(lst_enemy);
  	lst_enemy.forEach(enemy -> drawEnemy(context, data, enemy));
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
		drawBG(context, data);
		if (data.mapOrBag()) {
			drawGrid(context, data);
			drawItemBag(context, data);
		} else {
			drawMap(context, data);
		}
		
		drawHero(context, data);
		drawButton(context, data);
		// Draw enemy if we're in combat
		if (GameDataCombat.combat()) {
			GameDataCombat.refreshCombatDraw(context, data);;
		}
  }
}
