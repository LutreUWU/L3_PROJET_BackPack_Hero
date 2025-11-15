package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import game.weaponView.SwordView;
import monster.Enemy;
import monster.Rat;

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
   * Draw the grid from the Backpack
   * @param context Which window to draw
   * @param data	  Data of the game
   */	
  private static void drawGrid(ApplicationContext context, GameData data) {
    var screenInfo = context.getScreenInfo();
		int [][] grid = data.bag().grid();
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
	    	final int fi = i;
	    	final int fj = j;
			  context.renderFrame(graphics -> {
			  	if (grid[fi][fj] >= -1) {
			  		graphics.setColor(Color.GRAY);
			  	}
			  	if (grid[fi][fj] == -2) {
			  		graphics.setColor(Color.RED);
			  	}
			  	
			    graphics.fill(new Rectangle2D.Double((screenInfo.width() / 2) - 3.5 * data.grid_size() + (data.grid_size() * fj), 
										        							  	 (screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
										        							  	 data.grid_size(), data.grid_size()));
			    graphics.setColor(Color.BLACK);
			    graphics.draw(new Rectangle2D.Double((screenInfo.width()/2) - 3.5*data.grid_size() + (data.grid_size() * fj), 
											        								 (screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
											        								 data.grid_size(), data.grid_size()));
			    
			  });
      }
		}
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
	  for (var block : item.shape()) {
		switch (item.id()) {
		  case 1 ->{
				var drawSword = new SwordView(context, data, block.y(), block.x());
				drawSword.draw();
		  }
		  default ->{}
		}
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
	for (var block : item.shape()) {
	  switch (item.id()) {
		case 1 ->{
		  var drawSword = new SwordView(context, data, block.y(), block.x());
		  drawSword.draw();
		}
		  default ->{}
	  }
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
		graphics.setFont(font);
	  graphics.drawString("PV : " + data.hero().getHP() + "/" + data.hero().getMax_HP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(data.hero().getCurrent_protection()), x,	y + size*2);
	  graphics.drawString("AP : " + String.valueOf(data.hero().getEnergy_point()), x,	y + size*3);
	  graphics.drawString("MANA : " + String.valueOf(data.hero().getMana_point()), x,	y + size*4);
	  graphics.drawString("EXP : " + String.valueOf(data.hero().getXp()), x,	y + size*5);
  }
  
  /**
   * Draws all the information about the hero
   * 
   * @param graphics {@code Graphics2D} object for drawing.
   * @param data 		 GameData containing the game data. 
   * @param x				 coordinate x where we wants to draw.
   * @param y				 coordinate y where we wants to draw.
   */
  private static void drawEnemyStats(Graphics2D graphics, Enemy enemy, int x, int y) {
  	int size = 14;
    Font font = new Font("Arial", Font.PLAIN, size);
		graphics.setFont(font);
	  graphics.drawString("PV : " + enemy.getHP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(enemy.getShield()), x,	y + size*2);
	  graphics.drawString("NEXT ATK : " + String.valueOf(enemy.pre_action()), x,	y + size * 3);
  }
  
  /**
   * Draws the hero in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   */
  private static void drawHero(ApplicationContext context, GameData data) {
  	var screenInfo = context.getScreenInfo();
  	double size_x = screenInfo.width() * 0.05;
  	double size_y = screenInfo.height() * 0.2;
  	context.renderFrame(graphics -> {
  		graphics.setColor(Color.BLUE);
  		graphics.fill(new Rectangle2D.Double(screenInfo.width() * 0.25, screenInfo.height() * 0.65, 
		        															 size_x, size_y));
  		graphics.setColor(Color.BLACK);
  		Font font = new Font("Arial", Font.PLAIN, 20);
  		graphics.setFont(font);
  	  graphics.drawString("HERO", (int) (screenInfo.width() * 0.25), (int) (screenInfo.height() * 0.64));
  		drawHeroStats(graphics, data, (int) (screenInfo.width() * 0.25), (int) ((screenInfo.height() * 0.65) + size_y));
  	});
  	
  }
  
  /**
   * Draws the enemy in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   */
  private static void drawEnemy(ApplicationContext context, GameData data, Enemy enemy) {
  	var screenInfo = context.getScreenInfo();
  	double size_x = screenInfo.width() * 0.05;
  	double size_y = screenInfo.height() * 0.2;
  	context.renderFrame(graphics -> {
  		graphics.setColor(Color.RED);
  		graphics.fill(new Rectangle2D.Double(screenInfo.width() * 0.75 - size_x, screenInfo.height() * 0.65, 
		        															 size_x, size_y));
  		graphics.setColor(Color.BLACK);
  		Font font = new Font("Arial", Font.PLAIN, 20);
  		graphics.setFont(font);
  	  graphics.drawString(enemy.toString(), (int) (screenInfo.width() * 0.75 - size_x), (int) (screenInfo.height() * 0.64));
  		drawEnemyStats(graphics, enemy, (int) (screenInfo.width() * 0.75 - size_x), (int) ((screenInfo.height() * 0.65) + size_y));
  	});
  	
  }
	
  /**
   * Draws the game board from its data.
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data.
   */	
  public static void draw(ApplicationContext context, GameData data, GameView view) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		Objects.requireNonNull(view);
		context.renderFrame(graphics -> {
	    // Refresh the screen with a white screen
	    ScreenInfo screenInfo = context.getScreenInfo();
	    graphics.setColor(Color.WHITE);
	    graphics.fillRect(0, 0, screenInfo.width(), screenInfo.height());
	});
		drawHero(context, data);
		var Rat = new Rat();
		drawEnemy(context, data, Rat);
		drawGrid(context, data);
		drawItemBag(context, data);
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
}
