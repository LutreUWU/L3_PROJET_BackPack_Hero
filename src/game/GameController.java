package game;

import java.awt.Color;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;
import com.github.forax.zen.PointerEvent.Location;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import game.data.GameDataShop;
import loader.FontLoader;
import loader.ImageLoader;
import loader.MathLoader;
import model.Curse;
import model.Item;
import model.XY;
import model.item.epic.EnchantedDiamondSword;
import model.item.legendary.Axe;
import model.item.rare.Cookie;
import model.item.rare.ManaStone;
import model.item.superrare.Bomb;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Shop;
import model.map.Treasure;
import model.monster.Gnome;
import model.monster.Robot;
import model.monster.Soldat;

/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 */
public class GameController {
	private static boolean inLobby = true;
	/**
	 * Default constructor, which does basically nothing.
	 */
	public GameController() {}
	/**
	 * Goes once in the game loop, which consists in retrieving user actions,
	 * transmitting it to the GameView and GameData, and dealing with time events.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param data    GameData of the game.
	 * @return True if the game continue, False if we press the button to stop
	 */
	private static boolean gameLoop(ApplicationContext context, GameData data, GameView view) {
		Event event = context.pollOrWaitEvent(10);
		if (event != null) {
			switch (event) {
				// Mouse event
				case PointerEvent pointerEvent -> {
					checkPointerEvent(data, pointerEvent);
					GameView.draw(context, data, view);
				}
				// Keyboard event
				case KeyboardEvent key -> {
					if (key.action() == KeyboardEvent.Action.KEY_RELEASED) {
						if (!checkKeyEvent(data, key.key())) {
							return false;
						};
						GameView.draw(context, data, view);
					}
				}
				default -> {}
			}
		}
		return true;
	}

	/**
	 * Method dealing with all events about the mouse in game.
	 * It checks and apply all event, when we pressed, moved, released the mouse.
	 * 
	 * @param data					{@code GameData} of the game.
	 * @param pointerEvent	{@code pointerEvent} to know which type of mouse event it is.
	 */
	private static void checkPointerEvent(GameData data, PointerEvent pointerEvent) {
		switch(pointerEvent.action()) {
			case POINTER_DOWN -> checkPointerDown(data, pointerEvent.location());
			case POINTER_MOVE -> checkPointerMove(data, pointerEvent.location());
			case POINTER_UP -> checkPointerUp(data, pointerEvent.location());
			default ->{}
		}
		data.setMouseCoord(new XY(pointerEvent.location().x(), pointerEvent.location().y()));
	}
	
	/**
	 * Check where the mouse is when we click down,
	 * and update the game in consequence.
	 * 
	 * @param data		{@code GameData} of the game.
	 * @param mouse		{@code Location} containing the coordinate of the mouse.
	 */
	private static void checkPointerDown(GameData data, Location mouse) {
		var res = GameDataClick.click(mouse.x(), mouse.y());
		switch (res.type()) {
		case ITEM -> newDragItem(data, (Item) res.value(), mouse.x(), mouse.y());
		case MAP_OR_BAG -> swapMapOrBag(data);
		case BAG -> actionBag(data, (XY) res.value());
		case EVENT_CHOICE -> applyEvent(data, (int) res.value());
		case MAP -> applyMap(data, (XY) res.value());
		case NOTHING -> {}
		default -> throw new IllegalArgumentException("Unexpected value: " + res.type());
		}
	}
	
	/**
	 * On va vraiment faire une méthode pour chaque salle ????
	 * 
	 * @param data		{@code GameData} of the game.
	 * @param coord		{@code XY} containing the coordinate of the cell we clicks on the map.
	 */
	private static void applyMap(GameData data, XY coord) {
		if (coord != null) {
			if (data.map().getHeroAccessible().contains(coord) || data.map().getHeroVisited().contains(coord)) {
				var shortestPath = data.map().heroShortestPath(data.map().getHeroPos(), coord);
				data.setShortestPath(shortestPath);
				data.map().setHeroPos(coord);
		
				var coordHero = new XY(data.map().getHeroPos().x(), data.map().getHeroPos().y());
				switch (data.map().getGrid()[coordHero.y()][coordHero.x()]) {
				case EnemyRoom enemyRoom -> {
					if (!enemyRoom.getAlreadyVisited()) {
						data.swapMapOrBag();
						GameDataCombat.startCombat(enemyRoom.getLstEnemy(), data);
						data.map().updateMap(coord);
						enemyRoom.nowVisited();
					}
				}
				case EventRoom eventRoom -> {
					if (!eventRoom.getAlreadyVisited()) {
						var linkedEvent = eventRoom.getEvent();
						data.inEvent(linkedEvent);
						data.map().updateMap(coord);
						eventRoom.visitedEvent();
					}
				}
				case LockedDoor roomDoor -> {
					if (roomDoor.getLock()) {
						var linkedEvent = roomDoor.getEvent();
						data.inEvent(linkedEvent);
					}
				}
				case Healer healerRoom -> {
					if (!healerRoom.getAlreadyVisited()) {
						var linkedEvent = healerRoom.getEvent();
						data.inEvent(linkedEvent);
						data.map().updateMap(coord);
					}
				}
				case Treasure treasure -> {
					if (!treasure.getAlreadyVisited()) {
						var linkedEvent = treasure.getEvent();
						data.inEvent(linkedEvent);
						data.map().updateMap(coord);
					}
				}
				case Exit roomExit -> {
					var linkedEvent = roomExit.getEvent();
					data.inEvent(linkedEvent);
					data.map().updateMap(coord);
				}
				case Shop shop -> {
					data.setShop(true, shop);
					new GameDataShop(shop, true, data);
					data.map().updateMap(coord);
				}
				default -> {}
				}
			}
		}	
	}
	
