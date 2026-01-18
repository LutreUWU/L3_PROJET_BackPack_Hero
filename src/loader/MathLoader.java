package loader;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import com.github.forax.zen.ScreenInfo;

import game.GameData;
import model.BoundingBox;
import model.XY;

/**
 * The MathLoader class calculates and stores all the screen-dependent coordinates,
 * sizes, and transformations needed to render game elements.
 * 
 * All coordinates and scaling factors are based on the screen dimensions and
 * assets loaded in ImageLoader. It provides static access to calculated
 * positions via the renderDataGame map.
 */
public class MathLoader {
	// Screen dimensions
	private static int screenWidth;
	private static int screenHeight;
  // Reference to game data and image loader
	private static GameData data;
	private static ImageLoader imgLoader;
  // Map containing pre-calculated render information for all game elements
	private static LinkedHashMap<String, RenderData> renderDataGame = new LinkedHashMap<>();
	
	/**
   * Initializes the MathLoader, calculates positions, scales, and bounding boxes
   * for all UI elements based on the screen dimensions.
   *
   * @param dataGame 		The current game data
   * @param imageLoader The image loader with all game assets
   * @param screenInfo  The screen information (resolution)
   */
	public MathLoader(GameData dataGame, ImageLoader imageLoader, ScreenInfo screenInfo) {
		data = dataGame;
		imgLoader = imageLoader;
		screenWidth = screenInfo.width();
		screenHeight = screenInfo.height();
    // Initialize positions and scales for all game UI elements
		getBGValue();
		getBGLobbyValue();
		getHeroValue();
		getIconHeroValue();
		getBackpackValue();
		getInfoItemValue();
		getMapValue();
		getEventValue();	
		getEndTurnValue();
		getBinValue();
		getShopValue();
		getFFButtonValue();
	}

  // ========================= LOBBY / BACKGROUND =========================
	
	 /**
   * Computes positions and sizes for all lobby elements, including background,
   * start button, HOF button, leave button, and Hall of Fame panel.
   */
	private static void getBGLobbyValue() {
		getBGImgLobbyValue();
		getStartButtonLobbyValue();
		getHOFButtonLobbyValue();
		getLeaveButtonLobbyValue();
		getHOFLobbyValue();
	}

	/**
   * Calculates the position, size, and bounding box for the "START_GAME" button in the lobby.
   */
	private static void getStartButtonLobbyValue() {
		int height = FontLoader.getH1();
		int width = FontLoader.getH1() * 10;
		XY NW = new XY((int) (screenWidth * 0.80), (int) (screenHeight * 0.25));
		XY SE = new XY(NW.x() + width, NW.y() + height);
		renderDataGame.put("START_GAME", new RenderData(null, new BoundingBox(NW, SE)));	
	}
	
	/**
   * Calculates the position, size, and bounding box for the "HOF_BUTTON" in the lobby.
   */
	private static void getHOFButtonLobbyValue() {
		int height = FontLoader.getH1();
		int width = FontLoader.getH1() * 10;
		XY NW = new XY((int) (screenWidth * 0.80), (int) (screenHeight * 0.25 + height) );
		XY SE = new XY(NW.x() + width, NW.y() + height);
		renderDataGame.put("HOF_BUTTON", new RenderData(null, new BoundingBox(NW, SE)));	
	}

	/**
   * Calculates the position, size, and bounding box for the "LEAVE_BUTTON" in the lobby.
   */
	private static void getLeaveButtonLobbyValue() {
		int height = FontLoader.getH1();
		int width = FontLoader.getH1() * 10;
		XY NW = new XY((int) (screenWidth * 0.80), (int) (screenHeight * 0.25 + height * 2) );
		XY SE = new XY(NW.x() + width, NW.y() + height);
		renderDataGame.put("LEAVE_BUTTON", new RenderData(null, new BoundingBox(NW, SE)));	
	}
	
