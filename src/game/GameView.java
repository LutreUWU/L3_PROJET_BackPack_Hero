package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.github.forax.zen.ApplicationContext;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import loader.FontLoader;
import loader.MathLoader;
import model.BoundingBox;
import model.Direction;
import model.Item;
import model.XY;
import model.item.common.Arrow;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.common.Sword;
import model.item.epic.Bow;
import model.item.epic.DespairShield;
import model.item.legendary.Axe;
import model.item.mythic.Mimicry;
import model.item.rare.Gant;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;
import model.item.superrare.Massue;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Floor;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Room;
import model.map.Shop;
import model.map.Start;
import model.map.Treasure;
import model.monster.Enemy;

 /**
  * The SimpleGameView class deals with the display of the game the screen, and
  * with the interpretation of which zones were clicked on by the user.
  * 
  * @param width    	Width of the windows screen
  * @param height	Height of the windows screen
  * @param tileSize Size of a grid in the bag
  *
  */
public record GameView(int width, int height, int tileSize) {	
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
	 * Draw the background of the game
	 * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
	 */
	private void drawBG(Graphics2D graphics, GameData data) {
		BufferedImage img = data.imgMap().get("BG1");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG1").transform(), null);
	}
	
	/**
	 * Draw an image in the window
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
	 */
	private void drawElement(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX / 2.0;
	  double centerY = y + dimY / 2.0;
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width / 2, -height / 2);
	  graphics.drawImage(img, transform, null);
	}
	
	/**
   * <p>
   * Draw an image but with a specifity.<br>
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
   * </p>
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
	 */
	private void drawSpecialElement(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction, double marginX, double marginY) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX / 2.0;
	  double centerY = y + dimY / 2.0;
	  // J'ai aucune idée pq ça marche, mais ça a l'air de marché ???
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width * marginX, -height * marginY); 
	  graphics.drawImage(img, transform, null);
	}
	
	/**
   * <p>
   * Draw an item in the bag but with a specifity.<br>
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
   * </p>
	 * 
	 * @param graphics	{@code Graphics2D} of the game
	 * @param img				Image we wants to put
   * @param x  				The x coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
   * @param y 				The y coordinate of the location in user space where
   * 									the upper left corner of the image is rendered
	 * @param dimX			Width of the image
	 * @param dimY			Height of the image
	 * @param direction Direction we wants to draw it
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
	 */
	private void drawSpecialElementInBag(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction, double marginX, double marginY) {
		double angle = Math.toRadians(90 * direction.ordinal());
		var width = img.getWidth();
		var height = img.getHeight();
		var scale = Math.min(dimX / width, dimY / height);
	  AffineTransform transform = new AffineTransform();
	  double centerX = x + dimX * marginX;
	  double centerY = y + dimY * marginY;
	  transform.translate(centerX, centerY);
	  transform.rotate(angle);
	  transform.scale(scale, scale);
	  transform.translate(-width * marginX, -height * marginY); // We modify this
	  graphics.drawImage(img, transform, null);
	}
	
  /** 
   * Draw the grid of the Backpack
   * @param context Which window to draw
   * @param data	  Data of the game
   */	
  private void drawGrid(Graphics2D graphics, GameData data) {
    int size = data.bag().getGridSize();
		int [][] grid = data.bag().grid();
		BufferedImage imgBackpack = data.imgMap().get("BG_BACKPACK");
		BoundingBox boundingBox = MathLoader.getMapEvent().get("BG_BACKPACK").box(); 
		graphics.drawImage(imgBackpack, MathLoader.getMapEvent().get("BG_BACKPACK").transform(), null);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
	    	final int fi = i;
	    	final int fj = j;				  
		  	if (grid[fi][fj] >= -1) {
		  		graphics.drawImage(data.imgMap().get("BG_BAG_UNLOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  		
		  	}
		  	if (grid[fi][fj] == -2) {
		  		graphics.drawImage(data.imgMap().get("BG_BAG_LOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  	}
			}
    }
  }
  
  /**
   * Check the id int the bag we wants to draw and calls the appropriate method.<br>
   * This method use a switch on the item id to know which item we wants to draw.
   * 
   * @param graphics {@Code Graphics2D} of the game
   * @param data	   Data of the game
   */
  private void drawItemBag(Graphics2D graphics, GameData data) {
		var itemLst = data.bag().bagItemLst();
		for (var item : itemLst) {
			XY coordinate = item.shape()[0];
		  switch (item.ID()) {
			  case 1 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), data.imgMap().get("keyDoor"), 0.5, 0.75);
			  case 2 -> drawItemGold(graphics, data, coordinate, item);
				case 3 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), data.imgMap().get("sword")); 
				case 4 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), data.imgMap().get("despairShield"), 0.25, 0.25); 
				case 5 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), data.imgMap().get("mimicry")); 
				case 6 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), data.imgMap().get("massue")); 
				case 7 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), data.imgMap().get("gant"), 0.5, 0.75); 
				case 8 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 2, 3, item.direction(), data.imgMap().get("axe"), 0.20, 0.5); 
				case 9 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("arrow"));
				case 10 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), data.imgMap().get("bow"), 0.25, 0.25);
				case 11 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("poisonArrow"));
				case 12 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("bomb"));
				default ->{}
		  }
		}
  }
  
  private void drawItemGold(Graphics2D graphics, GameData data, XY coordinate, Item item) {
  	var itemGold = (Gold) item;
  	switch (getSizeGold(itemGold.value())) {
  	case 3 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("gold3"));
  	case 2 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("gold2"));
  	default -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), data.imgMap().get("gold1"));
  	}
  }
  
  private String getDescriptionItem(Item item) {
  	return switch (item) {
	  	case KeyDoor _ -> "J'ai retrouvé mes clés de maison"; 
	  	case Gold _ -> "Comme dans la vraie vie, sans argent c'est la merde";
	  	case Sword _ -> "Une épée simple et basique.";
	  	case DespairShield _ -> "Un bouclier pas très gentil";
	  	case Mimicry _ -> "GG, t'as finis le jeu";
	  	case Massue _ -> "Une épée, mais c'est une masse";
	  	case Gant _ -> "Un super gant pour l'hiver";
	  	case Axe _ -> "Une HACHE AOUH AOUH";
	  	case Arrow _ -> "Des flèches volées à Steeve";
	  	case Bow _ -> "Un arc volé à Steeve";
	  	case PoisonArrow _ -> "Des flèches volées à Steeve";
	  	case Bomb _ -> "Bombe volé à Mario";
	  	default -> throw new IllegalArgumentException("Unexpected value: " + item.ID());
  	};
  }
  
  private String getEffectItem(Item item) {
  	var durabilityOrQuantityString = (item.canMerge() ? "Quantité : " : "Durabilité : ") + item.durability() + " ";
  	return switch (item) {
	  	case KeyDoor _ -> durabilityOrQuantityString + "Déverouille une porte (1 fois)"; 
	  	case Gold g -> "Il y a " + g.value() + "gold";
	  	case Sword _ -> durabilityOrQuantityString + "1AP : Inflige -3 à l'ennemi";
	  	case DespairShield _ -> durabilityOrQuantityString + "1AP : Se met 10 Shield en échange de 3PV";
	  	case Mimicry _ -> durabilityOrQuantityString + "-30 PV en échange de 5PV";
	  	case Massue _ -> durabilityOrQuantityString + "2AP : Influge -30PV à l'ennemi en échange de 5PV";
	  	case Gant _ -> durabilityOrQuantityString + "1AP : Régénère 10 PV";
	  	case Axe _ -> durabilityOrQuantityString + "1AP : Inflige -10PV à l'ennemi";
	  	case Arrow _ -> durabilityOrQuantityString + "1AP : Inflige -8PV à l'ennemi";
	  	case PoisonArrow _ -> durabilityOrQuantityString + "1AP : Inflige -6PV à l'ennemi et empoisonne l'ennemi";
	  	case Bow _ -> durabilityOrQuantityString + "1AP : Inflige -8PV à l'ennemi";
	  	case Bomb _ -> durabilityOrQuantityString + "2AP : Inflige -6PV à tous les ennemies";
	  	default -> throw new IllegalArgumentException("Unexpected value: " + item.ID());
  	};
  }
  
  private void drawItemInfo(Graphics2D graphics, GameData data) {
  	BufferedImage imgItemInfo = data.imgMap().get("BG_INFO_ITEM");
  	BoundingBox itemInfoBoundingBox = MathLoader.getMapEvent().get("BG_INFO_ITEM").box();
  	XY NW = itemInfoBoundingBox.northWest();
  	XY SE = itemInfoBoundingBox.southEast();
		graphics.drawImage(imgItemInfo, MathLoader.getMapEvent().get("BG_INFO_ITEM").transform(), null);
		Item item = data.dragItem();
		if (item != null) {
	    drawTextInfoName(graphics, item, NW);
		  drawTextInfo(graphics, getDescriptionItem(item), NW.x(), NW.y() + (int) (NW.y() * 0.30), 20);
		  drawTextInfo(graphics, getEffectItem(item), NW.x(), NW.y() + (int) ((SE.y() - NW.y()) / 2), 20);

		}
  }
  
  private void drawTextInfoName(Graphics2D graphics, Item item, XY NW) {
  	Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH2());
    graphics.setFont(font);
    graphics.setColor(switch(item.rarity()) {
	    case COMMON -> Color.GRAY;
	    case RARE -> Color.GREEN;
	    case SUPERARE -> Color.BLUE;
	    case EPIC -> Color.MAGENTA;
	    case LEGENDARY -> Color.YELLOW;
	    case MYTHIC -> Color.PINK;
    });
	  graphics.drawString(item.toString().toUpperCase(), NW.x(),	NW.y() + (int) (NW.y() * 0.10));

  }
  
  private void drawTextInfo(Graphics2D graphics, String content, int x, int y, int maxChar) {
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getSpan());
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    graphics.setColor(Color.WHITE);
    int maxCharsPerLine = maxChar;
    String[] words = content.split(" ");
    StringBuilder line = new StringBuilder();
    int lineCount = 0;
    for (String word : words) {
        if (line.length() + word.length() + 1 > maxCharsPerLine) {
            graphics.drawString(line.toString(), x, y + lineCount * fm.getHeight());
            line = new StringBuilder(word);
            lineCount++;
        } else {
            if (line.length() > 0) line.append(" ");
            line.append(word);
        }
    }
    if (line.length() > 0) {
        graphics.drawString(line.toString(), x, y + lineCount * fm.getHeight());
    }
  }
  
  /**
   * Draw an item in the backpack
   * 
   * @param graphics	{@code Graphics2D} of the game
   * @param pos				{@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param width			Number of tile horizontally
   * @param height		Number of tile vertically
   * @param direction	Direction the img aim
   * @param img				{@code BufferedImage} of the item
   */
	private void drawInBag(Graphics2D graphics, XY pos, int width, int height, Direction direction, BufferedImage img){
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		drawElement(graphics, img, 
																	 coord.northWest().x() + (tileSize * pos.x()) + centerX,
																	 coord.northWest().y() + (tileSize * pos.y()) - centerY, 
																	 tileSize * width, tileSize * height, direction);
	}
	
	/**
   * Draw an item with a special shape in the backpack
   * 
   * @param graphics	{@code Graphics2D} of the game
   * @param pos				{@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param width			Number of tile horizontally
   * @param height		Number of tile vertically
   * @param direction	Direction the img aim
   * @param img				{@code BufferedImage} of the item
   * @param marginX		Value between 0.0 and 1.0 indicating the gap horizontally
   * @param marginY		Value between 0.0 and 1.0 indicating the gap vertically
   */
	private void drawInBagSpecial(Graphics2D graphics, XY pos, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY){
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		drawSpecialElementInBag(graphics, img, 
																	 coord.northWest().x() + (tileSize * pos.x()) + centerX,
																	 coord.northWest().y() + (tileSize * pos.y()) - centerY, 
																	 tileSize * width, tileSize * height, direction, marginX, marginY);
	}
	
	/**
   * Draws the hero in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   */
  private void drawHero(Graphics2D graphics, GameData data) {
  	double size_x = data.hero().getSizeX();
  	double size_y = data.hero().getSizeY();
  	if (!data.getShop()) {
  		BufferedImage img = data.imgMap().get("Roland");
  		drawElement(graphics, img, width * 0.20, height * 0.50, size_x, size_y, Direction.UP);
  	}
		drawHeroStats(graphics, data, (int) (width * 0.20 + size_x/2),  (int) (height * 0.50 + size_y));
  }
  
  /**
   * Draws all the information about the hero
   * 
   * @param graphics {@code Graphics2D} object for drawing.
   * @param data 		 GameData containing the game data. 
   * @param x				 coordinate x where we wants to draw.
   * @param y				 coordinate y where we wants to draw.
   */
  private void drawHeroStats(Graphics2D graphics, GameData data, int x, int y) {
    drawHeroHP(graphics, data);
    drawHeroShield(graphics, data);
    drawHeroAction(graphics, data);
    drawHeroMana(graphics, data);
    drawHeroUnlock(graphics, data);
    drawHeroLevel(graphics, data);
    drawHeroGold(graphics, data);
    drawFloorLevel(graphics, data);
  }
  
  private void drawHeroHP(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_HEALTH");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("ICON_HEALTH");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2 , width * 0.20, size));
    graphics.setColor(Color.GREEN);
    graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20 * data.hero().getHP() / data.hero().getMax_HP(), size));
    graphics.setColor(Color.WHITE);
    graphics.draw(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20, size));
	  graphics.drawString(data.hero().getHP() + "/" + data.hero().getMax_HP(), (int) (logoWidth + width * 0.205), logoHeight / 2 + size / 2);
  }
  
  private void drawHeroShield(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_SHIELD");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("ICON_SHIELD");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
    graphics.setColor(Color.BLUE);
    graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20 * data.hero().getCurrent_protection() / data.hero().getMax_HP(), size));
    graphics.setColor(Color.WHITE);
    graphics.draw(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
	  graphics.drawString(data.hero().getCurrent_protection() + "/" + data.hero().getMax_HP(), (int) (logoWidth + width * 0.205), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroMana(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_MANA");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("ICON_MANA");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.CYAN);
	  graphics.drawString(data.hero().getMana_point() + " MANA" , (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroAction(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_ACTION");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("ICON_ACTION");
    graphics.drawImage(img, render.transform() , null);
    graphics.setColor(data.hero().getEnergy_point() > 1 ? Color.YELLOW : Color.RED);
	  graphics.drawString(data.hero().getEnergy_point() + " AP", (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroUnlock(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_UNLOCK");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("ICON_UNLOCK");
    graphics.drawImage(img, render.transform() , null);
    graphics.setColor(data.bag().getCaseUnlock() > 0 ? Color.GREEN : Color.RED);
	  graphics.drawString(data.bag().getCaseUnlock() + " CASE DEBLOQUABLE", (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroGold(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("gold");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = data.imgMap().get("gold1");
    graphics.drawImage(img, render.transform() , null);
    graphics.setColor(data.bag().getGoldInBag() > 0 ? Color.GREEN : Color.RED);
	  graphics.drawString(data.bag().getGoldInBag() + " gold", (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroLevel(Graphics2D graphics, GameData data) {
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    int textWidth = fm.stringWidth("LEVEL " + data.hero().getLevel());
    graphics.setColor(Color.WHITE);
	  graphics.drawString("LEVEL " + data.hero().getLevel(), width / 2 - textWidth / 2,	size);
	  graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Double(width / 2 - textWidth / 2, (int) (height * 0.03)  , textWidth, size * 0.25));
    graphics.setColor(Color.CYAN);
    graphics.fill(new Rectangle2D.Double(width / 2 - textWidth / 2, (int) (height * 0.03)  , textWidth * data.hero().getXp() / data.hero().MAX_XP(), size * 0.25));
  }
  
  private void drawFloorLevel(Graphics2D graphics, GameData data) {
  	int size = (int) (height * 0.03);
  	Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    int textWidth = fm.stringWidth("ETAGE : " + data.floor());
    graphics.setColor(Color.WHITE);
	  graphics.drawString("ETAGE : " + data.floor(), width - (int) (textWidth * 1.2),	size);
  }
  
  /**
   * Draw the button for switching between map and bag
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private void drawButton(Graphics2D graphics, GameData data) {
		graphics.setColor(Color.RED);  		
		graphics.setColor(data.mapOrBag() ? Color.ORANGE : Color.CYAN);
    graphics.fill(new Rectangle2D.Double(width - tileSize / 2, height/3.5 - 2.5 * tileSize, 
    																		 tileSize / 2, tileSize / 2));
  }
  
  /**
   * Draw the map in the screen
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private void drawMap(Graphics2D graphics, GameData data) {
  	BufferedImage imgMap = data.imgMap().get("BG_MAP");
  	var leftGrid = MathLoader.getMapEvent().get("BG_MAP").box();
  	var gap = tileSize * 0.1;
  	graphics.drawImage(imgMap, MathLoader.getMapEvent().get("BG_MAP").transform(), null);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 11; j++) {
	    	final int fi = i;
	    	final int fj = j;
		  	var coordXY = new XY(fj, fi);
		  	int newX = (int) (gap * fj) + leftGrid.northWest().x() + (tileSize * fj);
		  	int newY = (int) (gap * fi) + leftGrid.northWest().y() + (tileSize * fi);
		  	if (data.map().getHeroVisible().contains(coordXY)) {
		  		graphics.drawImage(data.imgMap().get("BG_MAP_TILE"), newX, newY, tileSize, tileSize, null);
			    if (data.map().getHeroAccessible().contains(coordXY)) {
			    	graphics.drawImage(data.imgMap().get("BG_MAP_TILE_ACCES"), newX, newY, tileSize, tileSize, null);
			    }
			  	switch(data.map().getGrid()[fi][fj]) {
			  		case Shop _ -> graphics.drawImage(data.imgMap().get("ICON_SHOP"), newX, newY, tileSize, tileSize, null);
			  		case EnemyRoom _ -> graphics.drawImage(data.imgMap().get("ICON_COMBAT"), newX, newY, tileSize, tileSize, null);
			  		case EventRoom _ -> graphics.drawImage(data.imgMap().get("ICON_EVENT"), newX, newY, tileSize, tileSize, null);
			  		case Healer _ -> graphics.drawImage(data.imgMap().get("ICON_HEAL"), newX, newY, tileSize, tileSize, null);
			  		case Start _ -> graphics.drawImage(data.imgMap().get("ICON_START"), newX, newY, tileSize, tileSize, null);
			  		case Exit _ -> graphics.drawImage(data.imgMap().get("ICON_EXIT"), newX, newY, tileSize, tileSize, null);
			  		case LockedDoor _ -> graphics.drawImage(data.imgMap().get("ICON_LOCK_DOOR"), newX, newY, tileSize, tileSize, null);
			  		case Treasure _ -> graphics.drawImage(data.imgMap().get("ICON_TREASURE"), newX, newY, tileSize, tileSize, null);
			  		default ->  {}
			  	}
		  	} else graphics.drawImage(data.imgMap().get("BG_MAP_SHADOW"), newX - (int) gap, newY - (int) gap, tileSize + (int) gap*2, tileSize + (int) gap*2, null);

      }
		}
		
		// A RETIRER QUAND ON AURA FINIT DE CRER LES MAPS
  	graphics.setColor(Color.ORANGE);
  	graphics.setStroke(new BasicStroke(5));
  	for (var coord : data.map().getHeroVisibleLine()) {
  		for (var coord_acc : data.map().getGrid()[coord.y()][coord.x()].getAccessible()) {
  			graphics.drawLine((int) ((gap * coord.x()) + (leftGrid.northWest().x() + (tileSize * coord.x() + tileSize/2))), 
													(int) ((gap * coord.y()) + (leftGrid.northWest().y() + (tileSize * coord.y() + tileSize/2))), 
													(int) ((gap * coord_acc.x()) + (leftGrid.northWest().x() + (tileSize * coord_acc.x()) + tileSize/2)), 
													(int) ((gap * coord_acc.y()) + (leftGrid.northWest().y() + (tileSize * coord_acc.y()) + tileSize/2)));
  		}
  	}
		
  	var shortestPath = data.getShortestPath();
  	if (shortestPath != null) {
  		graphics.setColor(Color.RED);
  		for (int i = 0; i < shortestPath.size() - 1; i++) {
  			var coord = shortestPath.get(i);
  			var coord_acc = shortestPath.get(i + 1);
  			graphics.drawLine((int) ((gap * coord.x()) + (leftGrid.northWest().x() + (tileSize * coord.x() + tileSize/2))), 
						(int) ((gap * coord.y()) + (leftGrid.northWest().y() + (tileSize * coord.y() + tileSize/2))), 
						(int) ((gap * coord_acc.x()) + (leftGrid.northWest().x() + (tileSize * coord_acc.x()) + tileSize/2)), 
						(int) ((gap * coord_acc.y()) + (leftGrid.northWest().y() + (tileSize * coord_acc.y()) + tileSize/2)));
  		}
  	}
  	
		graphics.setColor(Color.WHITE);
		var coord = data.map().getHeroPos();
		graphics.drawImage(data.imgMap().get("ICON_HERO"), (int) (gap * coord.x()) + leftGrid.northWest().x() + (coord.x() * tileSize), (int) (gap * coord.y()) + leftGrid.northWest().y() + (coord.y() * tileSize), tileSize, tileSize, null);
		///////////////////////////////////
  }

	
  /**
   * Update the position of each weapons we can move in the screen.
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data     GameData containing the game data.
   */	
  private void updateDragItem(Graphics2D graphics, GameData data) {
		GameDataClick.getDragItemMap().reversed().forEach((item, box) -> drawDrag(graphics, data, item, box));
  }
  
  /**
   * Check the id of the item we're dragging and calls the appropriate method.<br>
   * This method use a switch on the item id to know which item we wants to draw.
   * 
   * @param graphics {@code ApplicationContext} of the game.
   * @param data		 {@code GameData} containing the game data.
   * @param item		 The Item we're currently dragging.
   * @param box		 	 The boundingbox (border) of the item.
   */
  private void drawDrag(Graphics2D graphics, GameData data, Item item, BoundingBox box) {
	  switch (item.ID()) {
		  case 1 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), data.imgMap().get("keyDoor"), 0.5, 0.75); 
		  case 2 -> drawDragGold(graphics, data, item, box);
			case 3 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), data.imgMap().get("sword")); 
			case 4 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), data.imgMap().get("despairShield"), 0.25, 0.25); 
			case 5 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), data.imgMap().get("mimicry")); 
			case 6 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), data.imgMap().get("massue")); 
			case 7 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), data.imgMap().get("gant"), 0.5, 0.75); 
			case 8 -> drawDragSpecialItem(graphics, box.northWest(), 2, 3, item.direction(), data.imgMap().get("axe"), 0.20, 0.5); 
			case 9 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("arrow"));
			case 10 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), data.imgMap().get("bow"), 0.25, 0.25); 
			case 11 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("poisonArrow"));
			case 12 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("bomb"));
			default ->{}
	  }
	}
  
  private int getSizeGold(int value) {
  	if (value <= 15) return 1;
  	else if (value <= 50) return 2;
  	else return 3;
  }
  
  private void drawDragGold(Graphics2D graphics, GameData data, Item item, BoundingBox box) {
  	var itemGold = (Gold) item;
  	switch (getSizeGold(itemGold.value())) {
  	case 3 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("gold3"));
  	case 2 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("gold2")); 
  	default -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), data.imgMap().get("gold1"));
  	}
  }
  
  /**
   * Draw the current item we're dragging.
   * 
   * @param graphics  {@code ApplicationContext} of the game.
   * @param coord		  {@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param size		  Size of a tile.
   * @param width		  Number of tile horizontally. 
   * @param height 	  Number of tile vertically.
   * @param direction {@code Direction} the item will aim.
   * @param img				{@code BufferedImage} of the item.
   */
  private void drawDragItem(Graphics2D graphics, XY coord, int width, int height, Direction direction, BufferedImage img){
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = tileSize*width, dimY = tileSize*height;
		// If direction is left or right, the North West coordinate need to be update in order to draw correctly
	  if (direction == Direction.LEFT || direction == Direction.RIGHT) { 
	  	centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			x = centerX - dimX / 2.0;
			y = centerY - dimY / 2.0;
		}
		drawElement(graphics, img, x, y, dimX, dimY, direction);
	}
  
  /**
   * <p>
   * Draw the current item we're dragging but with a specifity.<br>
   * Initially, the other method {@code drawDragItem} draw the item base of the center of the image.
   * </p>
   * 
   * <p>
   * But since we're using .png image, it can happens that the center of the image is empty.<br>
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * </p>
   * 
   * <p>
   * We're adding two new parameters marginX and marginY to help drawing this item.<br>
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5
   * </p>
   * 
   * @param graphics  {@code ApplicationContext} of the game.
   * @param coord		  {@code XY} containing the coordinate NorthWest (x, y) of image on the screen.
   * @param width		  Number of tile horizontally. 
   * @param height 	  Number of tile vertically.
   * @param direction {@code Direction} the item will aim.
   * @param img				{@code BufferedImage} of the item.
   * @param marginX		Value between 0.0 and 1.0, the margeX of the item (If we wants the centerX, marginX = 0.5)
   * @param marginY	  Value between 0.0 and 1.0, the margeY of the item (If we wants the centerY, marginY = 0.5)
   */
  private void drawDragSpecialItem(Graphics2D graphics, XY coord, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY){
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = tileSize*width, dimY = tileSize*height;
	  if (direction == Direction.LEFT || direction == Direction.RIGHT) {
	  	centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			x = centerX - dimX / 2.0;
			y = centerY - dimY / 2.0;
		}
		drawSpecialElement(graphics, img, x, y, dimX, dimY, direction, marginX, marginY);
	}
  
  /**
   * Update the state of the combat.
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   * @param lstEnemy List of all enemy we fight
   */
  private void updateCombat(Graphics2D graphics, GameData data,  ArrayList<Enemy> lstEnemy) {
  	drawEndTurnButton(graphics,  data.imgMap().get("BG_ENDTURN"));
  	lstEnemy.forEach(enemy -> drawEnemy(graphics, data, enemy));
  	drawLog(graphics, data, GameDataCombat.getLog());
  }
  
  /**
   * Draws the enemy in the windows
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data    GameData containing the game data. 
   * @param enemy 	Data of the enemy.
   */
  private void drawEnemy(Graphics2D graphics, GameData data, Enemy enemy) {
  	var boundingBox = GameDataCombat.getEnemyBox().get(enemy);
  	int sizeX = boundingBox.southEast().x() - boundingBox.northWest().x();
  	int sizeY = boundingBox.southEast().y() - boundingBox.northWest().y();
		drawElement(graphics, data.imgMap().get(enemy.getImg()), boundingBox.northWest().x(), boundingBox.northWest().y(), sizeX, sizeY, Direction.UP);
		drawEnemyInfo(graphics, enemy, (int) boundingBox.northWest().x(), (int) (boundingBox.southEast().y()));
  }
  
  private void drawLog(Graphics2D graphics, GameData data, List<String> log) {
  	int i = 0;
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setColor(Color.WHITE);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    var iterator = log.iterator();
    while (iterator.hasNext()) {
    	var text = iterator.next();
    	int textWidth = fm.stringWidth(text);
    	double gap = fm.getAscent() * 1.5;
  	  graphics.drawString(text, width / 2 - textWidth / 2,	(int) (gap * i++ + height * 0.8));
    }
  }
  
  private void drawEndTurnButton(Graphics2D graphics, BufferedImage img) {
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_ENDTURN").transform(), null);
  }
  
  /**
   * Draws all the information about the enemy
   * 
   * @param graphics {@code Graphics2D} object for drawing.
   * @param enemy 	 Data of the enemy.
   * @param x				 Coordinate x where we wants to draw.
   * @param y				 Coordinate y where we wants to draw.
   */
  private void drawEnemyInfo(Graphics2D graphics, Enemy enemy, int x, int y) {
  	int size = FontLoader.getSpan();
    Font font = new Font("Arial", Font.PLAIN, size);
    graphics.setColor(GameDataCombat.getTarget() == enemy ? Color.RED : Color.WHITE);
		graphics.setFont(font);
	  graphics.drawString("PV : " + enemy.getHP(), x,	y + size);
	  graphics.drawString("SHIELD : " + String.valueOf(enemy.getShield()), x,	y + size*2);
	  graphics.drawString("ACTION : " + String.valueOf(enemy.getAction()), x,	y + size * 3);
	  var i = 0;
	  for (var effect : enemy.getEffects().keySet()) {
	  	i++;
	  	graphics.drawString(effect + " : " + enemy.getEffects().get(effect), x,	y + size * (3 + i));
	  }
  }
  
  /**
   * Draw an event and their choices
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private void drawEvent(Graphics2D graphics, GameData data) {
  	drawBgEvent(graphics, data);
  	drawTextEvent(graphics, data);
  	drawChoiceEvent(graphics, data);
  }
  
  /**
   * Draw the background image of the event
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private void drawBgEvent(Graphics2D graphics, GameData data) {
  	BufferedImage img = data.imgMap().get("BG_EVENT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_EVENT").transform(), null);
  }
  
  /**
   * Draw the text of the event in the screen
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private void drawTextEvent(Graphics2D graphics, GameData data){
	  double top = MathLoader.getMapEvent().get("BG_EVENT").box().northWest().y();
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
    graphics.setColor(Color.WHITE);
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    int textWidth = fm.stringWidth(data.event().getRoot().getQuestion());
		int x = width / 2 - textWidth / 2;
		int y = (int) (top * 1.05)  + fm.getAscent() ;
	  graphics.drawString(data.event().getRoot().getQuestion(), x,	y);
  }
  
  /**
   * Draw the background image of a choice<br>
   * Also called the fonction to write the choice inside.
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   */
  private void drawChoiceEvent(Graphics2D graphics, GameData data) {
  	var img1 = data.imgMap().get("BG_CHOICE1");
  	var img2 = data.imgMap().get("BG_CHOICE2");
  	var event1 = MathLoader.getMapEvent().get("BG_CHOICE1");
  	var event2 = MathLoader.getMapEvent().get("BG_CHOICE2");
  	int width = event1.box().southEast().x() - event1.box().northWest().x();
  	int height = event1.box().southEast().y() - event1.box().northWest().y();
  	if (data.event().getRoot().getChoice2() == null){
  		var img3 = data.imgMap().get("BG_CHOICE_END");
  		var event3 = MathLoader.getMapEvent().get("BG_CHOICE_END");
  		graphics.drawImage(img3, event3.transform(), null);
		  drawText(graphics, data, data.event().getRoot().getChoice1().getAnswer(), 
		  															(int) event3.box().northWest().x() + width / 2, 
		  															(int) (event3.box().northWest().y() + height * 0.5), 30);
  	}
  	else {
  		graphics.drawImage(img1, event1.transform(), null);
  	  drawText(graphics, data, data.event().getRoot().getChoice1().getAnswer(), 
  	  															(int) event1.box().northWest().x() + width / 2, 
  	  															(int) (event1.box().northWest().y() + height * 0.5), 30);
  		graphics.drawImage(img2, event2.transform(), null);
  	  drawText(graphics, data, data.event().getRoot().getChoice2().getAnswer(), 
  	  															(int) event2.box().northWest().x() + width / 2, 
  	  															(int) (event2.box().northWest().y() + height * 0.5), 30);
  	}
  }
  
  /**
   * Draw the text inside each choice. <br>
   * The text is draw at the center of the box choice.
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param data		 {@code GameData} containing all informations about the game
   * @param content	 Text we wants to write.
   * @param x				 The x coordinate of the text
   * @param y				 The y coordinate of the text
   * @param maxChar  Number of char max per line
   */
  private static void drawText(Graphics2D graphics, GameData data, String content, int x, int y, int maxChar) {
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    graphics.setColor(Color.WHITE);
    int maxCharsPerLine = maxChar;
    String[] words = content.split(" ");
    StringBuilder line = new StringBuilder();
    int lineCount = 0;
    for (String word : words) {
        if (line.length() + word.length() + 1 > maxCharsPerLine) {
            int lineWidth = fm.stringWidth(line.toString());
            graphics.drawString(line.toString(), x - lineWidth / 2, y + lineCount * fm.getHeight());
            line = new StringBuilder(word);
            lineCount++;
        } else {
            if (line.length() > 0) line.append(" ");
            line.append(word);
        }
    }
    if (line.length() > 0) {
        int lineWidth = fm.stringWidth(line.toString());
        graphics.drawString(line.toString(), x - lineWidth / 2, y + lineCount * fm.getHeight());
    }
  }
  
  public static void heroMove(GameData data, XY coordFinal, ApplicationContext context, GameView view) {
  	var bestWay = new ArrayList<XY>();
//  	IO.println("On va de : " + data.map().getHeroPos() + " dans " + coordFinal);
  	heroMove(data.map().getGrid(), data.map().getHeroPos(), coordFinal, new ArrayList<XY>(), bestWay, data.map());
//  	IO.println(bestWay.stream()
//  										.map(XY::toString)
//  										.collect(Collectors.joining(" -> ")));
  	for (var coord : bestWay) {
  		data.map().setHeroPos(coord);
  		GameView.draw(context, data, view);
  		try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
  	}
  }
  
  private static void heroMove(Room[][] grid, XY coordCurrent, XY coordFinal, List<XY> bestWay, List<XY> currentWay, Floor floor) {
//  	IO.println("Meilleur : " + bestWay);
  	if (coordCurrent.equals(coordFinal)) {
  		if (currentWay.size() <= bestWay.size() || bestWay.isEmpty()) {
  			bestWay.clear();
  			bestWay.addAll(currentWay);
  		}
  	} else {
  		for (var room : grid[coordCurrent.y()][coordCurrent.x()].getAccessible()) {
  			if (floor.getHeroAccessible().contains(room) || floor.getHeroVisited().contains(room)) {
  				if (!currentWay.contains(room)) {
      			var newCurrentWay = new ArrayList<>(currentWay);
      			newCurrentWay.add(room);
      			heroMove(grid, room, coordFinal, bestWay, newCurrentWay, floor);
      		}
  			}
    	}
  	}
  }
  
  private static void drawBinButton(Graphics2D graphics, GameData data) {
  	BufferedImage img = data.imgMap().get(data.getBin() ? "BG_BIN_OPEN" : "BG_BIN_CLOSE");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_BIN_CLOSE").transform(), null);
  }
  
  private static void drawShop(Graphics2D graphics, GameData data) {
  	BufferedImage img = data.imgMap().get("BG_SHOP");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_SHOP").transform(), null);
    drawTextBubble(graphics, data);
    drawArticleBubble(graphics, data);
    drawButtonShop(graphics, data);
    drawSellArticle(graphics, data);
  	img = data.imgMap().get("RolandBody");
    graphics.drawImage(img, MathLoader.getMapEvent().get("RolandBody").transform(), null);
    img = data.imgMap().get("ICON_EXIT_SHOP");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_EXIT_SHOP").transform(), null);
  }
  
  private static void drawTextBubble(Graphics2D graphics, GameData data) {
    var bubbleBox = MathLoader.getMapEvent().get("BG_SHOP_BUBBLE").box();
    int centerX = (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2 ;
    int centerY = (bubbleBox.southEast().y() - bubbleBox.northWest().y()) / 2;
    int x = bubbleBox.northWest().x() + centerX;
    int y = bubbleBox.northWest().y() + centerY;
    drawText(graphics, data, "Bienvenues au shop mon frère", x, y, 23);
  }
  
  private static void drawArticleBubble(Graphics2D graphics, GameData data) {
    var bubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE").box();
    var width = bubbleBox.southEast().x() - bubbleBox.northWest().x();
    var height = bubbleBox.southEast().y() - bubbleBox.northWest().y();
    graphics.drawRect(bubbleBox.northWest().x(), bubbleBox.northWest().y(), width, height);
  }
  
  private static void drawButtonShop(Graphics2D graphics, GameData data) {
  	var img = data.imgMap().get("ICON_SHOP_LEFT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_LEFT").transform(), null);
    img = data.imgMap().get("ICON_SHOP_RIGHT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_RIGHT").transform(), null);
    img = data.imgMap().get("ICON_SHOP_BUY");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_BUY").transform(), null);
  }
  
  private static void drawSellArticle(Graphics2D graphics, GameData data) {
    var bubbleBox = MathLoader.getMapEvent().get("SHOP_SELL_ARTICLE").box();
    var width = bubbleBox.southEast().x() - bubbleBox.northWest().x();
    var height = bubbleBox.southEast().y() - bubbleBox.northWest().y();
    graphics.drawRect(bubbleBox.northWest().x(), bubbleBox.northWest().y(), width, height);
  }
  
  /**
   * Methods for drawing the game 
   * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
   */
  private void draw(Graphics2D graphics, GameData data) {
		drawBG(graphics, data);
		if (data.mapOrBag()) {
			drawGrid(graphics, data);
			drawItemBag(graphics, data);
			drawItemInfo(graphics, data);
			drawBinButton(graphics, data);
			if (data.getShop()) {
				drawShop(graphics, data);
			}
			if(!GameDataClick.getDragItemMap().isEmpty()) {
				updateDragItem(graphics, data);
			}
			
		} else {
			drawMap(graphics, data);
		}
		drawHero(graphics, data);
		drawButton(graphics, data);
		// Draw enemy if we're in combat
		if (GameDataCombat.combat()) {
			updateCombat(graphics, data, GameDataCombat.getLstEnemy());
		}

		if (data.event() != null) {
			drawEvent(graphics, data);	
		}
 }
  
  public static void draw(ApplicationContext context, GameData data, GameView view) {
		context.renderFrame(graphics -> view.draw(graphics, data));
	}
}
