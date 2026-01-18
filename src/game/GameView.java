package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import model.item.epic.EnchantedDiamondSword;
import model.item.epic.Shield;
import model.item.legendary.Axe;
import model.item.mythic.Mimicry;
import model.item.rare.Cookie;
import model.item.rare.FireBall;
import model.item.rare.Gant;
import model.item.rare.ManaStone;
import model.item.rare.PoisonArrow;
import model.item.superrare.Bomb;
import model.item.superrare.Massue;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Shop;
import model.map.Start;
import model.map.Treasure;
import model.monster.Enemy;

/**
 * The SimpleGameView class deals with the display of the game the screen, and
 * with the interpretation of which zones were clicked on by the user.
 * 
 * @param width     Width of the windows screen
 * @param height    Height of the windows screen
 * @param tileSize  Size of a grid in the bag
 * @param imgLoader List of all image in the game
 */
public record GameView(int width, int height, int tileSize, ImageLoader imgLoader) {
	/**
	 * Create a new GameView
	 * 
	 * @param width     Width of the windows screen
	 * @param height    Height of the windows screen
	 * @param gridSize Size of a grid in the bag
	 * @param imgLoader Class containing all image for the game
	 * 
	 * @return SimpleGameView
	 */
	public static GameView initGameGraphics(int width, int height, int gridSize, ImageLoader imgLoader) {
		if (width <= 0 || height <= 0 || gridSize <= 0) {
			throw new IllegalArgumentException("width <= 0 || height <= 0 || gridSize <= 0");
		}
		Objects.requireNonNull(imgLoader);
		return new GameView(width, height, gridSize, imgLoader);
	}

	/**
	 * Draw the background of the game
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 */
	private void drawBG(Graphics2D graphics, GameData data) {
		graphics.setColor(Color.gray);
		graphics.fillRect(0, 0, width, height);
		BufferedImage img = imgLoader.bgImages().get(data.getBG());
		graphics.drawImage(img, MathLoader.getMapEvent().get(data.getBG()).transform(), null);
	}

	/**
	 * Draw an image in the window
	 * 
	 * @param graphics  {@code Graphics2D} of the game
	 * @param img       Image we wants to put
	 * @param x         The x coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param y         The y coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param dimX      Width of the image
	 * @param dimY      Height of the image
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
	 * Draw an image but with a specifity. Initially, the other method
	 * {@code drawElement} draw the element base of the center of the image.
	 * 
	 * But since we're using .png image, it can happens that the center of the image
	 * is empty. In consequence, we need to change the "center" of the image to draw
	 * properly the image.
	 * 
	 * We're adding two new parameters marginX and marginY to help drawing this
	 * item. For example, if we wants the center to be at the left center of the
	 * image, marginX = 0 and marginY = 0.5
	 * 
	 * @param graphics  {@code Graphics2D} of the game
	 * @param img       Image we wants to put
	 * @param x         The x coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param y         The y coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param dimX      Width of the image
	 * @param dimY      Height of the image
	 * @param direction Direction we wants to draw it
	 * @param marginX   Value between 0.0 and 1.0, the margeX of the item (If we
	 *                  wants the centerX, marginX = 0.5)
	 * @param marginY   Value between 0.0 and 1.0, the margeY of the item (If we
	 *                  wants the centerY, marginY = 0.5)
	 */
	private void drawSpecialElement(Graphics2D graphics, BufferedImage img, double x, double y, double dimX, double dimY, Direction direction, double marginX, double marginY) {
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
		transform.translate(-width * marginX, -height * marginY);
		graphics.drawImage(img, transform, null);
	}

	/**
	 * Draw an item in the bag but with a specifity. Initially, the other method
	 * {@code drawElement} draw the element base of the center of the image.
	 * 
	 * But since we're using .png image, it can happens that the center of the image
	 * is empty. In consequence, we need to change the "center" of the image to draw
	 * properly the image.
	 * 
	 * We're adding two new parameters marginX and marginY to help drawing this
	 * item. For example, if we wants the center to be at the left center of the
	 * image, marginX = 0 and marginY = 0.5
	 * 
	 * @param graphics  {@code Graphics2D} of the game
	 * @param img       Image we wants to put
	 * @param x         The x coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param y         The y coordinate of the location in user space where the
	 *                  upper left corner of the image is rendered
	 * @param dimX      Width of the image
	 * @param dimY      Height of the image
	 * @param direction Direction we wants to draw it
	 * @param marginX   Value between 0.0 and 1.0, the margeX of the item (If we
	 *                  wants the centerX, marginX = 0.5)
	 * @param marginY   Value between 0.0 and 1.0, the margeY of the item (If we
	 *                  wants the centerY, marginY = 0.5)
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
	 * 
	 * @param context Which window to draw
	 * @param data    Data of the game
	 */
	private void drawGrid(Graphics2D graphics, GameData data) {
		int size = data.bag().getGridSize();
		int[][] grid = data.bag().grid();
		BufferedImage imgBackpack = imgLoader.bgImages().get("BG_BACKPACK");
		BoundingBox boundingBox = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		graphics.drawImage(imgBackpack, MathLoader.getMapEvent().get("BG_BACKPACK").transform(), null);
		for (int i = 0; i < data.bag().getRow(); i++) {
			for (int j = 0; j < data.bag().getCol(); j++) {
				if (grid[i][j] >= -1) {
					graphics.drawImage(imgLoader.bgImages().get("BG_BAG_UNLOCK"), boundingBox.northWest().x() + (size * j), boundingBox.northWest().y() + (size * i), size, size, null);
				} else if (grid[i][j] == -2) {
					graphics.drawImage(imgLoader.bgImages().get("BG_BAG_LOCK"), boundingBox.northWest().x() + (size * j), boundingBox.northWest().y() + (size * i), size, size, null);
				}
			}
		}
		drawManaGrid(graphics, data);
	}

	/**
	 * Draw all tiles where mana is fill inside.
	 * 
	 * @param graphics {@code Graphics2D} of the game
	 * @param data     {@code data} of the game
	 */
	private void drawManaGrid(Graphics2D graphics, GameData data) {
		int size = data.bag().getGridSize();
		BoundingBox boundingBox = MathLoader.getMapEvent().get("BG_BACKPACK").box();

		for (XY coord : data.bag().getManaConnectedCoords()) {
			graphics.drawImage(imgLoader.bgImages().get("BG_BAG_MANA"), boundingBox.northWest().x() + (size * coord.x()), boundingBox.northWest().y() + (size * coord.y()), size, size, null);
		}
	}