	/**
   * Calculates the position, size, and bounding box of the Hall of Fame panel (HOF) in the lobby.
   */
	private static void getHOFLobbyValue() {
		int height = (int) (FontLoader.getH2() * 10);
		int width = (int) (screenWidth * 0.15);
		XY NW = new XY((int) (screenWidth * 0.01), (int) (screenHeight * 0.99 - height) );
		XY SE = new XY(NW.x() + width, NW.y() + height);
		BufferedImage img = imgLoader.bgImages().get("BG_SCORE");
		double scaleW = (double) (width) / img.getWidth();
		double scaleH = (double) (height) / img.getHeight();
    var transform = new AffineTransform();
    transform.translate(NW.x(), NW.y());
    transform.scale(scaleW, scaleH);
		renderDataGame.put("HOF", new RenderData(transform, new BoundingBox(NW, SE)));	
	}
	
	 /**
   * Calculates the position and scale of the lobby background image.
   */
	private static void getBGImgLobbyValue() {
		BufferedImage img = imgLoader.bgImages().get("BG_LOBBY");
		double scale =  (double) (screenWidth) / img.getWidth();
    var transform = new AffineTransform();
    transform.scale(scale, scale);
		XY NW = new XY(0, 0);
		XY SE = new XY(screenWidth, screenHeight);
		renderDataGame.put("BG_LOBBY", new RenderData(transform, new BoundingBox(NW, SE)));
	
	}
  // ========================= ICONS =========================
	
