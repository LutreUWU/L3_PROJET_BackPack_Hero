package view;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;
import model.Backpack;


public class Interface { // #
	private final int GRID_SIZE = 100;
		
	public void drawRectangle(ApplicationContext context, int x, int y, int size_x, int size_y, Color color) {
	/** 
	   * Draw a rectangle at the coordinate (x, y)
	   * @param context which window to draw
	   * @param x 		the X coordinate of the upper-left corner
       * @param y 		the Y coordinate of the upper-left corner
       * @param size_x  the width of the newly constructed
       * @param size_y  the height of the newly constructed
       * @param color 	the color of the rectangle
	 */
		context.renderFrame(graphics -> {
	        graphics.setColor(color);
	        graphics.fill(new Rectangle2D.Float(x, y, size_x, size_y));
	    });		
	} 
	
	public void drawGrid(ApplicationContext context, int x, int y, ScreenInfo screenInfo, Backpack bag) {
	/** 
	   * draw the grid from the Backpack
	   * @param context 	which window to draw
	   * @param x 			the X coordinate of the upper-left corner
       * @param y 			the Y coordinate of the upper-left corner
       * @param screenInfo  the width and height of the window's screen (for centering the grid)
       * @param bag 		the bag where the grid is inside
	 */	
		int [][] grid = bag.grid();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 7; j++) {
				if (grid[i][j] >= -1) {
					final int fi = i;
		            final int fj = j;
					context.renderFrame(graphics -> {
						graphics.setColor(Color.BLACK);
				        graphics.draw(new Rectangle2D.Double((screenInfo.width()/2) - 3.5*GRID_SIZE + (GRID_SIZE * fj), 
				        									(screenInfo.height()/2) - 2.5*GRID_SIZE + (GRID_SIZE * fi), 
				        									GRID_SIZE, GRID_SIZE));
					});
				}
			}
		}
	}
	
} // #
