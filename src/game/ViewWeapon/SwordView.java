package game.ViewWeapon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.github.forax.zen.ApplicationContext;

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
public record SwordView(ApplicationContext context, GameData data, Direction direction, int x, int y) {/**
	 * Draw a Sword using library.
	 * 
	 */
	public void draw(){
		int size = data.bag().grid_size();
		var screenInfo = context.getScreenInfo();
		context.renderFrame(graphics -> {
			BufferedImage img = null;
			try {
				img = ImageIO.read(new File("data/weapon/sword.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			GameView.drawElement(graphics, img, screenInfo.width() / 2 - 3.5 * size + (size * x),
																				screenInfo.height()/4.5 - 2.5*size + (size * (y - 1)), 
																				size, size * 3, direction);
		});
	}
}
