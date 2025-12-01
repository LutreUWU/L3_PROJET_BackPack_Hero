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
	
	
	private static LinkedHashMap<String, RenderData> renderDataGame = new LinkedHashMap<>();
	
	public GameMath(GameData data_) {
		data = data_;
		screenWidth = data.screenInfo().width();
		screenHeight = data.screenInfo().height();
		getBGValue();
		getBackpackValue();
		getEventValue();
	}
	
// ================== BG ======================
	private static void getBGValue() {
		for (int i = 1; i < 7; i ++) {
			getBGimgValue("BG" + i);
		}
	}
	
	private static void getBGimgValue(String BG_name) {
		BufferedImage img = data.img_map().get(BG_name);
		var width = img.getWidth();
		var height = img.getHeight();
		double scale = width / screenWidth;
    double scaledX = width * scale;
    double scaledY = height * scale;
    double offsetX = (screenWidth  - scaledX) / 2.0;
    double offsetY = (screenHeight - scaledY) / 2.0;
    var transform = new AffineTransform();
		transform.translate(offsetX, offsetY);
    transform.scale(scale, scale);
		XY NW = new XY(0, 0);
		XY SE = new XY(screenWidth, screenHeight);
		renderDataGame.put(BG_name, new RenderData(transform, new BoundingBox(NW, SE)));
	}
// ============================================	

	
// =============== Backpack ===================
	private static void getBackpackValue() {
		var size = data.bag().grid_size();
		var dimX = size * 8.0;
		var dimY = size * 6.0;
		XY NW = new XY((int) (screenWidth / 2 - dimX/2 + size/2), (int) (size / 2  + screenHeight * 0.02));
		XY SE = new XY((int) (screenWidth / 2 + dimX/2), (int) (size / 2  + screenHeight * 0.02 + dimY));
		BufferedImage img = data.img_map().get("bag");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(NW.x() - size/2, NW.y() - size / 2);
	  transform.scale(scaleX, scaleY);
		renderDataGame.put("BG_BACKPACK", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
// ============================================
	
// ================== Map =====================
	
// ============================================
	
// ================= Hero =====================
	
// ============================================
	

	
// ================= EVENT ====================
	/**
	 * This methods will calculate all value necessary for drawing an event in the interface
	 */
	private static void getEventValue() {
		getEventBackgroundValue();
		getEventChoiceValue();
		getEventChoiceEndValue();
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
    renderDataGame.put("BG_EVENT", new RenderData(transform, boundingBox));
	}
	
	private static void getEventChoiceValue() {
		// Choice 1
		var img = data.img_map().get("BG_CHOICE1");
		int width = img.getWidth(), height = img.getHeight();;
		var scale = (screenWidth * 0.20) / width;
	  AffineTransform transform1 = new AffineTransform();
	  double posX = screenWidth * (0.5 - 0.23);
	  double posY = renderDataGame.get("BG_EVENT").box().northWest().y() * 1.15;
	  transform1.translate(posX, posY);
	  transform1.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_CHOICE1", new RenderData(transform1, boundingBox));
	  // Choice 2
	  img = data.img_map().get("BG_CHOICE2");
	  AffineTransform transform2 = new AffineTransform();
	  posX = screenWidth * (0.5 + 0.23) - (width * scale);
	  transform2.translate(posX, posY);
	  transform2.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_CHOICE2", new RenderData(transform2, boundingBox));
	}
	
	private static void getEventChoiceEndValue() {
		var img = data.img_map().get("BG_CHOICE_END");
		int width = img.getWidth(), height = img.getHeight();;
		var scale = (screenWidth * 0.20) / width;
	  AffineTransform transform = new AffineTransform();
	  double posX = screenWidth * 0.5 - (width * scale) / 2;
	  double posY = renderDataGame.get("BG_EVENT").box().northWest().y() * 1.15;
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_CHOICE_END", new RenderData(transform, boundingBox));
	}
	
	// Getter Event
	public static LinkedHashMap<String, RenderData> getMapEvent() {
		return renderDataGame;
	}
// ===========================================
	
}
