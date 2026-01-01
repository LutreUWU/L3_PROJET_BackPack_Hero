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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.github.forax.zen.ApplicationContext;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import loader.FontLoader;
import loader.ImageLoader;
import loader.MathLoader;
import model.BoundingBox;
import model.Curse;
import model.Direction;
import model.Effect;
import model.Item;
import model.XY;
import model.item.common.Arrow;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.common.Sword;
import model.item.epic.Bow;
import model.item.epic.DespairShield;
import model.item.epic.Shield;
import model.item.legendary.Axe;
import model.item.mythic.Mimicry;
import model.item.rare.FireBall;
import model.item.rare.Gant;
import model.item.rare.ManaStone;
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
public record GameView(int width, int height, int tileSize, ImageLoader imgLoader) {	
  /**
   * Create a new GameView
   * 
   * @param width    	Width of the windows screen
   * @param height	Height of the windows screen
   * @param grid_size Size of a grid in the bag
   * @return SimpleGameView
   */
  public static GameView initGameGraphics(int width, int height, int grid_size, ImageLoader imgLoader) {
  	return new GameView(width, height, grid_size, imgLoader);
  }
  
  /**
	 * Draw the background of the game
	 * 
   * @param context		{@code ApplicationContext} of the game.
   * @param data			GameData containing the game data. 
	 */
	private void drawBG(Graphics2D graphics, GameData data) {
//		graphics.setColor(Color.gray);
//		graphics.fillRect(0, 0, width, height);
		BufferedImage img = imgLoader.bgImages().get("BG1");
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
   * Draw an image but with a specifity.
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * 
   * But since we're using .png image, it can happens that the center of the image is empty.
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * 
   * We're adding two new parameters marginX and marginY to help drawing this item.
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
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
   * Draw an item in the bag but with a specifity.
   * Initially, the other method {@code drawElement} draw the element base of the center of the image.
   * 
   * But since we're using .png image, it can happens that the center of the image is empty.
   * In consequence, we need to change the "center" of the image to draw properly the image.
   * 
   * We're adding two new parameters marginX and marginY to help drawing this item.
   * For example, if we wants the center to be at the left center of the image, marginX = 0 and marginY = 0.5 
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
		BufferedImage imgBackpack = imgLoader.bgImages().get("BG_BACKPACK");
		BoundingBox boundingBox = MathLoader.getMapEvent().get("BG_BACKPACK").box(); 
		graphics.drawImage(imgBackpack, MathLoader.getMapEvent().get("BG_BACKPACK").transform(), null);
		for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 7; j++) {
	    	final int fi = i;
	    	final int fj = j;				  
		  	if (grid[fi][fj] >= -1) {
		  		graphics.drawImage(imgLoader.bgImages().get("BG_BAG_UNLOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  		
		  	}
		  	if (grid[fi][fj] == -2) {
		  		graphics.drawImage(imgLoader.bgImages().get("BG_BAG_LOCK"), boundingBox.northWest().x() + (size * fj), boundingBox.northWest().y() + (size * fi), size, size, null);
		  	}
			}
    }
  }
  