	/**
	 * Check the id int the bag we wants to draw and calls the appropriate method.
	 * This method use a switch on the item id to know which item we wants to draw.
	 * 
	 * @param graphics {@Code Graphics2D} of the game
	 * @param data     Data of the game
	 */
	private void drawItemBag(Graphics2D graphics, GameData data) {
		var itemLst = data.bag().bagItemLst();
		for (var item : itemLst) {
			var id = item.info().ID();
			XY coordinate = item.shape()[0];
			switch (id) {
			case 1 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.75);
			case 2 -> drawItemGold(graphics, data, coordinate, item);
			case 3, 5, 6 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 3, item.direction(), imgLoader.itemImagesByID().get(id));
			case 4 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.25, 0.25);
			case 7 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 1, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.75);
			case 8 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 2, 3, item.direction(), imgLoader.itemImagesByID().get(id), 0.20, 0.5);
			case 9, 11, 12, 14, 15, 16, 18 -> drawInBag(graphics, new XY(coordinate.x(), coordinate.y()), 1, 1, item.direction(), imgLoader.itemImagesByID().get(id));
			case 10 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y()), 2, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.25, 0.25);
			case 13 -> drawInBagSpecial(graphics, new XY(coordinate.x() - 1, coordinate.y()), 3, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.2);
			case 17 -> drawInBagSpecial(graphics, new XY(coordinate.x(), coordinate.y() - 1), 2, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.25, 0.75);
			default -> {
			}
			}
		}
	}

	/**
	 * Draw gold item in bag. Since gold has a specifical visual depending of his
	 * amount, we did a separated method for that.
	 * 
	 * @param graphics   {@Code Graphics2D} of the game
	 * @param data       Data of the game
	 * @param coordinate coordinate XY of where the gold is in the bag
	 * @param item       {@Code Item} containing the amount of gold
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
		case ManaStone _ -> "Le mana se propage entre les éléments conduteurs (ceux avec du métal)";
		case EnchantedDiamondSword _ -> "Une épée DIVINE";
		case Cookie _ -> "Cookie congelé du crous ! Il est incassable !";
		default -> throw new IllegalArgumentException("Unexpected value: " + item.info().ID());
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
		case DespairShield _ -> "Se met 10 Shield en échange de 3PV";
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
		case EnchantedDiamondSword _ -> "Inflige -8PV à l'ennemi (nécessite 1 point de Mana)";
		case Cookie _ -> "Augmente de 10% les dégats !";
		default -> throw new IllegalArgumentException("Unexpected value: " + item.info().ID());
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
		if (item != null) {
			writeItemInfo(graphics, data, item, NW, SE);
		}
	}

	/**
	 * Renders all textual information related to an item. This includes the item
	 * name, description, action point cost, mana cost, durability and special
	 * effects.
	 *
	 * Text is positioned relative to the given bounding box coordinates and uses
	 * different colors to highlight specific attributes.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     current game data
	 * @param item     item whose information is displayed
	 * @param NW       north-west coordinate of the display area
	 * @param SE       south-east coordinate of the display area
	 */
	private void writeItemInfo(Graphics2D graphics, GameData data, Item item, XY NW, XY SE) {
		int height = SE.y() - NW.y();
		drawTextInfoName(graphics, item, NW);
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH4());
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		graphics.setColor(Color.WHITE);
		drawTextInfo(graphics, getDescriptionItem(item), NW.x(), NW.y() + (int) (NW.y() * 0.30), 25);
		graphics.setColor(Color.GREEN);
		drawTextInfo(graphics, "AP : " + item.info().AP(), NW.x(), NW.y() + height / 4, 20);
		drawTextInfo(graphics, "MANA : " + item.info().mana(), NW.x(), NW.y() + height / 4 + (int) (fm.getAscent() * 1.5), 20);
		var durabilityText = item.durability() == -1 ? "Infiny" : item.durability();
		drawTextInfo(graphics, "Durability : " + durabilityText, NW.x(), NW.y() + height / 4 + fm.getAscent() * 3, 20);
		graphics.setColor(Color.WHITE);
		drawTextInfo(graphics, getEffectItem(item), NW.x(), NW.y() + height / 4 + fm.getAscent() * 5, 23);
	}

	/**
	 * Draws the item name on screen using a font size and color based on the item's
	 * rarity.
	 *
	 * The item name is displayed in uppercase and positioned relative to the given
	 * north-west coordinate.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param item     item whose name is displayed
	 * @param NW       north-west coordinate of the display area
	 */
	private void drawTextInfoName(Graphics2D graphics, Item item, XY NW) {
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
		graphics.setFont(font);
		graphics.setColor(switch (item.info().rarity()) {
		case COMMON -> Color.GRAY;
		case RARE -> Color.GREEN;
		case SUPERARE -> Color.BLUE;
		case EPIC -> Color.MAGENTA;
		case LEGENDARY -> Color.YELLOW;
		case MYTHIC -> Color.PINK;
		});
		graphics.drawString(item.toString().toUpperCase(), NW.x(), NW.y() + (int) (NW.y() * 0.10));
	}

	/**
	 * Draws multi-line text with automatic word wrapping. The text is split into
	 * multiple lines so that each line does not exceed the specified maximum number
	 * of characters.
	 *
	 * Lines are rendered vertically using the current font metrics.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param content  text content to display
	 * @param x        horizontal position of the text
	 * @param y        vertical starting position of the text
	 * @param maxChar  maximum number of characters per line
	 */
	private void drawTextInfo(Graphics2D graphics, String content, int x, int y, int maxChar) {
		if (maxChar <= 0) {
			throw new IllegalArgumentException("maxChar <= 0");
		}
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
				if (line.length() > 0) {
					line.append(" ");
				}
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
	 * @param graphics  {@code Graphics2D} of the game
	 * @param pos       {@code XY} containing the coordinate NorthWest (x, y) of
	 *                  image on the screen.
	 * @param width     Number of tile horizontally
	 * @param height    Number of tile vertically
	 * @param direction Direction the img aim
	 * @param img       {@code BufferedImage} of the item
	 */
	private void drawInBag(Graphics2D graphics, XY pos, int width, int height, Direction direction, BufferedImage img) {
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		drawElement(graphics, img, coord.northWest().x() + (tileSize * pos.x()) + centerX, coord.northWest().y() + (tileSize * pos.y()) - centerY, tileSize * width, tileSize * height, direction);

	}

	/**
	 * Draw an item with a special shape in the backpack
	 * 
	 * @param graphics  {@code Graphics2D} of the game
	 * @param pos       {@code XY} containing the coordinate NorthWest (x, y) of
	 *                  image on the screen.
	 * @param width     Number of tile horizontally
	 * @param height    Number of tile vertically
	 * @param direction Direction the img aim
	 * @param img       {@code BufferedImage} of the item
	 * @param marginX   Value between 0.0 and 1.0 indicating the gap horizontally
	 * @param marginY   Value between 0.0 and 1.0 indicating the gap vertically
	 */
	private void drawInBagSpecial(Graphics2D graphics, XY pos, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY) {
		BoundingBox coord = MathLoader.getMapEvent().get("BG_BACKPACK").box();
		double centerX = 0, centerY = 0;
		drawSpecialElementInBag(graphics, img, coord.northWest().x() + (tileSize * pos.x()) + centerX, coord.northWest().y() + (tileSize * pos.y()) - centerY, tileSize * width, tileSize * height, direction, marginX, marginY);
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
			var render = MathLoader.getMapEvent().get("Roland");
			BufferedImage img = imgLoader.bgImages().get("Roland");
			graphics.drawImage(img, render.transform(), null);
		}
		drawHeroStats(graphics, data, (int) (width * 0.20 + size_x / 2), (int) (height * 0.50 + size_y));
	}

	/**
	 * Draws all the information about the hero
	 * 
	 * @param graphics {@code Graphics2D} object for drawing.
	 * @param data     GameData containing the game data.
	 * @param x        coordinate x where we wants to draw.
	 * @param y        coordinate y where we wants to draw.
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
		if (GameDataCombat.combat()) {
			drawHeroBoost(graphics, data);
			drawHeroEffect(graphics, data);
		}
	}

	/**
	 * Draws the hero health bar with its icon, background bar, filled health
	 * amount, and numerical HP value.
	 *
	 * The bar width is proportional to the hero's current HP relative to the
	 * maximum HP.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero health information
	 */
	private void drawHeroHP(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_HEALTH");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_HEALTH");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(Color.GRAY);
		graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20, size));
		graphics.setColor(Color.GREEN);
		graphics.fill(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20 * data.hero().getHP() / data.hero().getMaxHP(), size));
		graphics.setColor(Color.WHITE);
		graphics.draw(new Rectangle2D.Double(logoWidth, logoHeight / 2 - size / 2, width * 0.20, size));
		graphics.drawString(data.hero().getHP() + "/" + data.hero().getMaxHP(), (int) (logoWidth + width * 0.205), logoHeight / 2 + size / 2);
	}

	/**
	 * Draws the hero shield (protection) bar with its icon, background bar, filled
	 * protection amount, and numerical value.
	 *
	 * The shield amount is displayed relative to the hero's maximum HP.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero protection information
	 */
	private void drawHeroShield(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_SHIELD");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_SHIELD");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(Color.GRAY);
		graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
		graphics.setColor(Color.BLUE);
		graphics.fill(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20 * data.hero().getCurrentProtection() / data.hero().getMaxHP(), size));
		graphics.setColor(Color.WHITE);
		graphics.draw(new Rectangle2D.Double(logoWidth, render.box().northWest().y() + logoHeight / 2 - size / 2, width * 0.20, size));
		graphics.drawString(data.hero().getCurrentProtection() + "/" + data.hero().getMaxHP(), (int) (logoWidth + width * 0.205), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Displays the hero mana information with its icon.
	 *
	 * During combat, the mana value is taken from combat data; otherwise, it is
	 * taken from the hero's inventory.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing mana information
	 */
	private void drawHeroMana(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_MANA");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_MANA");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(Color.CYAN);
		graphics.drawString((GameDataCombat.combat() ? GameDataCombat.getNbMana() : data.bag().getManaInBag()) + " MANA", (int) (logoWidth + width * 0.005), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Draws the hero damage boost indicator with its icon.
	 *
	 * The boost value is displayed as a percentage: positive values are shown in
	 * green, negative values in red.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero boost information
	 */
	private void drawHeroBoost(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_BOOST");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_BOOST");
		graphics.drawImage(img, render.transform(), null);
		var boostDmg = data.hero().getBoostDmg();
		String boostText;
		if (boostDmg < 0) {
			graphics.setColor(Color.RED);
			boostText = boostDmg + "%";
		} else {
			graphics.setColor(Color.GREEN);
			boostText = "+" + boostDmg + "%";
		}
		graphics.drawString(boostText + " BOOST DMG", (int) (logoWidth + width * 0.005), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Renders all active status effects applied to the hero.
	 *
	 * Effects are displayed symmetrically around the hero, each with its icon and
	 * associated stack value.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero effects
	 */
	private void drawHeroEffect(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("Roland");
		var boundingBox = render.box();
		var width = boundingBox.southEast().x() - boundingBox.northWest().x();
		var centerX = boundingBox.northWest().x() + width / 2;
		int size = (int) (height * 0.04);
		int gap = (int) (size * 0.1);
		var i = 0;
		for (var effect : data.hero().getEffects().keySet()) {
			int offset = (i + 1) / 2;
			int dir = (i % 2 == 0) ? 1 : -1;
			int pos = offset * dir;
			int x = centerX + pos * (size + gap);
			drawHeroEffectImageAndValue(graphics, data, effect, x, (boundingBox.southEast().y()), size);
			i++;
		}
	}

	/**
	 * Draws a single hero effect icon along with its numeric value.
	 *
	 * The value represents the number of stacks for the given effect.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero effects
	 * @param effect   the effect to render
	 * @param x        horizontal position of the effect
	 * @param y        vertical position of the effect
	 * @param size     size of the effect icon
	 */
	private void drawHeroEffectImageAndValue(Graphics2D graphics, GameData data, Effect effect, int x, int y, int size) {
		var img = switch (effect) {
		case POISON -> imgLoader.bgImages().get("ICON_POISON");
		case FIRE -> imgLoader.bgImages().get("ICON_BURN");
		default -> throw new IllegalArgumentException("This is not an effect : " + effect);
		};
		drawElement(graphics, img, x, y, size, size, Direction.UP);
		var charNumber = Integer.toString(data.hero().getEffects().get(effect));
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
		FontMetrics fm = graphics.getFontMetrics();
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		graphics.drawString(charNumber, x + fm.stringWidth(charNumber) / 2, (int) (y + size * 1.05));
		graphics.setColor(Color.WHITE);
		graphics.drawString(charNumber, x + fm.stringWidth(charNumber) / 2, y + size);
	}

	/**
	 * Displays the hero action points (AP) with its icon.
	 *
	 * The text color changes depending on whether the hero has enough action points
	 * available.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero action points
	 */
	private void drawHeroAction(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_ACTION");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_ACTION");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(data.hero().getEnergyPoint() > 1 ? Color.YELLOW : Color.RED);
		graphics.drawString(data.hero().getEnergyPoint() + " AP", (int) (logoWidth + width * 0.005), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Displays the number of unlockable cases available to the hero.
	 *
	 * The text color indicates whether at least one case can be unlocked.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing unlock information
	 */
	private void drawHeroUnlock(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("ICON_UNLOCK");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("ICON_UNLOCK");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(data.bag().getCaseUnlock() > 0 ? Color.GREEN : Color.RED);
		graphics.drawString(data.bag().getCaseUnlock() + " CASE DEBLOQUABLE", (int) (logoWidth + width * 0.005), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Draws the hero gold amount with its associated icon.
	 *
	 * The gold value is displayed in green if greater than zero, otherwise in red.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing gold information
	 */
	private void drawHeroGold(Graphics2D graphics, GameData data) {
		var render = MathLoader.getMapEvent().get("gold");
		int logoWidth = render.box().southEast().x() - render.box().northWest().x();
		int logoHeight = (int) (height * 0.04);
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		BufferedImage img = imgLoader.bgImages().get("gold1");
		graphics.drawImage(img, render.transform(), null);
		graphics.setColor(data.bag().getGoldInBag() > 0 ? Color.GREEN : Color.RED);
		graphics.drawString(data.bag().getGoldInBag() + " gold", (int) (logoWidth + width * 0.005), render.box().northWest().y() + logoHeight / 2 + size / 2);
	}

	/**
	 * Displays the hero level and experience progress bar.
	 *
	 * The experience bar visually represents the current XP relative to the maximum
	 * XP required for the next level.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing hero level and XP
	 */
	private void drawHeroLevel(Graphics2D graphics, GameData data) {
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		int textWidth = fm.stringWidth("LEVEL " + data.hero().getLevel());
		graphics.setColor(Color.WHITE);
		graphics.drawString("LEVEL " + data.hero().getLevel(), width / 2 - textWidth / 2, size);
		graphics.setColor(Color.GRAY);
		graphics.fill(new Rectangle2D.Double(width / 2 - textWidth / 2, (int) (height * 0.03), textWidth, size * 0.25));
		graphics.setColor(Color.CYAN);
		graphics.fill(new Rectangle2D.Double(width / 2 - textWidth / 2, (int) (height * 0.03), textWidth * data.hero().getXp() / data.hero().maxXP(), size * 0.25));
	}

	/**
	 * Displays the current floor level reached by the player.
	 *
	 * This method also triggers the rendering of the score display.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing floor information
	 */
	private void drawFloorLevel(Graphics2D graphics, GameData data) {
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		int textWidth = fm.stringWidth("ETAGE : " + data.floor());
		graphics.setColor(Color.WHITE);
		graphics.drawString("ETAGE : " + data.floor(), width - (int) (textWidth + width * 0.01), size);
		drawScoreLevel(graphics, data);
	}

	/**
	 * Calculates and displays the current score.
	 *
	 * The score is computed using the base score, current floor, and hero level to
	 * reflect progression difficulty.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing score information
	 */
	private void drawScoreLevel(Graphics2D graphics, GameData data) {
		int score = (int) (data.getScore() * (1.2 * data.floor() + (data.hero().getLevel() / 2.0)));
		int size = (int) (height * 0.03);
		Font font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		int textWidth = fm.stringWidth("SCORE : " + score);
		graphics.setColor(Color.WHITE);
		graphics.drawString("SCORE : " + score, width - (int) (textWidth + width * 0.01), size * 2);
	}

	/**
	 * Draw the button for switching between map and bag
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 */
	private void drawButton(Graphics2D graphics, GameData data) {
		graphics.setColor(Color.RED);
		graphics.setColor(data.mapOrBag() ? Color.ORANGE : Color.CYAN);
		graphics.fill(new Rectangle2D.Double(width - tileSize / 2, height / 3.5 - 2.5 * tileSize, tileSize / 2, tileSize / 2));
		BufferedImage img = imgLoader.bgImages().get("ICON_ABANDON");
		graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_ABANDON").transform(), null);

	}

	/**
	 * Draw the map in the screen
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 */
	private void drawMap(Graphics2D graphics, GameData data) {
		BufferedImage imgMap = imgLoader.bgImages().get("BG_MAP");
		var leftGrid = MathLoader.getMapEvent().get("BG_MAP").box();
		var gap = tileSize * 0.1;
		graphics.drawImage(imgMap, MathLoader.getMapEvent().get("BG_MAP").transform(), null);
		for (int i = 0; i < data.map().getRow(); i++) {
			for (int j = 0; j < data.map().getCol(); j++) {
				var coordXY = new XY(j, i);
				int newX = (int) (gap * j) + leftGrid.northWest().x() + (tileSize * j);
				int newY = (int) (gap * i) + leftGrid.northWest().y() + (tileSize * i);
				if (data.map().getHeroVisible().contains(coordXY)) {
					checkAndDrawTileMap(graphics, data, coordXY, newX, newY);
				} else {
					graphics.drawImage(imgLoader.bgImages().get("BG_MAP_SHADOW"), newX - (int) gap, newY - (int) gap, tileSize + (int) gap * 2, tileSize + (int) gap * 2, null);
				}
			}
		}
		drawPathMap(graphics, data, leftGrid, gap);
		drawShortestPath(graphics, data, leftGrid, gap);
		var coord = data.map().getHeroPos();
		graphics.drawImage(imgLoader.bgImages().get("ICON_HERO"), (int) (gap * coord.x()) + leftGrid.northWest().x() + (coord.x() * tileSize), (int) (gap * coord.y()) + leftGrid.northWest().y() + (coord.y() * tileSize), tileSize, tileSize, null);
	}

	/**
	 * Draws a single map tile at the given screen position and overlays additional
	 * visual information depending on its state.
	 *
	 * The base tile is always rendered first, followed by: - an accessibility
	 * overlay if the hero can reach this tile - a specific icon depending on the
	 * room type (shop, enemy, event, etc.)
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing map and accessibility information
	 * @param coordXY  grid coordinates of the tile
	 * @param newX     screen X position where the tile is drawn
	 * @param newY     screen Y position where the tile is drawn
	 */
	private void checkAndDrawTileMap(Graphics2D graphics, GameData data, XY coordXY, int newX, int newY) {
		graphics.drawImage(imgLoader.bgImages().get("BG_MAP_TILE"), newX, newY, tileSize, tileSize, null);
		if (data.map().getHeroAccessible().contains(coordXY)) {
			graphics.drawImage(imgLoader.bgImages().get("BG_MAP_TILE_ACCES"), newX, newY, tileSize, tileSize, null);
		}
		switch (data.map().getGrid()[coordXY.y()][coordXY.x()]) {
		case Shop _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_SHOP"), newX, newY, tileSize, tileSize, null);
		case EnemyRoom _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_COMBAT"), newX, newY, tileSize, tileSize, null);
		case EventRoom _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_EVENT"), newX, newY, tileSize, tileSize, null);
		case Healer _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_HEAL"), newX, newY, tileSize, tileSize, null);
		case Start _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_START"), newX, newY, tileSize, tileSize, null);
		case Exit _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_EXIT"), newX, newY, tileSize, tileSize, null);
		case LockedDoor _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_LOCK_DOOR"), newX, newY, tileSize, tileSize, null);
		case Treasure _ -> graphics.drawImage(imgLoader.bgImages().get("ICON_TREASURE"), newX, newY, tileSize, tileSize, null);
		default -> {
		}
		}
	}

	/**
	 * Draws all visible paths between accessible map tiles.
	 *
	 * Paths are rendered as orange lines connecting the center of each tile to the
	 * center of its accessible neighboring tiles.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing visible path information
	 * @param leftGrid bounding box of the map grid on screen
	 * @param gap      spacing between tiles on screen
	 */
	private void drawPathMap(Graphics2D graphics, GameData data, BoundingBox leftGrid, double gap) {
		graphics.setColor(Color.ORANGE);
		graphics.setStroke(new BasicStroke(5));
		for (var coord : data.map().getHeroVisibleLine()) {
			for (var coord_acc : data.map().getGrid()[coord.y()][coord.x()].getAccessible()) {
				graphics.drawLine((int) ((gap * coord.x()) + (leftGrid.northWest().x() + (tileSize * coord.x() + tileSize / 2))), (int) ((gap * coord.y()) + (leftGrid.northWest().y() + (tileSize * coord.y() + tileSize / 2))), (int) ((gap * coord_acc.x()) + (leftGrid.northWest().x() + (tileSize * coord_acc.x()) + tileSize / 2)), (int) ((gap * coord_acc.y()) + (leftGrid.northWest().y() + (tileSize * coord_acc.y()) + tileSize / 2)));
			}
		}
	}

	/**
	 * Draws the shortest path from the hero position to the target destination.
	 *
	 * The shortest path is rendered as a red line connecting successive tiles in
	 * the computed path, if such a path exists.
	 *
	 * @param graphics Graphics2D context used for rendering
	 * @param data     game data containing the shortest path
	 * @param leftGrid bounding box of the map grid on screen
	 * @param gap      spacing between tiles on screen
	 */
	private void drawShortestPath(Graphics2D graphics, GameData data, BoundingBox leftGrid, double gap) {
		var shortestPath = data.getShortestPath();
		if (shortestPath != null) {
			graphics.setColor(Color.RED);
			for (int i = 0; i < shortestPath.size() - 1; i++) {
				var coord = shortestPath.get(i);
				var coord_acc = shortestPath.get(i + 1);
				graphics.drawLine((int) ((gap * coord.x()) + (leftGrid.northWest().x() + (tileSize * coord.x() + tileSize / 2))), (int) ((gap * coord.y()) + (leftGrid.northWest().y() + (tileSize * coord.y() + tileSize / 2))), (int) ((gap * coord_acc.x()) + (leftGrid.northWest().x() + (tileSize * coord_acc.x()) + tileSize / 2)), (int) ((gap * coord_acc.y()) + (leftGrid.northWest().y() + (tileSize * coord_acc.y()) + tileSize / 2)));
			}
		}
	}

	/**
	 * Update the position of each weapons we can move in the screen.
	 * 
	 * @param graphics {@code ApplicationContext} of the game.
	 * @param data     GameData containing the game data.
	 */
	private void updateDragItem(Graphics2D graphics, GameData data) {
		var dragItem = new LinkedHashMap<Item, BoundingBox>(GameDataClick.getDragItemMap());
		if (!dragItem.isEmpty()) {
			Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
			graphics.setFont(font);
			graphics.setColor(Color.RED);
			drawText(graphics, "Debarassez vous de vos items pour continuer", width / 2, (int) (height * 0.98), 100);
			dragItem.reversed().forEach((item, box) -> drawDrag(graphics, data, item, box));
		}

	}

	/**
	 * Check the id of the item we're dragging and calls the appropriate method.<br>
	 * This method use a switch on the item id to know which item we wants to draw.
	 * 
	 * @param graphics {@code ApplicationContext} of the game.
	 * @param data     {@code GameData} containing the game data.
	 * @param item     The Item we're currently dragging.
	 * @param box      The boundingbox (border) of the item.
	 */
	private void drawDrag(Graphics2D graphics, GameData data, Item item, BoundingBox box) {
		var id = item.info().ID();
		switch (id) {
		case 1 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.75);
		case 2 -> drawDragGold(graphics, data, item, box);
		case 4, 10 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.25, 0.25);
		case 3, 5, 6 -> drawDragItem(graphics, box.northWest(), 1, 3, item.direction(), imgLoader.itemImagesByID().get(id));
		case 7 -> drawDragSpecialItem(graphics, box.northWest(), 1, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.75);
		case 8 -> drawDragSpecialItem(graphics, box.northWest(), 2, 3, item.direction(), imgLoader.itemImagesByID().get(id), 0.20, 0.5);
		case 9, 11, 12, 14, 15, 16, 18 -> drawDragItem(graphics, box.northWest(), 1, 1, item.direction(), imgLoader.itemImagesByID().get(id));
		case 13 -> drawDragSpecialItem(graphics, box.northWest(), 3, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.5, 0.2);
		case 17 -> drawDragSpecialItem(graphics, box.northWest(), 2, 2, item.direction(), imgLoader.itemImagesByID().get(id), 0.25, 0.75);
		default -> {
		}
		}
	}

	/**
	 * Determines the visual size category for a gold value.
	 *
	 * The returned size is used to adjust rendering or scaling depending on the
	 * amount of gold: - small for low values - medium for moderate values - large
	 * for high values
	 *
	 * @param value the amount of gold
	 * @return an integer representing the size category
	 */
	private int getSizeGold(int value) {
		if (value <= 15) {
			return 1;
		} else if (value <= 50) {
			return 2;
		} else {
			return 3;
		}
	}

	/**
	 * Draws a dragged gold item with a visual size adapted to its value.
	 *
	 * The gold amount is converted into a size category using
	 * {@link #getSizeGold(int)}, which determines which gold sprite is rendered
	 * (small, medium, or large).
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data (not directly used but kept for
	 *                 consistency)
	 * @param item     The dragged item, expected to be an instance of {@code Gold}
	 * @param box      The bounding box defining the drawing area
	 */
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
	 * @param coord     {@code XY} containing the coordinate NorthWest (x, y) of
	 *                  image on the screen.
	 * @param size      Size of a tile.
	 * @param width     Number of tile horizontally.
	 * @param height    Number of tile vertically.
	 * @param direction {@code Direction} the item will aim.
	 * @param img       {@code BufferedImage} of the item.
	 */
	private void drawDragItem(Graphics2D graphics, XY coord, int width, int height, Direction direction, BufferedImage img) {
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = tileSize * width, dimY = tileSize * height;
		// If direction is left or right, the North West coordinate need to be update in
		// order to draw correctly
		if (direction == Direction.LEFT || direction == Direction.RIGHT) {
			centerX = x + dimY / 2.0;
			centerY = y + dimX / 2.0;
			x = centerX - dimX / 2.0;
			y = centerY - dimY / 2.0;
		}
		drawElement(graphics, img, x, y, dimX, dimY, direction);
	}

	/**
	 * Draw the current item we're dragging but with a specifity. Initially, the
	 * other method {@code drawDragItem} draw the item base of the center of the
	 * image.
	 * 
	 * But since we're using .png image, it can happens that the center of the image
	 * is empty. In consequence, we need to change the "center" of the image to draw
	 * properly the image.
	 * 
	 * We're adding two new parameters marginX and marginY to help drawing this
	 * item. For example, if we wants the center to be at the left center of the
	 * image, marginX = 0 and marginY = 0.5
	 * 
	 * @param graphics  {@code ApplicationContext} of the game.
	 * @param coord     {@code XY} containing the coordinate NorthWest (x, y) of
	 *                  image on the screen.
	 * @param width     Number of tile horizontally.
	 * @param height    Number of tile vertically.
	 * @param direction {@code Direction} the item will aim.
	 * @param img       {@code BufferedImage} of the item.
	 * @param marginX   Value between 0.0 and 1.0, the margeX of the item (If we
	 *                  wants the centerX, marginX = 0.5)
	 * @param marginY   Value between 0.0 and 1.0, the margeY of the item (If we
	 *                  wants the centerY, marginY = 0.5)
	 */
	private void drawDragSpecialItem(Graphics2D graphics, XY coord, int width, int height, Direction direction, BufferedImage img, double marginX, double marginY) {
		double centerX, centerY;
		double x = coord.x(), y = coord.y();
		double dimX = tileSize * width, dimY = tileSize * height;
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
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 * @param list    List of all enemy we fight
	 */
	private void updateCombat(Graphics2D graphics, GameData data, List<Enemy> list) {
		drawEndTurnButton(graphics, imgLoader.bgImages().get("BG_ENDTURN"));
		list.forEach(enemy -> drawEnemy(graphics, data, enemy));
		if (GameDataCombat.getHoverItem() != null) {
			drawSelectedItem(graphics, GameDataCombat.getHoverItem());
		}
		drawLog(graphics, data, GameDataCombat.getLog().reversed().iterator());
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
		graphics.drawString(sentence, width / 2 - textWidth / 2, y + (int) (textHeight * 0.25));
		graphics.setColor(Color.WHITE);
		graphics.drawString(sentence, width / 2 - textWidth / 2, y);
	}

	/**
	 * Draws an enemy in the windows
	 * 
	 * @param graphics {@code graphics} of the game.
	 * @param data     GameData containing the game data.
	 * @param enemy    Data of the enemy.
	 */
	private void drawEnemy(Graphics2D graphics, GameData data, Enemy enemy) {
		var imgName = enemy.getInfo().img();
		var boundingBox = GameDataCombat.getEnemyBox().get(enemy);
		int sizeX = boundingBox.southEast().x() - boundingBox.northWest().x();
		int sizeY = boundingBox.southEast().y() - boundingBox.northWest().y();
		drawEnemyArc(graphics, boundingBox.northWest().x(), (boundingBox.southEast().y()), sizeX, sizeY, enemy);
		drawElement(graphics, imgLoader.bgImages().get(imgName), boundingBox.northWest().x(), boundingBox.northWest().y(), sizeX, sizeY, Direction.UP);
		drawEnemyInfo(graphics, enemy, boundingBox, sizeX, sizeY);
	}

	/**
	 * Renders the game log on screen.
	 *
	 * Log entries are displayed from the most recent to the oldest, starting from
	 * the bottom of the screen and going upward.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data (not directly used here)
	 * @param log      The list of log messages to display
	 */
	private void drawLog(Graphics2D graphics, GameData data, Iterator<String> log) {
		int i = 0;
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		FontMetrics fm = graphics.getFontMetrics();
		double gap = fm.getAscent() * 1.5;
		while (log.hasNext()) {
			var text = log.next();
			graphics.drawString("-> " + text, (int) gap, (int) (height - gap - gap * i++));
		}
	}

	/**
	 * Draws the "End Turn" button using its predefined layout.
	 *
	 * The button position and size are defined by the associated map event
	 * transform.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param img      The image used to represent the end turn button
	 */
	private void drawEndTurnButton(Graphics2D graphics, BufferedImage img) {
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG_ENDTURN").transform(), null);
	}

	/**
	 * Draws all the information about the enemy
	 * 
	 * @param graphics {@code Graphics2D} object for drawing.
	 * @param enemy    Data of the enemy.
	 * @param x        Coordinate x where we wants to draw.
	 * @param y        Coordinate y where we wants to draw.
	 */
	private void drawEnemyInfo(Graphics2D graphics, Enemy enemy, BoundingBox boundingBox, int width, int height) {
		int size = FontLoader.getH1();
		drawEnemyPV(graphics, boundingBox.northWest().x(), boundingBox.southEast().y(), width, height, enemy, size);
		drawEnemyShield(graphics, boundingBox.southEast().x(), boundingBox.southEast().y(), width, height, enemy, size);
		drawEnemyAction(graphics, boundingBox, enemy);
		drawEnemyEffect(graphics, enemy, boundingBox);
	}

	/**
	 * Draws all active status effects applied to an enemy.
	 *
	 * Effects are displayed symmetrically around the horizontal center of the enemy
	 * bounding box, each with its icon and remaining value.
	 *
	 * @param graphics    The graphics context used for rendering
	 * @param enemy       The enemy whose effects are displayed
	 * @param boundingBox The bounding box of the enemy sprite
	 */
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

	/**
	 * Draws a single enemy effect icon and its associated value.
	 *
	 * The icon depends on the effect type, and the numeric value represents the
	 * remaining duration or intensity.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param enemy    The enemy affected by the effect
	 * @param effect   The effect to render
	 * @param x        The x-coordinate where the effect is drawn
	 * @param y        The y-coordinate where the effect is drawn
	 * @param size     The size of the effect icon
	 *
	 * @throws IllegalArgumentException if the effect type is unsupported
	 */
	private void drawEnemyEffectImageAndValue(Graphics2D graphics, Enemy enemy, Effect effect, int x, int y, int size) {
		var img = switch (effect) {
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
		graphics.drawString(charNumber, x + fm.stringWidth(charNumber) / 2, (int) (y + size * 1.05));
		graphics.setColor(Color.WHITE);
		graphics.drawString(charNumber, x + fm.stringWidth(charNumber) / 2, y + size);
	}

	/**
	 * Draws enemy textual combat information.
	 *
	 * Displays the enemy name and its next action above the enemy, with visual
	 * emphasis when the enemy is the current combat target.
	 *
	 * @param graphics    The graphics context used for rendering
	 * @param boundingBox The bounding box of the enemy sprite
	 * @param enemy       The enemy to display information for
	 */
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

	/**
	 * Draws the enemy's current health points as centered text.
	 *
	 * A shadow is rendered first to improve readability.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param x        The center x-coordinate of the text
	 * @param y        The base y-coordinate of the text
	 * @param width    The width of the enemy area
	 * @param height   The height of the enemy area
	 * @param enemy    The enemy whose HP is displayed
	 * @param size     The font size used for rendering
	 */
	private void drawEnemyPV(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy, int size) {
		// Text shadow
		Font font = new Font("Mikodacs", Font.PLAIN, size + 4);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		graphics.setColor(Color.BLACK);
		graphics.drawString(Integer.toString(enemy.getHP()), x - fm.stringWidth(Integer.toString(enemy.getHP())) / 2 + 1, y - height / 4 + 4);
		// PV
		font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		fm = graphics.getFontMetrics();
		graphics.setColor(Color.WHITE);
		graphics.drawString(Integer.toString(enemy.getHP()), x - fm.stringWidth(Integer.toString(enemy.getHP())) / 2, y - height / 4);
	}

	/**
	 * Draws the enemy's current shield value as centered text.
	 *
	 * A shadow is rendered first to improve readability.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param x        The center x-coordinate of the text
	 * @param y        The base y-coordinate of the text
	 * @param width    The width of the enemy area
	 * @param height   The height of the enemy area
	 * @param enemy    The enemy whose shield is displayed
	 * @param size     The font size used for rendering
	 */
	private void drawEnemyShield(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy, int size) {
		// Text shadow
		Font font = new Font("Mikodacs", Font.PLAIN, size + 4);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		graphics.setColor(Color.BLACK);
		graphics.drawString(Integer.toString(enemy.getShield()), x - fm.stringWidth(Integer.toString(enemy.getShield())) / 2 - 1, y - height / 4 + 4);
		// PV
		font = new Font("Mikodacs", Font.PLAIN, size);
		graphics.setFont(font);
		fm = graphics.getFontMetrics();
		graphics.setColor(Color.BLUE);
		graphics.drawString(Integer.toString(enemy.getShield()), x - fm.stringWidth(Integer.toString(enemy.getShield())) / 2 - 1, y - height / 4);
	}

	/**
	 * Draws a curved health bar above the enemy.
	 *
	 * The arc represents the enemy's remaining HP as a percentage of its maximum
	 * health.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param x        The x-coordinate of the arc
	 * @param y        The y-coordinate of the arc
	 * @param width    The width of the arc
	 * @param height   The height of the arc
	 * @param enemy    The enemy whose health is represented
	 */
	private void drawEnemyArc(Graphics2D graphics, int x, int y, int width, int height, Enemy enemy) {
		graphics.setStroke(new BasicStroke(38));
		graphics.setColor(Color.BLACK);
		graphics.drawArc(x, (int) (y - (height / 2 * 1.5) / 2), width, height / 2, 0, -180);
		graphics.setStroke(new BasicStroke(30));
		graphics.setColor(Color.GRAY);
		graphics.drawArc(x, (int) (y - (height / 2 * 1.5) / 2), width, height / 2, 0, -180);
		graphics.setColor(Color.RED);
		double percent = (double) enemy.getHP() / (double) enemy.getInfo().maxHP();
		int arcAngle = (int) (180 * percent);
		graphics.drawArc(x, (int) (y - (height / 2 * 1.5) / 2), width, height / 2, 0, -arcAngle);
		graphics.setStroke(new BasicStroke(1));
	}

	/**
	 * Draw an event and their choices
	 * 
	 * @param graphics {@code Graphics2D} of the game.
	 * @param data     {@code GameData} containing all informations about the game
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
	 * @param data     {@code GameData} containing all informations about the game
	 */
	private void drawBgEvent(Graphics2D graphics, GameData data) {
		BufferedImage img = imgLoader.bgImages().get("BG_EVENT");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG_EVENT").transform(), null);
	}

	/**
	 * Draw the text of the event in the screen
	 * 
	 * @param graphics {@code Graphics2D} of the game.
	 * @param data     {@code GameData} containing all informations about the game
	 */
	private void drawTextEvent(Graphics2D graphics, GameData data) {
		double top = MathLoader.getMapEvent().get("BG_EVENT").box().northWest().y();
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
		graphics.setColor(Color.WHITE);
		graphics.setFont(font);
		FontMetrics fm = graphics.getFontMetrics();
		int textWidth = fm.stringWidth(data.event().getRoot().getQuestion());
		int x = width / 2 - textWidth / 2;
		int y = (int) (top * 1.05) + fm.getAscent();
		graphics.drawString(data.event().getRoot().getQuestion(), x, y);
	}

	/**
	 * Draw all elements regarding an event. Also called the fonction to write the
	 * choice inside.
	 * 
	 * @param graphics {@code Graphics2D} of the game.
	 * @param data     {@code GameData} containing all informations about the game
	 */
	private void drawChoiceEvent(Graphics2D graphics, GameData data) {
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
		graphics.setFont(font);
		if (data.event().getRoot().getChoice2() == null) {
			drawEndChoiceButtonEvent(graphics, data);
		} else {
			drawFirstChoiceButtonEvent(graphics, data);
			drawSecondChoiceButtonEvent(graphics, data);
		}
	}

	/**
	 * Draws the final event choice button.
	 *
	 * This button is displayed when the event reaches its end state and shows the
	 * associated answer text.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing the active event
	 */
	private void drawEndChoiceButtonEvent(Graphics2D graphics, GameData data) {
		var img3 = imgLoader.bgImages().get("BG_CHOICE_END");
		var event3 = MathLoader.getMapEvent().get("BG_CHOICE_END");
		int width = event3.box().southEast().x() - event3.box().northWest().x();
		int height = event3.box().southEast().y() - event3.box().northWest().y();
		graphics.drawImage(img3, event3.transform(), null);
		drawText(graphics, data.event().getRoot().getChoice1().getAnswer(), event3.box().northWest().x() + width / 2, (int) (event3.box().northWest().y() + height * 0.5), 30);
	}

	/**
	 * Draws the first choice button of an event.
	 *
	 * The button displays the text associated with the first available choice in
	 * the event decision tree.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing the active event
	 */
	private void drawFirstChoiceButtonEvent(Graphics2D graphics, GameData data) {
		var img1 = imgLoader.bgImages().get("BG_CHOICE1");
		var event1 = MathLoader.getMapEvent().get("BG_CHOICE1");
		int width = event1.box().southEast().x() - event1.box().northWest().x();
		int height = event1.box().southEast().y() - event1.box().northWest().y();
		graphics.drawImage(img1, event1.transform(), null);
		drawText(graphics, data.event().getRoot().getChoice1().getAnswer(), event1.box().northWest().x() + width / 2, (int) (event1.box().northWest().y() + height * 0.5), 30);
	}

	/**
	 * Draws the second choice button of an event.
	 *
	 * The button displays the text associated with the second available choice in
	 * the event decision tree.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing the active event
	 */
	private void drawSecondChoiceButtonEvent(Graphics2D graphics, GameData data) {
		var img2 = imgLoader.bgImages().get("BG_CHOICE2");
		var event2 = MathLoader.getMapEvent().get("BG_CHOICE2");
		int width = event2.box().southEast().x() - event2.box().northWest().x();
		int height = event2.box().southEast().y() - event2.box().northWest().y();
		graphics.drawImage(img2, event2.transform(), null);
		drawText(graphics, data.event().getRoot().getChoice2().getAnswer(), event2.box().northWest().x() + width / 2, (int) (event2.box().northWest().y() + height * 0.5), 30);
	}

	/**
	 * Draw the text inside each choice. <br>
	 * The text is draw at the center of the box choice.
	 * 
	 * @param graphics {@code Graphics2D} of the game.
	 * @param content  Text we wants to write.
	 * @param x        The x coordinate center of the text
	 * @param y        The y coordinate center of the text
	 * @param maxChar  Number of char max per line
	 */
	private void drawText(Graphics2D g, String content, int x, int y, int max) {
		FontMetrics fm = g.getFontMetrics();
		var words = content.split(" ");
		var lines = new ArrayList<String>();
		var line = new StringBuilder();
		for (var w : words) {
			if (line.length() + w.length() + 1 > max) {
				lines.add(line.toString());
				line = new StringBuilder(w);
			} else {
				line.append(line.isEmpty() ? w : " " + w);
			}
		}
		if (!line.isEmpty()) {
			lines.add(line.toString());
		}
		int y0 = y + lines.size() * fm.getAscent() / 2;
		for (int i = 0; i < lines.size(); i++) {
			var l = lines.get(lines.size() - 1 - i);
			g.drawString(l, x - fm.stringWidth(l) / 2, y0 - i * fm.getAscent());
		}
	}

	/**
	 * Draws the bin button in either open or closed state.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data indicating bin state
	 */
	private void drawBinButton(Graphics2D graphics, GameData data) {
		BufferedImage img = imgLoader.bgImages().get(data.getBin() ? "BG_BIN_OPEN" : "BG_BIN_CLOSE");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG_BIN_CLOSE").transform(), null);
	}

	/**
	 * Draws the shop interface, including the shop background, NPC, exit icon, item
	 * display, buttons, and logs.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data including the shop information
	 */
	private void drawShop(Graphics2D graphics, GameData data) {
		BufferedImage img = imgLoader.bgImages().get("BG_SHOP");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG_SHOP").transform(), null);
		img = imgLoader.bgImages().get("RolandBody");
		graphics.drawImage(img, MathLoader.getMapEvent().get("RolandBody").transform(), null);
		img = imgLoader.bgImages().get("ICON_EXIT_SHOP");
		graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_EXIT_SHOP").transform(), null);
		drawTextBubble(graphics, data.getShopLst());
		if (data.getShopLst().getCurrentShop().isEmpty()) {
			drawNoItemShop(graphics);
		} else {
			drawArticle(graphics, data.getShopLst());
		}
		drawButtonShop(graphics);
		drawSellArticle(graphics);
	}

	/**
	 * Draws the dialogue bubble for the shop, showing the latest shop log messages.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing the shop log
	 */
	private void drawTextBubble(Graphics2D graphics, Shop shop) {
		var bubbleBox = MathLoader.getMapEvent().get("BG_SHOP_BUBBLE").box();
		int centerX = (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2;
		int centerY = (bubbleBox.southEast().y() - bubbleBox.northWest().y()) / 2;
		int x = bubbleBox.northWest().x() + centerX;
		int y = bubbleBox.northWest().y() + centerY;
		graphics.setColor(Color.WHITE);
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
		graphics.setFont(font);
		drawText(graphics, shop.getLogShop(), x, y, 30);
	}

	/**
	 * Draws a "no items available" indicator in the shop.
	 *
	 * @param graphics The graphics context used for rendering
	 */
	private void drawNoItemShop(Graphics2D graphics) {
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

	/**
	 * Draws the left, right, and buy buttons in the shop interface.
	 *
	 * @param graphics The graphics context used for rendering
	 */
	private void drawButtonShop(Graphics2D graphics) {
		var img = imgLoader.bgImages().get("ICON_SHOP_LEFT");
		graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_LEFT").transform(), null);
		img = imgLoader.bgImages().get("ICON_SHOP_RIGHT");
		graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_RIGHT").transform(), null);
		img = imgLoader.bgImages().get("ICON_SHOP_BUY");
		graphics.drawImage(img, MathLoader.getMapEvent().get("ICON_SHOP_BUY").transform(), null);
	}

	/**
	 * Draws the sell button for selling items in the shop.
	 *
	 * @param graphics The graphics context used for rendering
	 */
	private void drawSellArticle(Graphics2D graphics) {
		var img = imgLoader.bgImages().get("ICON_SELL_BUTTON");
		graphics.drawImage(img, MathLoader.getMapEvent().get("SHOP_SELL_ARTICLE").transform(), null);
	}

	/**
	 * Draws a shop item including its image, name, and pricing information.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param shop     The current Shop data
	 */
	private void drawArticle(Graphics2D graphics, Shop shop) {
		Iterator<Map.Entry<Item, Integer>> it = shop.getCurrentShop().entrySet().iterator();
		Map.Entry<Item, Integer> entry = it.next();
		Item item = entry.getKey();
		int price = entry.getValue();
		var imageBubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_IMAGE_HOLDER").box();
		if (imgLoader.itemImagesByID().get(item.info().ID()) == null) {
			throw new IllegalArgumentException("Can't fint img : " + item.toString());
		}
		drawItemShopImage(graphics, imgLoader.itemImagesByID().get(item.info().ID()), imageBubbleBox);
		var titleBubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_NAME_HOLDER").box();
		drawItemShopName(graphics, item, titleBubbleBox);
		drawItemShopInfo(graphics, item, price, shop.getCurrentShop().size());
	}

	/**
	 * Draws an item image in its designated shop bubble.
	 *
	 * @param graphics  The graphics context used for rendering
	 * @param img       The image of the item
	 * @param bubbleBox The bounding box where the item is drawn
	 */
	private void drawItemShopImage(Graphics2D graphics, BufferedImage img, BoundingBox bubbleBox) {
		XY northWest = bubbleBox.northWest();
		XY southEast = bubbleBox.southEast();
		double width = southEast.x() - northWest.x();
		double height = southEast.y() - northWest.y();
		drawElement(graphics, img, northWest.x(), northWest.y(), width, height, Direction.UP);
	}

	/**
	 * Draws the item name in the shop with color based on its rarity.
	 *
	 * @param graphics       The graphics context used for rendering
	 * @param item           The item whose name is drawn
	 * @param titleBubbleBox The bounding box for the item name display
	 */
	private void drawItemShopName(Graphics2D graphics, Item item, BoundingBox titleBubbleBox) {
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
		graphics.setFont(font);
		graphics.setColor(switch (item.info().rarity()) {
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

	/**
	 * Draws the item pricing and remaining quantity in the shop.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param item     The item being displayed
	 * @param price    The price of the item in gold
	 * @param nbItem   Number of remaining items in the shop
	 */
	private void drawItemShopInfo(Graphics2D graphics, Item item, int price, int nbItem) {
		var bubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_IMAGE_HOLDER").box();
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH3());
		graphics.setFont(font);
		graphics.setColor(Color.BLACK);
		FontMetrics fm = graphics.getFontMetrics();
		int centerX = bubbleBox.northWest().x() + (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2;
		int centerY = bubbleBox.southEast().y() - fm.getAscent() / 2;
		drawText(graphics, "Price : " + price + " Golds", centerX, centerY + (int) (fm.getAscent() * 0.2), 20);
		graphics.setColor(Color.YELLOW);
		drawText(graphics, "Price : " + price + " Golds", centerX, centerY, 20);
		bubbleBox = MathLoader.getMapEvent().get("SHOP_ARTICLE_NAME_HOLDER").box();
		centerX = bubbleBox.northWest().x() + (bubbleBox.southEast().x() - bubbleBox.northWest().x()) / 2;
		centerY = bubbleBox.northWest().y();
		graphics.setColor(Color.WHITE);
		drawText(graphics, "Items restants : " + nbItem, centerX, centerY - fm.getAscent() / 2, 20);
	}

	/**
	 * Draws the main elements of the hero's backpack interface in the game.
	 *
	 * This includes: - The backpack UI frame and background - The grid representing
	 * inventory slots - All items currently in the backpack - The detailed item
	 * information panel - The bin button for discarding items
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing the backpack and hero
	 *                 information
	 */
	private void drawBackpackElementGame(Graphics2D graphics, GameData data) {
		drawGrid(graphics, data);
		drawItemBag(graphics, data);
		drawItemInfo(graphics, data);
		drawBinButton(graphics, data);
	}

	/**
	 * Methods for drawing the game
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 */
	private void draw(Graphics2D graphics, GameData data) {
		drawBG(graphics, data);
		if (data.mapOrBag()) {
			drawBackpackElementGame(graphics, data);
			if (data.getShop()) {
				drawShop(graphics, data);
			}
			if (GameDataCombat.combat()) {
				updateCombat(graphics, data, GameDataCombat.getLstEnemy());
			}
			if (!GameDataClick.getDragItemMap().isEmpty()) {
				updateDragItem(graphics, data);
			}
		} else {
			drawMap(graphics, data);
		}
		drawHero(graphics, data);
		drawButton(graphics, data);
		if (data.event() != null) {
			drawEvent(graphics, data);
		}
	}

	/**
	 * Methods for drawing the game
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData containing the game data.
	 */
	private void drawLobby(Graphics2D graphics, GameData data) {
		BufferedImage img = imgLoader.bgImages().get("BG_LOBBY");
		graphics.drawImage(img, MathLoader.getMapEvent().get("BG_LOBBY").transform(), null);
		drawButtonLobby(graphics);
		if (data.getScore() != 0) {
			var scoreFinal = (int) (data.getScore() * (1.2 * data.floor() + (data.hero().getLevel() / 2.0)));
			Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
			graphics.setFont(font);
			graphics.setColor(Color.WHITE);
			FontMetrics fm = graphics.getFontMetrics();
			graphics.drawString("SCORE FINAL : " + scoreFinal, width / 2 - (fm.stringWidth("SCORE FINAL : " + scoreFinal)) / 2, height / 2);
		}
		if (data.getScoreLobby()) {
			drawScore(graphics, data);
		}
	}

	/**
	 * Draws the Hall of Fame / Score screen.
	 *
	 * It renders the score background and reads the scores from the "data/score"
	 * file, displaying them in the graphics context.
	 *
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data containing player information
	 */
	public void drawScore(Graphics2D graphics, GameData data) {
		BufferedImage img = imgLoader.bgImages().get("BG_SCORE");
		graphics.drawImage(img, MathLoader.getMapEvent().get("HOF").transform(), null);
		Path scoreFile = Path.of("data", "score");
		try {
			printAllLines(scoreFile, graphics, data);
		} catch (IOException e) {
			IO.println("Impossible de lire le fichier des scores.");
		}
	}

	/**
	 * Reads all lines from the given score file and draws them.
	 *
	 * Only up to 9 lines are drawn. If the file is empty, "Aucun score" is
	 * displayed.
	 *
	 * @param path     The path to the score file
	 * @param graphics The graphics context used for rendering
	 * @param data     The current game data (used for layout)
	 * @throws IOException if reading the file fails
	 */
	private void printAllLines(Path path, Graphics2D graphics, GameData data) throws IOException {
		int nbScore = 0;
		var boundingBox = MathLoader.getMapEvent().get("HOF").box();
		int size = FontLoader.getH2();
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH2());
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		try (var reader = Files.newBufferedReader(path)) {
			String line;
			for (nbScore = 0; nbScore < 9;) {
				if ((line = reader.readLine()) == null) {
					break;
				}
				graphics.drawString(line, boundingBox.northWest().x() + (int) (width * 0.01), boundingBox.northWest().y() + (int) (height * 0.01) + size * (nbScore + 1));
				nbScore++;
			}
		}
		if (nbScore == 0) {
			graphics.drawString("Aucun score", boundingBox.northWest().x(), boundingBox.northWest().y() + size);
		}
	}

	/**
	 * Draws the lobby buttons on the screen.
	 *
	 * Buttons include: - START_GAME: "DEBUTER" - HOF_BUTTON: "HALL OF FAME" -
	 * LEAVE_BUTTON: "QUITTER"
	 *
	 * @param graphics The graphics context used for rendering
	 */
	private void drawButtonLobby(Graphics2D graphics) {
		Font font = new Font("Mikodacs", Font.PLAIN, FontLoader.getH1());
		graphics.setFont(font);
		var boundingBox = MathLoader.getMapEvent().get("START_GAME").box();
		graphics.setColor(Color.WHITE);
		graphics.drawString("DEBUTER", boundingBox.northWest().x(), boundingBox.southEast().y());
		boundingBox = MathLoader.getMapEvent().get("HOF_BUTTON").box();
		graphics.drawString("HALL OF FAME", boundingBox.northWest().x(), boundingBox.southEast().y());
		boundingBox = MathLoader.getMapEvent().get("LEAVE_BUTTON").box();
		graphics.drawString("QUITTER", boundingBox.northWest().x(), boundingBox.southEast().y());
	}

	/**
	 * Renders the game frame by calling the view's draw method.
	 *
	 * @param context The application context used for rendering
	 * @param data    The current game data
	 * @param view    The view responsible for drawing the game
	 */
	public static void draw(ApplicationContext context, GameData data, GameView view) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		Objects.requireNonNull(view);
		context.renderFrame(graphics -> view.draw(graphics, data));
	}

	/**
	 * Renders the lobby frame by calling the view's drawLobby method.
	 *
	 * @param context The application context used for rendering
	 * @param data    The current game data
	 * @param view    The view responsible for drawing the game
	 */
	public static void drawLobby(ApplicationContext context, GameData data, GameView view) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		Objects.requireNonNull(view);
		context.renderFrame(graphics -> view.drawLobby(graphics, data));
	}

}