	/**
   * Calculates positions, scales, and bounding boxes for all hero icons:
   * Health, Shield, Mana, Action, Unlock, Gold, and Boost.
   */
	private static void getIconHeroValue() {
  	double sizeY =  screenHeight * 0.04;
  	getHealthValue(sizeY);
		getShieldValue(sizeY);
		getManaValue(sizeY);
		getActionValue(sizeY);
		getUnlockValue(sizeY);
		getGoldValue(sizeY);
		getBoostValue(sizeY);
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for health icon.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getHealthValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_HEALTH");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, 0);
    transform.scale(scale, scale);
		var NW = new XY(0, 0);
		var SE = new XY((int) (width * scale), (int) (height * scale));
		renderDataGame.put("ICON_HEALTH", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for shield icon.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getShieldValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_SHIELD");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale));
		var SE = new XY((int) (width * scale), (int) (height * scale * 2));
		renderDataGame.put("ICON_SHIELD", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for mana icons.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getManaValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_MANA");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale * 2);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale * 2));
		var SE = new XY((int) (width * scale), (int) (height * scale * 3));
		renderDataGame.put("ICON_MANA", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for AP icons.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getActionValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_ACTION");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale * 3);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale * 3));
		var SE = new XY((int) (width * scale), (int) (height * scale * 4));
		renderDataGame.put("ICON_ACTION", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for unlock icons.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getUnlockValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_UNLOCK");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale * 4);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale * 4));
		var SE = new XY((int) (width * scale), (int) (height * scale * 5));
		renderDataGame.put("ICON_UNLOCK", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for gold icons.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getGoldValue(double sizeY) {
		var img = imgLoader.bgImages().get("gold1");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale * 5);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale * 5));
		var SE = new XY((int) (width * scale), (int) (height * scale * 6));
		renderDataGame.put("gold", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	/**
	 * Calculates positions, scales, and bounding boxes for boost icons.
	 * 
	 * @param sizeY Size of the icon
	 */
	private static void getBoostValue(double sizeY) {
		var img = imgLoader.bgImages().get("ICON_BOOST");
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = sizeY / height;
		var transform = new AffineTransform();
    transform.translate(0, height * scale * 6);
    transform.scale(scale, scale);
		var NW = new XY(0, (int) (height * scale * 6));
		var SE = new XY((int) (width * scale), (int) (height * scale * 7));
		renderDataGame.put("ICON_BOOST", new RenderData(transform, new BoundingBox(NW, SE)));
	}
		
	// ================== BG ======================
	
	/**
   * Calculates positions and scales for all game background images.
   */
	private static void getBGValue() {
		var allBg = imgLoader.bgImages().keySet().stream().filter(key -> key.contains("BG")).toList();
		for (String bgName : allBg) {
			getBGimgValue(bgName);
		}
	}
	
	/**
   * Calculates the position and scale for a single background image.
   *
   * @param BG_name The name of the background image
   */
	private static void getBGimgValue(String BG_name) {
		BufferedImage img = imgLoader.bgImages().get(BG_name);
		double width = img.getWidth();
		double scale =  (double) (screenWidth) / width;
    var transform = new AffineTransform();
    transform.scale(scale, scale);
		XY NW = new XY(0, 0);
		XY SE = new XY(screenWidth, screenHeight);
		renderDataGame.put(BG_name, new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
	// =============== Backpack ===================
	
	/**
   * Calculates the position, scale, and bounding box for the backpack background.
   */
	private static void getBackpackValue() {
		var size = data.bag().getGridSize();
		int nbCol = data.bag().getCol();
		int nbRow =  data.bag().getRow();
		var dimX = (nbCol + 2.0) * size;
		var dimY = (nbRow + 2.0) * size;
		XY NW = new XY((int) (screenWidth / 2.0 - (nbCol / 2.0) * size), (int) (screenHeight * 0.02 + size * 0.8));
		XY SE = new XY((int) (NW.x() + nbCol * size), (int) (NW.y() + nbRow * size));
		BufferedImage img = imgLoader.bgImages().get("BG_BACKPACK");
		var width = img.getWidth();
		var height = img.getHeight();
		double scaleX = (double) dimX / width;
		double scaleY = (double) dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(screenWidth / 2.0 - dimX / 2.0, screenHeight * 0.02);
	  transform.scale(scaleX, scaleY);
		renderDataGame.put("BG_BACKPACK", new RenderData(transform, new BoundingBox(NW, SE)));
	}
		
	//================== INFO ITEM ================
	
	/**
   * Calculates positions, scales, and bounding boxes for the item info panel
   * displayed next to the backpack.
   */
	private static  void getInfoItemValue() {
		var size = data.bag().getGridSize();
		var dimX = size * 5.0;
		var dimY = size * (data.bag().getRow() + 1.0);
		var NW = new XY(renderDataGame.get("BG_BACKPACK").box().southEast().x() + size, renderDataGame.get("BG_BACKPACK").box().northWest().y());
		var SE = new XY(renderDataGame.get("BG_BACKPACK").box().southEast().x() + size + (int) dimX, renderDataGame.get("BG_BACKPACK").box().southEast().y() );
		BufferedImage img = imgLoader.bgImages().get("BG_INFO_ITEM");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(NW.x(), NW.y());
	  transform.scale(scaleX, scaleY);
	  var realNW = new XY((int) (NW.x() + size * 0.9), NW.y() + (int) (dimY*0.15));
	  var realSE = new XY((int) (SE.x() + size * 0.9), SE.y() + size/2);
		renderDataGame.put("BG_INFO_ITEM", new RenderData(transform, new BoundingBox(realNW, realSE)));
	}

	// ================== Map =====================

	/**
   * Calculates the position, scale, and bounding box of the main map background.
   */
	private static void getMapValue() {
		var size = data.bag().getGridSize();
  	var gap = size * 0.1;
  	int nbCol = data.map().getCol();
  	int nbRow = data.map().getRow();
		var dimX = size * (nbCol + 1) + (nbCol - 1) * gap;
		var dimY = size * (nbRow + 1) + (nbRow - 1) * gap;
		XY NW = new XY((int) ((screenWidth / 2) - (nbCol / 2.0) * size - (nbCol / 2) * gap), (int) ((screenHeight * 0.04 + size / 2.0)));
		XY SE = new XY((int) ((screenWidth / 2) + (nbCol / 2.0) * size + (nbCol / 2) * gap), (int) ((screenHeight * 0.04 + size / 2.0) + nbRow * size + (nbRow - 1) * gap));
		BufferedImage img = imgLoader.bgImages().get("BG_MAP");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(NW.x() - size / 2.0, NW.y() - size / 2.0);
	  transform.scale(scaleX, scaleY);
		renderDataGame.put("BG_MAP", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	// ================= Hero =====================
	
	/**
  * Calculates the position, scale, and bounding box for the hero image on the map.
  */
	private static void getHeroValue() {
		BufferedImage img = imgLoader.bgImages().get("Roland");
    int imgW = img.getWidth();
    int imgH = img.getHeight();
    double scale = 0.10 * screenWidth / imgW;
    AffineTransform transform = new AffineTransform();
    transform.translate(screenWidth * 0.20, screenHeight * 0.50);
    transform.scale(scale, scale);
    XY northWest = new XY((int) (screenWidth *0.20), (int) (screenHeight * 0.5));
    var boundingBox = new BoundingBox(northWest, new XY((int) (northWest.x() + imgW*scale), (int) (northWest.y() + imgH*scale)));
    renderDataGame.put("Roland", new RenderData(transform, boundingBox));
	}
	
	// ================= EVENT ====================
	
	/**
   * Calculates all positions, scales, and bounding boxes needed for events,
   * including event background and choices.
   */
	private static void getEventValue() {
		getEventBackgroundValue();
		getEventChoiceValue();
		getEventChoiceEndValue();
	}
	
	/**
   * Calculates the position, scale, and bounding box for the event background.
   */
	private static void getEventBackgroundValue() {
		BufferedImage img = imgLoader.bgImages().get("BG_EVENT");
    int imgW = img.getWidth();
    int imgH = img.getHeight();
    double scaleX = 0.80 * screenWidth / imgW;
    double scaleY = 0.40 * screenHeight / imgH;
    double imgHeightScale = imgH * scaleY;
    double imgWidthScale = imgW * scaleX;
    double drawY = screenHeight - imgHeightScale;
    AffineTransform transform = new AffineTransform();
    transform.translate(screenWidth/2 - (imgWidthScale/2.0), drawY);
    transform.scale(scaleX, scaleY);
    var boundingBox = new BoundingBox(new XY(screenWidth / 2 - (int) imgWidthScale/2, (int) drawY), new XY(screenWidth / 2 + (int) imgWidthScale/2, screenHeight));
    renderDataGame.put("BG_EVENT", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates positions and bounding boxes for event choice buttons (choice 1 and choice 2).
   */
	private static void getEventChoiceValue() {
		// Choice 1
		var img = imgLoader.bgImages().get("BG_CHOICE1");
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
	  img = imgLoader.bgImages().get("BG_CHOICE2");
	  AffineTransform transform2 = new AffineTransform();
	  posX = screenWidth * (0.5 + 0.23) - (width * scale);
	  transform2.translate(posX, posY);
	  transform2.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_CHOICE2", new RenderData(transform2, boundingBox));
	}
	
	/**
   * Calculates the position and bounding box for the "End Choice" event button.
   */
	private static void getEventChoiceEndValue() {
		var img = imgLoader.bgImages().get("BG_CHOICE_END");
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
	
	//================= ENDTURN =====================
	
	 /**
   * Calculates position, scale, and bounding box of the "End Turn" button.
   */
	private static void getEndTurnValue() {
		var img = imgLoader.bgImages().get("BG_ENDTURN");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenWidth * 0.10) / width;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth * 0.5 - (width * scale) / 2;
	  double posY = renderDataGame.get("BG_BACKPACK").box().southEast().y() + data.bag().getGridSize();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_ENDTURN", new RenderData(transform, boundingBox));
	}

	//================= BIN =====================
	
	/**
   * Calculates positions, scales, and bounding boxes for the bin button
   * in both open and closed states.
   */
	private static void getBinValue() {
		var img = imgLoader.bgImages().get("BG_BIN_CLOSE");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenWidth * 0.10) / width;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth * 0.5 - (width * scale) / 2;
	  double posY = renderDataGame.get("BG_BACKPACK").box().southEast().y() + data.bag().getGridSize();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_BIN_CLOSE", new RenderData(transform, boundingBox));
		img = imgLoader.bgImages().get("BG_BIN_OPEN");	
	  renderDataGame.put("BG_BIN_OPEN", new RenderData(transform, boundingBox));
	}	
	
	//================= Shop =====================
	
	/**
   * Calculates all positions, scales, and bounding boxes needed for the shop,
   * including background, character, bubble, article areas, buttons, and sell area.
   */
	private static void getShopValue() {
		var img = imgLoader.bgImages().get("BG_SHOP");	
		int width = img.getWidth();
		int height = img.getHeight();
		double scale = getBGshopValue();
	  getCharacterValue(width * scale, height * scale);
	  getBubbleShopValue(width * scale, height * scale);
	  getExitShopValue(width * scale, height * scale);
	  getArticleAreaShopValue(width * scale, height * scale);
	  getSellArticleShopValue(width * scale, height * scale);
	  var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		int articleHeight = articleBoundingBox.southEast().y() - articleBoundingBox.northWest().y();
		int articleWidth = articleBoundingBox.southEast().x() - articleBoundingBox.northWest().x();
	  getButtonShopValue(articleWidth, articleHeight);
	  getBuyButtonShopValue(articleWidth, articleHeight);
	  getArticleShopValue();
	  getSoldOutShopValue(articleWidth, articleHeight);
	}	
	
	/**
   * Calculates the scale and bounding box for the shop background.
   *
   * @return The scale factor applied to the shop background
   */
	private static double getBGshopValue() {
		var img = imgLoader.bgImages().get("BG_SHOP");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenHeight * 0.60) / height;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth - (width * scale);
	  double posY = screenHeight - (height * scale);
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_SHOP", new RenderData(transform, boundingBox));
	  return scale;
	}
	
	/**
   * Calculates the position and bounding box of the shop character.
   */
	private static void getCharacterValue(double shopWidth, double shopHeight) {
		var img = imgLoader.bgImages().get("RolandBody");
		int width = img.getWidth();
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth / 2 - (width / 2);
	  double posY = screenHeight * 0.65;
	  transform.translate(posX, posY);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width), screenHeight));
	  renderDataGame.put("RolandBody", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates the position and bounding box for the speech bubble in the shop.
   */
	private static void getBubbleShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.15;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.68;
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.48), (int) (posY + shopHeight*0.17)));
	  renderDataGame.put("BG_SHOP_BUBBLE", new RenderData(null, boundingBox));
	}
	
	/**
   * Calculates the area where articles are displayed in the shop.
   */
	private static void getArticleAreaShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.1;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.1;
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.7), (int) (posY + shopHeight * 0.55)));
	  renderDataGame.put("SHOP_ARTICLE", new RenderData(null, boundingBox));
	}
	
