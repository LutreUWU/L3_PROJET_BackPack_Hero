package game;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import game.data.RenderData;
import model.BoundingBox;
import model.XY;

/**
 * The GameDataMath class is where all coordinate, size ... depending of the screen are stored.
 */
public class GameMath {
	private static int screenWidth;
	private static int screenHeight;
	private static GameData data;
	
	// For EVENT
	private static LinkedHashMap<String, RenderData> mapEvent = new LinkedHashMap<>();
	
	public GameMath(GameData data_) {
		data = data_;
		screenWidth = data.screenInfo().width();
		screenHeight = data.screenInfo().height();
		getEventValue();
	}
	
	/**
	 * This methods will calculate all value necessary for drawing an event in the interface
	 * 
	 */
	private static void getEventValue() {
		getEventBackgroundValue();
		getEventChoiceValue();
	}
	
	private static void getEventBackgroundValue() {
		BufferedImage img = data.img_map().get("BG_EVENT");
    int imgW = img.getWidth();
    int imgH = img.getHeight();
    double scaleY = (double) (0.40 * screenHeight) / imgH;
    double imgHeightScale = imgH * scaleY;
    double drawY = screenHeight - imgHeightScale;
    AffineTransform transform = new AffineTransform();
    transform.translate(screenWidth/2 - (imgW/2.0), drawY);
    transform.scale(1, scaleY);
    var boundingBox = new BoundingBox(new XY(screenWidth / 2 - imgW/2, (int) drawY), new XY(screenWidth / 2 + imgW/2, screenHeight));
    mapEvent.put("BG_EVENT", new RenderData(transform, boundingBox));
	}
	
	private static void getEventChoiceValue() {
		// Choice 1
		var img = data.img_map().get("BG_CHOICE1");
		int width = img.getWidth(), height = img.getHeight();;
		var scale = (screenWidth * 0.20) / width;
	  AffineTransform transform1 = new AffineTransform();
	  double posX = screenWidth * (0.5 - 0.23);
	  double posY = mapEvent.values().stream().findFirst().orElse(null).box().northWest().y() * 1.15;
	  transform1.translate(posX, posY);
	  transform1.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  mapEvent.put("BG_CHOICE1", new RenderData(transform1, boundingBox));
	  // Choice 2
	  img = data.img_map().get("BG_CHOICE2");
	  AffineTransform transform2 = new AffineTransform();
	  posX = screenWidth * (0.5 + 0.23) - (width * scale);
	  transform2.translate(posX, posY);
	  transform2.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  mapEvent.put("BG_CHOICE2", new RenderData(transform2, boundingBox));
	}
	
	// Getter Event
	public static LinkedHashMap<String, RenderData> getMapEvent() {
		return mapEvent;
	}
	
}
