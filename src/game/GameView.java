package game;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;

import game.weaponView.SwordView;

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
    	if (grid[i][j] >= -1) {
		  context.renderFrame(graphics -> {
		    graphics.setColor(Color.GRAY);
			graphics.fill(new Rectangle2D.Double((screenInfo.width() / 2) - 3.5 * data.grid_size() + (data.grid_size() * fj), 
		        							  	 (screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
		        								 data.grid_size(), data.grid_size()));
			graphics.setColor(Color.BLACK);
		    graphics.draw(new Rectangle2D.Double((screenInfo.width()/2) - 3.5*data.grid_size() + (data.grid_size() * fj), 
		        								 (screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
		        								 data.grid_size(), data.grid_size()));
		  });
		}
		if (grid[i][j] == -2) {
		  context.renderFrame(graphics -> {
		    graphics.setColor(Color.RED);
		    graphics.fill(new Rectangle2D.Double((screenInfo.width()/2) - 3.5*data.grid_size() + (data.grid_size()* fj), 
		        									(screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size()* fi), 
		        									data.grid_size(), data.grid_size()));
		    graphics.setColor(Color.BLACK);
		    graphics.draw(new Rectangle2D.Double((screenInfo.width()/2) - 3.5*data.grid_size() + (data.grid_size() * fj), 
		        									(screenInfo.height()/3.5) - 2.5*data.grid_size() + (data.grid_size() * fi), 
		        									data.grid_size(), data.grid_size()));
		  });
		}
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
   * Draws the hero in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   */
  private static void drawHero(ApplicationContext context, GameData data) {
	//TO DO 
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
	drawHero(context, data);
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