	/**
	 * Methods dealing with event in dungeon.
	 * All events in game has two choices (1 or 2) and a "end choice" (3) to end the event.
	 * 
	 * @param data	{@code GameData} of the game.
	 * @param int	  1 is choice1
	 * 							2 is choice2
	 * 							3 is end button event
	 */
	private static void applyEvent(GameData data, int choice) {
		if (choice == 1) {
			data.event().choose1(data);
		}
		if (choice== 2) {
			data.event().choose2(data);
		}
		if (choice == 3) {
			data.event().restartEvent();
			data.outEvent();
		}
	}
	
	/**
	 * Method adding a new item in the list of draggable item.
	 * Since the item can be everywhere in the screen, we need the mouse coordinate
	 * 
	 * @param data	 {@code GameData} of the game.
	 * @param item   {@code Item} we wants to add.
	 * @param mouseX coordX of the mouse.
	 * @param mouseY coordY of the mouse.
	 */
	private static void newDragItem(GameData data, Item item, int mouseX, int mouseY) {
		data.bag().updateManaConnected();
		if (data.bag().bagItemLst().contains(item)) {
			switch(item) {
			case Curse _ -> {}
			default -> {
				data.setDragItem(item);
				GameDataClick.setOldPosition(mouseX, mouseY);
				GameDataClick.updateBoundingBox(data.dragItem(),mouseX, mouseY);
			}
			}
		} else {
			data.setDragItem(item);
			GameDataClick.setOldPosition(mouseX, mouseY);
			GameDataClick.updateBoundingBox(data.dragItem(),mouseX, mouseY);
		}
	}
	
	/**
	 * Method that permits to switch between the bag and the map in game.
	 * 
	 * @param data	 {@code GameData} of the game.
	 */
	private static void swapMapOrBag(GameData data) {
		if (!data.getShop() && !GameDataCombat.combat() && data.dragItem() == null && data.event() == null) {
			data.swapMapOrBag();
		}
	}
	
	/**
	 * Method that treats all event when clicking in the bag.
	 * Treating when using an item in combat.
	 * Treating when unlocking a box in the bag.
	 * 
	 * @param data	 {@code GameData} of the game.
	 * @param coord	 {@code XY} of the grid we click
	 */
	private static void actionBag(GameData data, XY coord) {
		if (GameDataCombat.combat()) {
			var item = data.bag().getItem(coord.x(), coord.y());
			if (item != null) {
				if (GameDataCombat.getHoverItem() == null || item != GameDataCombat.getHoverItem()) {
					GameDataCombat.setHoverItem(item);
				}
				else {
					GameDataCombat.heroAction(data);
					GameDataCombat.setHoverItem(null);
				}
			}
		}
		data.bag().unlockCaseBackpack(coord);
	}
	
	/**
	 * Check and trigger all events when we're moving the mouse
	 * 
	 * @param data	{@code GameData} of the game.
	 * @param mouse	{@code Location} containing the coordinate of the mouse.
	 */
	private static void checkPointerMove(GameData data, Location mouse) {
		if (data.dragItem() != null) {
			if (data.getShop()) {
				GameDataClick.sellButtonHover(mouse.x(), mouse.y());
			}
			GameDataClick.moveDragItem(data.dragItem(), mouse.x(), mouse.y());
			GameDataClick.setOldPosition(mouse.x(), mouse.y());
			GameDataClick.binHover(mouse.x(), mouse.y());
		}
	}
	
	/**
	 * Check and trigger all event when we're releasing the mouse
	 *
	 * @param data	{@code GameData} of the game.
	 * @param mouse	{@code Location} containing the coordinate of the mouse.
	 */
	private static void checkPointerUp(GameData data, Location mouse) {
		if(data.dragItem() != null) {
			int x = mouse.x();
			int y = mouse.y();
			GameDataClick.clickUp(x, y);
			data.setDragItem(null);
			data.bag().updateManaConnected();
		}
	}
	