  /**
   * Check the id int the bag we wants to draw and calls the appropriate method.
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
			  case 1 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.75);
			  case 2 -> drawItemGold(graphics, data, coordinate, item);
				case 3 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
				case 4 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.25, 0.25); 
				case 5 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
				case 6 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
				case 7 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.75); 
				case 8 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 2, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.20, 0.5); 
				case 9 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
				case 10 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.25, 0.25);
				case 11 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
				case 12 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
				case 13 -> drawInBagSpecial(graphics, new XY(coordinate.x() - 1, coordinate.y()), 3, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.2); 
				case 14, 15, 16 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
				default ->{}
		  }
		}
  }
  
  /**
   * Draw gold item in bag.
   * Since gold has a specifical visual depending of his amount, 
   * we did a separated method for that.
   * 
   * @param graphics 		{@Code Graphics2D} of the game
   * @param data	   		Data of the game
   * @param coordinate	coordinate XY of where the gold is in the bag
   * @param item				{@Code Item} containing the amount of gold
   */
  private void drawItemGold(Graphics2D graphics, GameData data, XY coordinate, Item item) {
  	var itemGold = (Gold) item;
  	switch (getSizeGold(itemGold.value())) {
  	case 3 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.bgImages().get("gold3"));
  	case 2 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.bgImages().get("gold2"));
  	default -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.bgImages().get("gold1"));
  	}
  }
  
  
  /**
   * Get the description of each item in the game.
   * 
   * @param item {@code Item} we wants to get the description
   * @return String description of the item
   */
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
	  	case Bomb _ -> "Bombe volée à Mario";
	  	case Curse _ -> "Malédiction que t'a jeté un ennemi";
	  	case Shield _ -> "(EFFET PASSIF) Bouclier de Captain pas America";
	  	case FireBall _ -> "Ca chauffe !!";
	  	case ManaStone m -> "Le mana se propage entre les éléments conduteurs (ceux avec du métal)";
	  	default -> throw new IllegalArgumentException("Unexpected value: " + item.ID());
  	};
  }
  
  /**
   * Get the effect of each item in the game.
   * 
   * @param item {@code Item} we wants to get the effect
   * @return String effect of the item
   */
  private String getEffectItem(Item item) {
  	return switch (item) {
	  	case KeyDoor _ -> "Déverouille une porte (1 fois)"; 
	  	case Gold g -> "Il y a " + g.value() + "gold";
	  	case Sword _ -> "Inflige -3 à l'ennemi";
	  	case DespairShield _ ->  "Se met 10 Shield en échange de 3PV";
	  	case Mimicry _ -> "-30 PV en échange de 5PV";
	  	case Massue _ -> "Inflige -5PV à l'ennemi";
	  	case Gant _ -> "Régénère 10 PV";
	  	case Axe _ -> "Inflige -10PV à l'ennemi";
	  	case Arrow _ -> "Inflige -8PV à l'ennemi";
	  	case PoisonArrow _ -> "Inflige -6PV à l'ennemi et empoisonne l'ennemi";
	  	case Bow _ -> "Inflige -8PV à l'ennemi";
	  	case Bomb _ -> "Inflige -6PV à tous les ennemies + 1PV par bombe qui l'entoure";
	  	case Curse _ -> "Utilise la malédiction pour t'en débarasser";
	  	case Shield _ -> "Te donne 3 shield par tour si tu l'as placé à la prmeière ligne, 1 sinon";
	  	case FireBall _ -> "Inflige -6PV à l'ennemi et enflamme l'ennemi";
	  	case ManaStone m -> "Cette pierre contient " + m.value() + " mana !";
	  	default -> throw new IllegalArgumentException("Unexpected value: " + item.ID());
  	};
  }
  
  /**
   * Display information about the item
   * 
   * @param graphics
   * @param data
   */
  private void drawItemInfo(Graphics2D graphics, GameData data) {
  	BufferedImage imgItemInfo = imgLoader.bgImages().get("BG_INFO_ITEM");
  	BoundingBox itemInfoBoundingBox = MathLoader.getMapEvent().get("BG_INFO_ITEM").box();
  	XY NW = itemInfoBoundingBox.northWest();
  	XY SE = itemInfoBoundingBox.southEast();
		graphics.drawImage(imgItemInfo, MathLoader.getMapEvent().get("BG_INFO_ITEM").transform(), null);
		Item item = data.dragItem();
		if (item == null) {
			if (data.getShop() && !data.getShopLst().getCurrentShop().isEmpty()) {
				item = data.getShopLst().getCurrentShop().keySet().iterator().next();
			}
		}
		if (GameDataCombat.combat()) {
			item = GameDataCombat.getHoverItem();
		}
		int height = SE.y() - NW.y();
		if (item != null) {
	    drawTextInfoName(graphics, item, NW);
	    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
	    graphics.setFont(font);
	    FontMetrics fm = graphics.getFontMetrics();
	    graphics.setColor(Color.WHITE);
		  drawTextInfo(graphics, getDescriptionItem(item), NW.x(), NW.y() + (int) (NW.y() * 0.30), 25);
		  graphics.setColor(Color.GREEN);
		  drawTextInfo(graphics, "AP : " + item.AP(), NW.x(), NW.y() + height / 3, 20);
		  drawTextInfo(graphics, "Durability : " + item.durability(), NW.x(), NW.y() + height / 3 + (int) (fm.getAscent() * 1.5), 20);
		  graphics.setColor(Color.WHITE);
		  drawTextInfo(graphics, getEffectItem(item), NW.x(), NW.y() + height / 3 + (int) (fm.getAscent() * 4), 23);
		}
  }
  
  private void drawTextInfoName(Graphics2D graphics, Item item, XY NW) {
  	Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
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
    FontMetrics fm = graphics.getFontMetrics();
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
  		BufferedImage img = imgLoader.bgImages().get("Roland");
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
    BufferedImage img = imgLoader.bgImages().get("ICON_HEALTH");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2 , width * 0.20, size));
    graphics.setColor(Color.GREEN);
    graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20 * data.hero().getHP() / data.hero().getMaxHP(), size));
    graphics.setColor(Color.WHITE);
    graphics.draw(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20, size));
	  graphics.drawString(data.hero().getHP() + "/" + data.hero().getMaxHP(), (int) (logoWidth + width * 0.205), logoHeight / 2 + size / 2);
  }
  
  private void drawHeroShield(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_SHIELD");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = imgLoader.bgImages().get("ICON_SHIELD");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.GRAY);
    graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
    graphics.setColor(Color.BLUE);
    graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20 * data.hero().getCurrentProtection() / data.hero().getMaxHP(), size));
    graphics.setColor(Color.WHITE);
    graphics.draw(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
	  graphics.drawString(data.hero().getCurrentProtection() + "/" + data.hero().getMaxHP(), (int) (logoWidth + width * 0.205), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroMana(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_MANA");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = imgLoader.bgImages().get("ICON_MANA");
    graphics.drawImage(img, render.transform() , null);
	  graphics.setColor(Color.CYAN);
	  graphics.drawString(data.hero().getManaPoint() + " MANA" , (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroAction(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_ACTION");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = imgLoader.bgImages().get("ICON_ACTION");
    graphics.drawImage(img, render.transform() , null);
    graphics.setColor(data.hero().getEnergyPoint() > 1 ? Color.YELLOW : Color.RED);
	  graphics.drawString(data.hero().getEnergyPoint() + " AP", (int) (logoWidth + width * 0.005), (int) (render.box().northWest().y() + logoHeight / 2 + size / 2));
  }
  
  private void drawHeroUnlock(Graphics2D graphics, GameData data) {
  	var render = MathLoader.getMapEvent().get("ICON_UNLOCK");
  	int logoWidth = render.box().southEast().x() - render.box().northWest().x(); 	
  	int logoHeight = (int) (height * 0.04);
  	int size = (int) (height * 0.03);
    Font font = new Font("Mikodacs", Font.PLAIN, size);
    graphics.setFont(font);
    BufferedImage img = imgLoader.bgImages().get("ICON_UNLOCK");
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
    BufferedImage img = imgLoader.bgImages().get("gold1");
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
    graphics.fill(new Rectangle2D.Double(width / 2 - textWidth / 2, (int) (height * 0.03)  , textWidth * data.hero().getXp() / data.hero().maxXP(), size * 0.25));
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
  	BufferedImage imgMap = imgLoader.bgImages().get("BG_MAP");
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
		  		graphics.drawImage(imgLoader.bgImages().get("BG_MAP_TILE"), newX, newY, tileSize, tileSize, null);
			    if (data.map().getHeroAccessible().contains(coordXY)) {
			    	graphics.drawImage(imgLoader.bgImages().get("BG_MAP_TILE_ACCES"), newX, newY, tileSize, tileSize, null);
			    }
			  	switch(data.map().getGrid()[fi][fj]) {
			  		case Shop _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_SHOP"), newX, newY, tileSize, tileSize, null);
			  		case EnemyRoom _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_COMBAT"), newX, newY, tileSize, tileSize, null);
			  		case EventRoom _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_EVENT"), newX, newY, tileSize, tileSize, null);
			  		case Healer _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_HEAL"), newX, newY, tileSize, tileSize, null);
			  		case Start _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_START"), newX, newY, tileSize, tileSize, null);
			  		case Exit _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_EXIT"), newX, newY, tileSize, tileSize, null);
			  		case LockedDoor _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_LOCK_DOOR"), newX, newY, tileSize, tileSize, null);
			  		case Treasure _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_TREASURE"), newX, newY, tileSize, tileSize, null);
			  		default ->  {}
			  	}
		  	} else graphics.drawImage(imgLoader.bgImages().get("BG_MAP_SHADOW"), newX - (int) gap, newY - (int) gap, tileSize + (int) gap*2, tileSize + (int) gap*2, null);
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
		graphics.drawImage(imgLoader.bgImages().get("ICON_HERO"), (int) (gap * coord.x()) + leftGrid.northWest().x() + (coord.x() * tileSize), (int) (gap * coord.y()) + leftGrid.northWest().y() + (coord.y() * tileSize), tileSize, tileSize, null);
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
		  case 1 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.75); 
		  case 2 -> drawDragGold(graphics, data, item, box);
			case 3 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
			case 4 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.25, 0.25); 
			case 5 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
			case 6 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID())); 
			case 7 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.75); 
			case 8 -> drawDragSpecialItem(graphics, box.northWest(), 2, 3, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.20, 0.5); 
			case 9 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
			case 10 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.25, 0.25); 
			case 11 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
			case 12 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
			case 13 -> drawDragSpecialItem(graphics, box.northWest(), 3, 2, item.direction(), imgLoader.itemImagesByID().get(item.ID()), 0.5, 0.2); 
			case 14, 15, 16 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.itemImagesByID().get(item.ID()));
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
  	case 3 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.bgImages().get("gold3"));
  	case 2 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.bgImages().get("gold2")); 
  	default -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.bgImages().get("gold1"));
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
   * @param list List of all enemy we fight
   */
  private void updateCombat(Graphics2D graphics, GameData data,  List<Enemy> list) {
  	drawEndTurnButton(graphics,  imgLoader.bgImages().get("BG_ENDTURN"));
  	list.forEach(enemy -> drawEnemy(graphics, data, enemy));
  	if (GameDataCombat.getHoverItem() != null) {
  		drawSelectedItem(graphics, GameDataCombat.getHoverItem());
  	}
  	drawLog(graphics, data, GameDataCombat.getLog());
  }
  
  /**
   * Show to the user, the item he selected.
   * 
   * @param graphics  {@code graphics} of the game.
   * @param hoverItem {@code Item} selected.
   */
  private void drawSelectedItem(Graphics2D graphics, Item hoverItem) {
  	String sentence = "Vous avez sélectionné " + hoverItem.toString();
  	Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    int textWidth = fm.stringWidth(sentence);
    int textHeight = fm.getAscent();
    int y = MathLoader.getMapEvent().get("BG_BIN_OPEN").box().northWest().y();
    graphics.setColor(Color.BLACK);
	  graphics.drawString(sentence, width / 2 - textWidth / 2, y + (int) (textHeight*0.25));
    graphics.setColor(Color.WHITE);
	  graphics.drawString(sentence, width / 2 - textWidth / 2, y);
	}

	/**
   * Draws the enemy in the windows
   * 
   * @param graphics {@code graphics} of the game.
   * @param data     GameData containing the game data. 
   * @param enemy 	 Data of the enemy.
   */
  private void drawEnemy(Graphics2D graphics, GameData data, Enemy enemy) {
  	var imgName = enemy.getInfo().img();
  	var boundingBox = GameDataCombat.getEnemyBox().get(enemy);
  	int sizeX = boundingBox.southEast().x() - boundingBox.northWest().x();
  	int sizeY = boundingBox.southEast().y() - boundingBox.northWest().y();
  	drawEnemyArc(graphics, (int) boundingBox.northWest().x(), (int) (boundingBox.southEast().y()), sizeX, sizeY, enemy);
		drawElement(graphics, imgLoader.bgImages().get(imgName), boundingBox.northWest().x(), boundingBox.northWest().y(), sizeX, sizeY, Direction.UP);
		drawEnemyInfo(graphics, enemy, boundingBox, sizeX, sizeY);
  }
  
  private void drawLog(Graphics2D graphics, GameData data, List<String> log) {
  	int i = 0;
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setFont(font);
    graphics.setColor(Color.WHITE);
    FontMetrics fm = graphics.getFontMetrics();
  	double gap = fm.getAscent() * 1.5;
    var iterator = log.reversed().iterator();
    while (iterator.hasNext()) {
    	var text = iterator.next();
  	  graphics.drawString("-> " + text, (int) gap ,	(int) (height - gap - gap * i++));
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
  private void drawEnemyInfo(Graphics2D graphics, Enemy enemy, BoundingBox boundingBox, int width, int height) {
  	int size = FontLoader.getH1();
  	drawEnemyPV(graphics, boundingBox.northWest().x(), boundingBox.southEast().y(), width, height, enemy, size);
  	drawEnemyShield(graphics, boundingBox.southEast().x(), boundingBox.southEast().y(), width, height, enemy, size);
  	drawEnemyAction(graphics, boundingBox, enemy);
		drawEnemyEffect(graphics, enemy, boundingBox);
  }
  
  private void drawEnemyEffect(Graphics2D graphics, Enemy enemy, BoundingBox boundingBox) {
		var width = boundingBox.southEast().x() - boundingBox.northWest().x();
		var centerX = boundingBox.northWest().x() + width / 2;
		int size = (int) (height * 0.04);
		int gap = (int) (size * 0.1);
  	var i = 0;
  	for (var effect : enemy.getEffects().keySet()) {
	    int offset = (i + 1) / 2;
	    int dir = (i % 2 == 0) ? 1 : -1;
	    int pos = offset * dir;
	    int x = centerX + pos * (size + gap);
	    drawEnemyEffectImageAndValue(graphics, enemy, effect, x, (int) (boundingBox.southEast().y() * 1.08), size);
	    i++;
  	}
	}

	private void drawEnemyEffectImageAndValue(Graphics2D graphics, Enemy enemy, Effect effect, int x, int y, int size) {
		var img = switch(effect) {
		case POISON -> imgLoader.bgImages().get("ICON_POISON");
		case FIRE -> imgLoader.bgImages().get("ICON_BURN");
		default -> throw new IllegalArgumentException("This is not an effect : " + effect);
		};
		drawElement(graphics, img, x, y, size, size, Direction.UP); 
		var charNumber = Integer.toString(enemy.getEffects().get(effect));
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    FontMetrics fm = graphics.getFontMetrics();
 		graphics.setFont(font);
 		graphics.setColor(Color.BLACK);
    graphics.drawString(charNumber, x + fm.stringWidth(charNumber)/2, (int) (y + size * 1.05));
 		graphics.setColor(Color.WHITE);
    graphics.drawString(charNumber, x + fm.stringWidth(charNumber)/2, y + size);

	}

	private void drawEnemyAction(Graphics2D graphics, BoundingBox boundingBox, Enemy enemy) {
		var width = boundingBox.southEast().x() - boundingBox.northWest().x();
		Font font = new Font("Mikodacs", Font.ITALIC, FontLoader.getH3());
    FontMetrics fm = graphics.getFontMetrics();
 		graphics.setFont(font);
		var centerX = boundingBox.northWest().x() + width / 2;
		var centerY = boundingBox.northWest().y() - fm.getAscent() / 2;
		graphics.setColor(Color.BLACK);
  	drawText(graphics, "Action : " + enemy.getAction(), centerX, (int) (centerY * 1.01), 20);	
 		graphics.setColor(Color.LIGHT_GRAY);
  	drawText(graphics, "Action : " + enemy.getAction(), centerX, centerY, 20);	
  	font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH2());
    fm = graphics.getFontMetrics();
    graphics.setFont(font);
		graphics.setColor(Color.BLACK);
    centerY = boundingBox.northWest().y() - fm.getAscent() * 3;
  	drawText(graphics, enemy.toString(), centerX, (int) (centerY * 1.01), 20);	
 		graphics.setColor(GameDataCombat.getTarget() == enemy ? Color.RED : Color.WHITE);
  	drawText(graphics, enemy.toString(), centerX, centerY, 20);	
	}

	private void drawEnemyPV(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy, int size) {
  	// Text shadow
  	Font font = new Font("Mikodacs", Font.PLAIN, size + 4);
 		graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    graphics.setColor(Color.BLACK);
 	  graphics.drawString(Integer.toString(enemy.getHP()), (int) (x - fm.stringWidth(Integer.toString(enemy.getHP()))/ 2 + 1),	(int) (y - height/4 + 4));
    // PV
 	  font = new Font("Mikodacs", Font.PLAIN, size);
 		graphics.setFont(font);
    fm = graphics.getFontMetrics();
 	  graphics.setColor(Color.WHITE);
 	  graphics.drawString(Integer.toString(enemy.getHP()), (int) (x - fm.stringWidth(Integer.toString(enemy.getHP()))/ 2),	(int) (y - height/4));
  }
  
  private void drawEnemyShield(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy, int size) {
  	// Text shadow
  	Font font = new Font("Mikodacs", Font.PLAIN, size + 4);
 		graphics.setFont(font);
    FontMetrics fm = graphics.getFontMetrics();
    graphics.setColor(Color.BLACK);
 	  graphics.drawString(Integer.toString(enemy.getShield()), (int) (x - fm.stringWidth(Integer.toString(enemy.getShield()))/ 2 - 1),	(int) (y - height/4 + 4));
    // PV
 	  font = new Font("Mikodacs", Font.PLAIN, size);
 		graphics.setFont(font);
    fm = graphics.getFontMetrics();
 	  graphics.setColor(Color.BLUE);
 	  graphics.drawString(Integer.toString(enemy.getShield()), (int) (x - fm.stringWidth(Integer.toString(enemy.getShield()))/ 2 - 1),	(int) (y - height/4));
  }
  
  private void drawEnemyArc(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy) {  
  	graphics.setStroke(new BasicStroke(38));
  	graphics.setColor(Color.BLACK);
  	graphics.drawArc(x, (int) (y - (height / 2* 1.5)/2), width, height / 2, 0, -180);
  	graphics.setStroke(new BasicStroke(30));
  	graphics.setColor(Color.GRAY);
  	graphics.drawArc(x, (int) (y - (height / 2* 1.5)/2), width, height / 2, 0, -180);
    graphics.setColor(Color.RED);
  	double percent = (double)enemy.getHP() / (double) enemy.getInfo().maxHP(); 
  	int arcAngle = (int) (180 * percent);
  	graphics.drawArc(x, (int) (y - (height / 2* 1.5)/2), width, height / 2, 0, -arcAngle);
  	graphics.setStroke(new BasicStroke(1));
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
  	BufferedImage img = imgLoader.bgImages().get("BG_EVENT");
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
  	var img1 = imgLoader.bgImages().get("BG_CHOICE1");
  	var img2 = imgLoader.bgImages().get("BG_CHOICE2");
  	var event1 = MathLoader.getMapEvent().get("BG_CHOICE1");
  	var event2 = MathLoader.getMapEvent().get("BG_CHOICE2");
  	int width = event1.box().southEast().x() - event1.box().northWest().x();
  	int height = event1.box().southEast().y() - event1.box().northWest().y();
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setFont(font);
  	if (data.event().getRoot().getChoice2() == null){
  		var img3 = imgLoader.bgImages().get("BG_CHOICE_END");
  		var event3 = MathLoader.getMapEvent().get("BG_CHOICE_END");
  		graphics.drawImage(img3, event3.transform(), null);
		  drawText(graphics, data.event().getRoot().getChoice1().getAnswer(), 
		  															(int) event3.box().northWest().x() + width / 2, 
		  															(int) (event3.box().northWest().y() + height * 0.5), 30);
  	}
  	else {
  		graphics.drawImage(img1, event1.transform(), null);
  	  drawText(graphics, data.event().getRoot().getChoice1().getAnswer(), 
  	  															(int) event1.box().northWest().x() + width / 2, 
  	  															(int) (event1.box().northWest().y() + height * 0.5), 30);
  		graphics.drawImage(img2, event2.transform(), null);
  	  drawText(graphics, data.event().getRoot().getChoice2().getAnswer(), 
  	  															(int) event2.box().northWest().x() + width / 2, 
  	  															(int) (event2.box().northWest().y() + height * 0.5), 30);
  	}
  }
  
  /**
   * Draw the text inside each choice. <br>
   * The text is draw at the center of the box choice.
   * 
   * @param graphics {@code Graphics2D} of the game.
   * @param content	 Text we wants to write.
   * @param x				 The x coordinate center of the text
   * @param y				 The y coordinate center of the text
   * @param maxChar  Number of char max per line
   */
  private void drawText(Graphics2D g, String content, int x, int y, int maxCharsPerLine) {
    FontMetrics fm = g.getFontMetrics();
    String[] words = content.split(" ");
    List<String> lines = new ArrayList<>();
    StringBuilder currentLine = new StringBuilder();
    for (String word : words) {
        if (currentLine.length() + word.length() + 1 > maxCharsPerLine) {
            lines.add(currentLine.toString());
            currentLine = new StringBuilder(word);
        } else {
            if (currentLine.length() > 0) currentLine.append(" ");
            currentLine.append(word);
        }
    }
    if (currentLine.length() > 0) {
        lines.add(currentLine.toString());
    }
    int totalHeight = lines.size() * fm.getAscent();
    int startY = y + totalHeight / 2;
    // Dessin centré
    for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(lines.size() - 1 - i );
        int lineWidth = fm.stringWidth(line);
        g.drawString(line, x - lineWidth / 2, startY - i * fm.getAscent());
    }
}
  
  public void heroMove(GameData data, XY coordFinal, ApplicationContext context, GameView view) {
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
  
  private void heroMove(Room[][] grid, XY coordCurrent, XY coordFinal, List<XY> bestWay, List<XY> currentWay, Floor floor) {
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
  
  private void drawBinButton(Graphics2D graphics, GameData data) {
  	BufferedImage img = imgLoader.bgImages().get(data.getBin() ? "BG_BIN_OPEN" : "BG_BIN_CLOSE");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_BIN_CLOSE").transform(), null);
  }
  
  private void drawShop(Graphics2D graphics, GameData data) {
  	BufferedImage img = imgLoader.bgImages().get("BG_SHOP");
    graphics.drawImage(img, MathLoader.getMapEvent().get("BG_SHOP").transform(), null);
    img = imgLoader.bgImages().get("RolandBody");
    graphics.drawImage(img, MathLoader.getMapEvent().get("RolandBody").transform(), null);
    img = imgLoader.bgImages().get("ICON_EXIT_SHOP");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_EXIT_SHOP").transform(), null);
    drawTextBubble(graphics, data);
    if (data.getShopLst().getCurrentShop().isEmpty()) {
    	drawNoItemShop(graphics, data);
    }
    else {
      drawArticle(graphics, data);
    }  	
    drawButtonShop(graphics, data);
    drawSellArticle(graphics, data);
  }
  
  private void drawTextBubble(Graphics2D graphics, GameData data) {
    var bubbleBox = MathLoader.getMapEvent().get("BG_SHOP_BUBBLE").box();
    int centerX = (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2 ;
    int centerY = (bubbleBox.southEast().y() - bubbleBox.northWest().y()) / 2;
    int x = bubbleBox.northWest().x() + centerX;
    int y = bubbleBox.northWest().y() + centerY;
    graphics.setColor(Color.WHITE);
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
    graphics.setFont(font);
    drawText(graphics, data.getShopLst().getLogShop(), x, y, 30);
  }
  
  private void drawNoItemShop(Graphics2D graphics, GameData data) {
  	var img = imgLoader.bgImages().get("ICON_SOLDOUT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SOLDOUT").transform(), null);
    Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
    graphics.setFont(font);
    graphics.setColor(Color.WHITE);
    var titleBubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_NAME_HOLDER").box();
    int centerX = (titleBubbleBox.southEast().x() - titleBubbleBox.northWest().x()) / 2;
    int centerY = (titleBubbleBox.southEast().y() - titleBubbleBox.northWest().y()) / 2;
    drawText(graphics, "Y A PLUS RIEN", titleBubbleBox.northWest().x() + centerX, titleBubbleBox.northWest().y() + centerY, 15);
  }
  
  private void drawButtonShop(Graphics2D graphics, GameData data) {
  	var img = imgLoader.bgImages().get("ICON_SHOP_LEFT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_LEFT").transform(), null);
    img = imgLoader.bgImages().get("ICON_SHOP_RIGHT");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_RIGHT").transform(), null);
    img = imgLoader.bgImages().get("ICON_SHOP_BUY");
    graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_BUY").transform(), null);
  }
  
  private void drawSellArticle(Graphics2D graphics, GameData data) {
    var img = imgLoader.bgImages().get("ICON_SELL_BUTTON");
    graphics.drawImage(img, MathLoader.getMapEvent().get("SHOP_SELL_ARTICLE").transform(), null);
  }
  
  private void drawArticle(Graphics2D graphics, GameData data) {
  	Iterator<Map.Entry<Item, Integer>> it = data.getShopLst().getCurrentShop().entrySet().iterator();
  	Map.Entry<Item, Integer> entry = it.next();
  	Item item = entry.getKey();
  	int price = entry.getValue();
    var imageBubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_IMAGE_HOLDER").box();
		if (imgLoader.itemImagesByID().get(item.ID()) == null) {
			throw new IllegalArgumentException("Can't fint img : " + item.toString());
		}
    drawItemShopImage(graphics, data, imgLoader.itemImagesByID().get(item.ID()), imageBubbleBox);
    var titleBubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_NAME_HOLDER").box();
    drawItemShopName(graphics, item, titleBubbleBox);
    drawItemShopInfo(graphics, item, price, data.getShopLst().getCurrentShop().size());    
  }
  
  private void drawItemShopImage(Graphics2D graphics, GameData data, BufferedImage img, BoundingBox bubbleBox) {
  	XY northWest = bubbleBox.northWest();
  	XY southEast = bubbleBox.southEast();
  	double width = southEast.x() - northWest.x();
  	double height = southEast.y() - northWest.y();
		drawElement(graphics, img, northWest.x(), northWest.y(), width, height, Direction.UP);
	}
  
  private void drawItemShopName(Graphics2D graphics, Item item, BoundingBox titleBubbleBox) {
  	Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
    graphics.setFont(font);
    graphics.setColor(switch(item.rarity()) {
	    case COMMON -> Color.GRAY;
	    case RARE -> Color.GREEN;
	    case SUPERARE -> Color.BLUE;
	    case EPIC -> Color.MAGENTA;
	    case LEGENDARY -> Color.YELLOW;
	    case MYTHIC -> Color.PINK;
    });
    int centerX = (titleBubbleBox.southEast().x() - titleBubbleBox.northWest().x()) / 2;
    int centerY = (titleBubbleBox.southEast().y() - titleBubbleBox.northWest().y()) / 2;
    drawText(graphics, item.toString().toUpperCase(), titleBubbleBox.northWest().x() + centerX, titleBubbleBox.northWest().y() + centerY, 10);
  }
  
  private void drawItemShopInfo(Graphics2D graphics, Item item, int price, int nbItem) {
  	var bubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_IMAGE_HOLDER").box();
  	Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
  	graphics.setFont(font);
  	graphics.setColor(Color.BLACK);
  	FontMetrics fm = graphics.getFontMetrics();
  	int centerX = bubbleBox.northWest().x() + (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2; 
  	int centerY = bubbleBox.southEast().y() - fm.getAscent() / 2; 
    drawText(graphics, "Price : " + price + " Golds", centerX, centerY + (int) (fm.getAscent()*0.2), 20);
  	graphics.setColor(Color.YELLOW);
    drawText(graphics, "Price : " + price + " Golds", centerX, centerY, 20);
  	bubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_NAME_HOLDER").box();
  	centerX = bubbleBox.northWest().x() + (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2; 
  	centerY = bubbleBox.northWest().y(); 
  	graphics.setColor(Color.WHITE);
    drawText(graphics, "Items restants : " + nbItem, centerX, centerY - fm.getAscent()/2, 20);
  }
  
  private void drawMana(Graphics2D graphics, GameData data) {
  	var lstMana = data.bag().getManaStone(data);
  	if (!lstMana.isEmpty()) {
  		var manaConductive = data.bag().getManaConnectedCoords(lstMana);
  		///                   
  	}
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
			drawMana(graphics, data);
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
