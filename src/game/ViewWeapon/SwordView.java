package game.ViewWeapon;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import game.GameData;
import game.GameView;
import model.Direction;

/**
 * The SwordView class deals with the display of the screen.
 * Since each item has different shape, each item has their own method of creation.
 * @param context {@code ApplicationContext} of the game.
 * @param data    GameData containing the game data.
 * @param x		  Position x of the center of the item.
 * @param y		  Position y of the center of the item.
 *
 */
public record SwordView(Graphics2D graphics, GameData data, Direction direction, int x, int y) {
	public void draw(){
		double centerX, centerY;
		double newX = x, newY = y;
		int size = data.bag().grid_size();
		double dimX = size, dimY = size*3;
	  if (direction == Direction.LEFT || direction == Direction.RIGHT) {
	  	centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			newX = centerX - dimX / 2.0;
			newY = centerY - dimY / 2.0;
		}
		
		GameView.drawElement(graphics, data.img_map().get("sword"), newX, newY, dimX, dimY, direction);
	}
	
	public void drawInBag(){
		int size = data.bag().grid_size();
		GameView.drawElement(graphics, data.img_map().get("sword"), 
																	 data.screenInfo().width() / 2 - 3.5 * size + (size * x),
																	 data.screenInfo().height()/4.5 - 2.5 * size + (size * (y - 1)), 
																	 size, size * 3, direction);
	}
}