	/**
	 * Methods dealing with all event regarding the keyboard.
	 * We use the keyboard when rotating an item and if we wants to leave the game.
	 * 
	 * @param data	{@code GameData} of the game.
	 * @param mouse	{@code Key} containing which key we pressed
	 * 
	 * @return false if we pressed E for leaving
	 * 				 true otherwise
	 */
	private static boolean checkKeyEvent(GameData data, Key key) {
		switch (key) {
		// A ENLEVER CAR UTILE SEULEMENT POUR LES TEST
		case Key.A -> {
			if (data.dragItem() == null && !GameDataCombat.combat() && data.mapOrBag()) {
				 GameDataClick.addDragItem(new Axe());
				 GameDataClick.addDragItem(new ManaStone(2));
				 GameDataClick.addDragItem(new EnchantedDiamondSword());
				 GameDataClick.addDragItem(new Cookie());
			}
		}
		case Key.I -> {
			if (GameDataCombat.combat() == false) {
				GameDataCombat.startCombat(new ArrayList<>(List.of(new Soldat(), new Robot(), new Gnome())), data);
			}
		}
		///////////////////////////////////////////////////////////////
		case Key.R -> {
			if (data.dragItem() != null) {
				GameDataClick.removeItemFromDrag(data.dragItem());
				data.setDragItem(GameData.rotateItem(data.dragItem()));			
				GameDataClick.addDragItem(data.dragItem());
				GameDataClick.updateBoundingBox(data.dragItem(), data.getMouseCoord().x(), data.getMouseCoord().y());
			}
		}
		// Leave the game
		case Key.E -> {
			return false;
		}
		default -> {}
		}
		return true;
	}
	
	private static int gameLoopLobby(ApplicationContext context, GameData data, GameView view) {
		Event event = context.pollOrWaitEvent(10);
		if (event != null) {
			switch (event) {
				// Mouse event
				case PointerEvent pointerEvent -> {
					switch(pointerEvent.action()) {
					case POINTER_UP -> {
						int res = checkPointerUpLobby(context, pointerEvent.location());
						switch(res) {
						case 0 -> {
							data.setScore(true);
							GameView.drawLobby(context, data, view);
						}
						case 1 -> {
							GameView.draw(context, data, view);
							inLobby = false;
						}
						
						}
						return res;
					}
					default -> {}
					
					}
				}
				default -> {}
			}
		}
		return 1;
	}
	
	/**
	 * Check and trigger all event when we're releasing the mouse
	 *
	 * @param data	{@code GameData} of the game.
	 * @param mouse	{@code Location} containing the coordinate of the mouse.
	 */
	private static int checkPointerUpLobby(ApplicationContext context, Location mouse) {
		var boundingBoxStart = MathLoader.getMapEvent().get("START_GAME").box();
		var boundingBoxHOF = MathLoader.getMapEvent().get("HOF_BUTTON").box(); // TO DO
		var boundingBoxLeave = MathLoader.getMapEvent().get("LEAVE_BUTTON").box(); // TO DO
		int x = mouse.x();
		int y = mouse.y();
		if (boundingBoxStart.northWest().x() <= x && boundingBoxStart.southEast().x() >= x &&
				boundingBoxStart.northWest().y() <= y && boundingBoxStart.southEast().y() >= y) {
			return 1;
		}
		if (boundingBoxHOF.northWest().x() <= x && boundingBoxHOF.southEast().x() >= x &&
				boundingBoxHOF.northWest().y() <= y && boundingBoxHOF.southEast().y() >= y) {
			return 0;
		}
		if (boundingBoxLeave.northWest().x() <= x && boundingBoxLeave.southEast().x() >= x &&
				boundingBoxLeave.northWest().y() <= y && boundingBoxLeave.southEast().y() >= y) {
			return -1;
		}
		return -2;
	}
	
	/**
	 * Sets up the game, then launches the game loop.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 */
	public static void memoryGame(ApplicationContext context) {
		var screenInfo = context.getScreenInfo();
		var data = new GameData(screenInfo);
		var imageLoader = new ImageLoader();
		FontLoader.load_font(screenInfo);
		new MathLoader(data, imageLoader);
		var view = GameView.initGameGraphics(screenInfo.width(), screenInfo.height(), data.bag().getGridSize(), imageLoader);
		GameView.drawLobby(context, data, view);
		int n = 1; 
		while (true) {
			if (inLobby) {
				n = gameLoopLobby(context, data, view);
			}
			else {
				if (!gameLoop(context, data, view)) {
					GameView.drawLobby(context, data, view);
					inLobby = true;
					// A enlever, car théoriquement, on retourne aux lobby seulement si le héro meurt
					// Si tu veux quitter directement avec E à l'intérieur du jeu, faut rajouter ça :
					// n = -1;
				}
			}
			if (n == -1) {
				System.out.println("Thank you for quitting!");
				context.dispose();
				return;
			}
		}
	}
	
	/**
	 * Executable program.
	 * 
	 * @param args Spurious arguments.
	 */
	public static void main(String[] args) {
		Application.run(Color.GRAY, GameController::memoryGame);
	}
}
