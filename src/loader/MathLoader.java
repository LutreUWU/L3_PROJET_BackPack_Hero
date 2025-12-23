package loader;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import game.GameData;
import model.BoundingBox;
import model.XY;

/**
 * The GameDataMath class is where all coordinate, size ... depending of the screen are stored.
 */
public class MathLoader {
	private static int screenWidth;
	private static int screenHeight;
	private static GameData data;
	
	
	private static LinkedHashMap<String, RenderData> renderDataGame = new LinkedHashMap<>();
	
	public MathLoader(GameData dataGame) {
		data = dataGame;
		screenWidth = data.screenInfo().width();
		screenHeight = data.screenInfo().height();
		getBGValue();
		getIconHeroValue();
		getBackpackValue();
		getInfoItemValue();
		getMapValue();
		getEventValue();	
		getEndTurnValue();
		getBinValue();
		getShopValue();
	}
	
// ================= ICON =====================
	private static void getIconHeroValue() {
  	double sizeY =  screenHeight * 0.04;
  	// ICON HEALTH
		BufferedImage img = data.imgMap().get("ICON_HEALTH");
		var width = img.getWidth();
		var height = img.getHeight();
		double scale = sizeY / height;
		var transform = new AffineTransform();
    transform.scale(scale, scale);
		XY NW = new XY(0, 0);
		XY SE = new XY((int) (width * scale), (int) (height * scale));
		renderDataGame.put("ICON_HEALTH", new RenderData(transform, new BoundingBox(NW, SE)));
		// ICON SHIELD
		img = data.imgMap().get("ICON_SHIELD");
		transform = new AffineTransform();
    transform.translate(0, height * scale);
    transform.scale(scale, scale);
		NW = new XY(0, (int) (height * scale));
		SE = new XY((int) (width * scale), (int) (height * scale * 2));
		renderDataGame.put("ICON_SHIELD", new RenderData(transform, new BoundingBox(NW, SE)));
		// ICON MANA
		img = data.imgMap().get("ICON_MANA");
		transform = new AffineTransform();
    transform.translate(0, height * scale * 2);
    transform.scale(scale, scale);
		NW = new XY(0, (int) (height * scale * 2));
		SE = new XY((int) (width * scale), (int) (height * scale * 3));
		renderDataGame.put("ICON_MANA", new RenderData(transform, new BoundingBox(NW, SE)));
		// ICON ACTION
		img = data.imgMap().get("ICON_ACTION");
		transform = new AffineTransform();
    transform.translate(0, height * scale * 3);
    transform.scale(scale, scale);
		NW = new XY(0, (int) (height * scale * 3));
		SE = new XY((int) (width * scale), (int) (height * scale * 4));
		renderDataGame.put("ICON_ACTION", new RenderData(transform, new BoundingBox(NW, SE)));
		// ICON UNLOCK
		img = data.imgMap().get("ICON_UNLOCK");
		transform = new AffineTransform();
    transform.translate(0, height * scale * 4);
    transform.scale(scale, scale);
		NW = new XY(0, (int) (height * scale * 4));
		SE = new XY((int) (width * scale), (int) (height * scale * 5));
		renderDataGame.put("ICON_UNLOCK", new RenderData(transform, new BoundingBox(NW, SE)));
		// ICON GOLD
		img = data.imgMap().get("gold");
		transform = new AffineTransform();
    transform.translate(0, height * scale * 5);
    transform.scale(scale, scale);
		NW = new XY(0, (int) (height * scale * 5));
		SE = new XY((int) (width * scale), (int) (height * scale * 6));
		renderDataGame.put("gold", new RenderData(transform, new BoundingBox(NW, SE)));

	}
	
//============================================	
	
// ================== BG ======================
	private static void getBGValue() {
		for (int i = 1; i < 7; i ++) {
			getBGimgValue("BG" + i);
		}
	}
	
