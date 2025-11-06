package game.weaponView;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import com.github.forax.zen.ApplicationContext;

import game.GameData;

/**
 * The SwordView class deals with the display of the screen.
 * Since each item has different shape, each item has their own method of creation.
 * @param context {@code ApplicationContext} of the game.
 * @param data    GameData containing the game data.
 * @param x		  Position x of the center of the item.
 * @param y		  Position y of the center of the item.
 *
 */
public record SwordView(ApplicationContext context, GameData data, int x, int y) {
	/**
	 * Draw a Sword using library.
	 * 
	 */
	public void draw(){
		int grid_size = data.grid_size();
		var screenInfo = context.getScreenInfo();
		context.renderFrame(graphics -> {
			graphics.setColor(Color.RED);
	        graphics.draw(new Rectangle2D.Double((grid_size/4 + screenInfo.width()/2) - 3.5*grid_size + (grid_size * y), 
	        									( grid_size/4 + screenInfo.height()/3.5) - 2.5*grid_size + (grid_size * x), 
	        									  grid_size/2, grid_size/2));
		});
	}
}