	/**
   * Calculates the position, scale, and bounding box for the sell article button.
   */
	private static void getSellArticleShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.81;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.1;
		var img = imgLoader.bgImages().get("ICON_SELL_BUTTON");
		int width = img.getWidth();
		int height = img.getHeight();
		var scaleX = (shopWidth * 0.1) / width;
		var scaleY = (shopHeight * 0.30) / height;
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scaleX, scaleY);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.1), (int) (posY + shopHeight * 0.30)));
	  renderDataGame.put("SHOP_SELL_ARTICLE", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates the bounding boxes for article names and images in the shop.
   */
	private static void getArticleShopValue() {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		getArticleNameHolderShopValue(articleBoundingBox);
	  getArticleImageHolderShopValue(articleBoundingBox);
	}
	
	 /**
   * Calculates the bounding box for the article name holder.
   *
   * @param articleBoundingBox Bounding box of the article container
   */
	private static void getArticleNameHolderShopValue(BoundingBox articleBoundingBox) {
		var buttonBoundingBox = renderDataGame.get("ICON_SHOP_BUY").box();
		int articleHeight = articleBoundingBox.southEast().y() - articleBoundingBox.northWest().y();
		double posX = buttonBoundingBox.northWest().x();
		double posY = articleBoundingBox.northWest().y() + 0.01 * articleHeight;
		int width = buttonBoundingBox.southEast().x() - (int) posX;
		int height = (int) (articleBoundingBox.northWest().y() + 0.15 * articleHeight - posY);
	  var newBoundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width), (int) (posY + height)));
	  renderDataGame.put("SHOP_ARTICLE_NAME_HOLDER", new RenderData(null, newBoundingBox));
	}
	
	/**
   * Calculates the bounding box for the article image holder.
   *
   * @param articleBoundingBox Bounding box of the article container
   */
	private static void getArticleImageHolderShopValue(BoundingBox articleBoundingBox) {
		var buttonBoundingBox = renderDataGame.get("ICON_SHOP_BUY").box();
		int articleHeight = articleBoundingBox.southEast().y() - articleBoundingBox.northWest().y();
		int posX = buttonBoundingBox.northWest().x();
		int posY = (int) (articleBoundingBox.northWest().y() + 0.16 * articleHeight);
		int width = buttonBoundingBox.southEast().x() - posX;
		int height = buttonBoundingBox.northWest().y() - posY;
	  var newBoundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width), (int) (posY + height)));
	  renderDataGame.put("SHOP_ARTICLE_IMAGE_HOLDER", new RenderData(null, newBoundingBox));
	}
	
	/**
	 * Calculates position, scale, and bounding box for the shop exit button.
	 */
	private static void getExitShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		var img = imgLoader.bgImages().get("ICON_EXIT_SHOP");
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenHeight * 0.10) / height;
	  double posX = shopBoundingBox.northWest().x() - (width*scale / 3);
	  double posY = shopBoundingBox.northWest().y();
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width * scale), (int) (posY + height * scale)));
	  renderDataGame.put("ICON_EXIT_SHOP", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates positions and bounding boxes for the left and right shop buttons.
   */
	private static void getButtonShopValue(double articleWidth, double articleHeight) {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		var img = imgLoader.bgImages().get("ICON_SHOP_LEFT");
		var scale = (articleHeight * 0.20) / img.getHeight();
	  double posX = articleBoundingBox.northWest().x() + articleWidth * 0.05;
	  double posY = articleBoundingBox.northWest().y() + articleHeight * 0.75;
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scale), (int) (posY + img.getHeight() * scale)));
	  renderDataGame.put("ICON_SHOP_LEFT", new RenderData(transform, boundingBox));
	  img = imgLoader.bgImages().get("ICON_SHOP_RIGHT");
	  posX = articleBoundingBox.southEast().x() - img.getWidth() * scale - articleWidth * 0.05;
	  transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scale), (int) (posY + img.getHeight() * scale)));
	  renderDataGame.put("ICON_SHOP_RIGHT", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates the position, scale, and bounding box for the "Buy" button in the shop.
   */
	private static void getBuyButtonShopValue(double articleWidth, double articleHeight) {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		var img = imgLoader.bgImages().get("ICON_SHOP_BUY");
		var scaleY = (articleHeight * 0.20) / img.getHeight();
		var imgButton = imgLoader.bgImages().get("ICON_SHOP_LEFT");
		var widthButton = imgButton.getWidth() * (articleHeight * 0.20) / imgButton.getHeight();
		var scaleX = (articleWidth - articleWidth * 0.15 - widthButton * 2) / img.getWidth();
	  double posX = articleBoundingBox.northWest().x() + articleWidth * 0.5 - img.getWidth() * scaleX / 2;
	  double posY = articleBoundingBox.northWest().y() + articleHeight * 0.75;
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scaleX, scaleY);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scaleX), (int) (posY + img.getHeight() * scaleY)));
	  renderDataGame.put("ICON_SHOP_BUY", new RenderData(transform, boundingBox));
	}
	
	/**
   * Calculates the bounding box for the "Sold Out" icon in the shop.
   */
	private static void getSoldOutShopValue(double articleWidth, double articleHeight) {
		var img = imgLoader.bgImages().get("ICON_SOLDOUT");
		var shopBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = articleWidth / width;
	  double posX = shopBoundingBox.northWest().x();
	  double posY = shopBoundingBox.northWest().y() + articleHeight/2 - (height * scale)/2;
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scale), (int) (posY + img.getHeight() * scale)));
	  renderDataGame.put("ICON_SOLDOUT", new RenderData(transform, boundingBox));
	}

	/**
   * Calculates the position, scale, and bounding box for the "Abandon" (fast-forward) button.
   */
	private static void getFFButtonValue() {
		var img = imgLoader.bgImages().get("ICON_ABANDON");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenWidth * 0.05) / width;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth - (width * scale);
	  double posY = screenHeight - (height * scale);
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("ICON_ABANDON", new RenderData(transform, boundingBox));
	}
	
  // ========================= GETTERS =========================

	/**
   * Returns a map of all RenderData objects keyed by element name.
   *
   * @return LinkedHashMap of element name -> RenderData
   */
	public static LinkedHashMap<String, RenderData> getMapEvent() {
		return renderDataGame;
	}
	
	/**
   * Returns the screen width.
   *
   * @return Screen width in pixels
   */
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	/**
   * Returns the screen height.
   *
   * @return Screen height in pixels
   */
	public static int getScreenHeight() {
		return screenHeight;
	}
	
}