	private static void getBGimgValue(String BG_name) {
		BufferedImage img = data.imgMap().get(BG_name);
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
		var size = data.bag().getGridSize();
		var dimX = size * 9.0;
		var dimY = size * 7.0;
		XY NW = new XY((int) (screenWidth / 2 - 3.5 * size), (int) (screenHeight * 0.02 + size * 0.8));
		XY SE = new XY((int) (screenWidth / 2 + 3.5 * size), (int) (screenHeight * 0.02 + size * 7.0));
		BufferedImage img = data.imgMap().get("BG_BACKPACK");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(screenWidth / 2 - dimX / 2, screenHeight * 0.02) ;
	  transform.scale(scaleX, scaleY);
		renderDataGame.put("BG_BACKPACK", new RenderData(transform, new BoundingBox(NW, SE)));
	}
	
// ============================================
	
//================== INFO ITEM ================
	private static  void getInfoItemValue() {
		var size = data.bag().getGridSize();
		var dimX = size * 3.0;
		var dimY = size * 5.0;
		var NW = new XY(renderDataGame.get("BG_BACKPACK").box().southEast().x() + size, renderDataGame.get("BG_BACKPACK").box().northWest().y());
		var SE = new XY(renderDataGame.get("BG_BACKPACK").box().southEast().x() + size + (int) dimX, renderDataGame.get("BG_BACKPACK").box().southEast().y() );
		BufferedImage img = data.imgMap().get("BG_INFO_ITEM");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(NW.x(), NW.y());
	  transform.scale(scaleX, scaleY);
	  var realNW = new XY((int) (NW.x() + size * 0.9), NW.y() + size/2);
	  var realSE = new XY((int) (SE.x() + size * 0.9), SE.y() + size/2);
		renderDataGame.put("BG_INFO_ITEM", new RenderData(transform, new BoundingBox(realNW, realSE)));
	}
		
//============================================
	
// ================== Map =====================
	private static void getMapValue() {
		var size = data.bag().getGridSize();
  	var gap = size * 0.1;
		var dimX = size * 12.0 + 10 * gap;
		var dimY = size * 6.0 + 4 * gap;
		XY NW = new XY((int) ((screenWidth / 2) - 5.5 * size - 5*gap), (int) ((screenHeight * 0.04 + size / 2.0)));
		XY SE = new XY((int) ((screenWidth / 2) + 5.5 * size + 5*gap), (int) ((screenHeight * 0.04 + size / 2.0) + 5.0 * size + 4*gap));
		BufferedImage img = data.imgMap().get("BG_MAP");
		var width = img.getWidth();
		var height = img.getHeight();
		var scaleX = dimX / width;
		var scaleY = dimY / height;
	  AffineTransform transform = new AffineTransform();
		transform.translate(NW.x() - size / 2.0, NW.y() - size / 2.0);
	  transform.scale(scaleX, scaleY);
		renderDataGame.put("BG_MAP", new RenderData(transform, new BoundingBox(NW, SE)));
	}
// ============================================
	
	
// ================= Hero =====================
	
// ============================================
	
//============================================

	
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
		BufferedImage img = data.imgMap().get("BG_EVENT");
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
	
	private static void getEventChoiceValue() {
		// Choice 1
		var img = data.imgMap().get("BG_CHOICE1");
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
	  img = data.imgMap().get("BG_CHOICE2");
	  AffineTransform transform2 = new AffineTransform();
	  posX = screenWidth * (0.5 + 0.23) - (width * scale);
	  transform2.translate(posX, posY);
	  transform2.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_CHOICE2", new RenderData(transform2, boundingBox));
	}
	
	private static void getEventChoiceEndValue() {
		var img = data.imgMap().get("BG_CHOICE_END");
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
	
	
// ===========================================
	
//================= ENDTURN =====================
	private static void getEndTurnValue() {
		var img = data.imgMap().get("BG_ENDTURN");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenWidth * 0.10) / width;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth * 0.5 - (width * scale) / 2;
	  double posY = renderDataGame.get("BG_BACKPACK").box().southEast().y() * 0.95 ;
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_ENDTURN", new RenderData(transform, boundingBox));
	}
	
//============================================
	
//================= BIN =====================
	private static void getBinValue() {
		var img = data.imgMap().get("BG_BIN_CLOSE");	
		int width = img.getWidth();
		int height = img.getHeight();
		var scale = (screenWidth * 0.10) / width;
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth * 0.5 - (width * scale) / 2;
	  double posY = renderDataGame.get("BG_BACKPACK").box().southEast().y() * 0.95 ;
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width*scale), (int) (posY + height*scale)));
	  renderDataGame.put("BG_BIN_CLOSE", new RenderData(transform, boundingBox));
		img = data.imgMap().get("BG_BIN_OPEN");	
	  renderDataGame.put("BG_BIN_OPEN", new RenderData(transform, boundingBox));
	}	
// ===================
	
//================= Shop =====================
	private static void getShopValue() {
		var img = data.imgMap().get("BG_SHOP");	
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
	  
	}	
	
	private static double getBGshopValue() {
		var img = data.imgMap().get("BG_SHOP");	
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
	
	private static void getCharacterValue(double shopWidth, double shopHeight) {
		var img = data.imgMap().get("RolandBody");
		int width = img.getWidth();
		int height = img.getHeight();
		AffineTransform transform = new AffineTransform();
	  double posX = screenWidth / 2 - (width / 2);
	  double posY = screenHeight * 0.65;
	  transform.translate(posX, posY);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + width), screenHeight));
	  renderDataGame.put("RolandBody", new RenderData(transform, boundingBox));
	}
	
	private static void getBubbleShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.15;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.68;
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.48), (int) (posY + shopHeight*0.17)));
	  renderDataGame.put("BG_SHOP_BUBBLE", new RenderData(null, boundingBox));
	}
	
	private static void getArticleAreaShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.1;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.1;
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.7), (int) (posY + shopHeight * 0.55)));
	  renderDataGame.put("SHOP_ARTICLE", new RenderData(null, boundingBox));
	}
	
	private static void getSellArticleShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		double posX = shopBoundingBox.northWest().x() + shopWidth * 0.81;
		double posY = shopBoundingBox.northWest().y() + shopHeight * 0.1;
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + shopWidth * 0.1), (int) (posY + shopHeight * 0.30)));
	  renderDataGame.put("SHOP_SELL_ARTICLE", new RenderData(null, boundingBox));
	}
	
	private static void getArticleShopValue() {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		getArticleNameHolderShopValue(articleBoundingBox);
	  getArticleImageHolderShopValue(articleBoundingBox);
	}
	
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
	
	
	
	private static void getExitShopValue(double shopWidth, double shopHeight) {
		var shopBoundingBox = renderDataGame.get("BG_SHOP").box();
		var img = data.imgMap().get("ICON_EXIT_SHOP");
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
	
	private static void getButtonShopValue(double articleWidth, double articleHeight) {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		var img = data.imgMap().get("ICON_SHOP_LEFT");
		var scale = (articleHeight * 0.20) / img.getHeight();
	  double posX = articleBoundingBox.northWest().x() + articleWidth * 0.05;
	  double posY = articleBoundingBox.northWest().y() + articleHeight * 0.75;
		AffineTransform transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  var boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scale), (int) (posY + img.getHeight() * scale)));
	  renderDataGame.put("ICON_SHOP_LEFT", new RenderData(transform, boundingBox));
	  img = data.imgMap().get("ICON_SHOP_RIGHT");
	  posX = articleBoundingBox.southEast().x() - img.getWidth() * scale - articleWidth * 0.05;
	  transform = new AffineTransform();
	  transform.translate(posX, posY);
	  transform.scale(scale, scale);
	  boundingBox = new BoundingBox(new XY((int) posX, (int) posY), new XY((int) (posX + img.getWidth() * scale), (int) (posY + img.getHeight() * scale)));
	  renderDataGame.put("ICON_SHOP_RIGHT", new RenderData(transform, boundingBox));
	}
	
	private static void getBuyButtonShopValue(double articleWidth, double articleHeight) {
		var articleBoundingBox = renderDataGame.get("SHOP_ARTICLE").box();
		var img = data.imgMap().get("ICON_SHOP_BUY");
		var scaleY = (articleHeight * 0.20) / img.getHeight();
		var imgButton = data.imgMap().get("ICON_SHOP_LEFT");
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

//============================================	
//Getter Event
	public static LinkedHashMap<String, RenderData> getMapEvent() {
		return renderDataGame;
	}
	
}
