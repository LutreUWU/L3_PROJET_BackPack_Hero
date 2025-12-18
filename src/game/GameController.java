package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import game.data.GameDataShop;
import loader.FontLoader;
import model.Item;
import model.XY;
import model.item.common.Arrow;
import model.item.common.Gold;
import model.item.common.KeyDoor;
import model.item.epic.Bow;
import model.item.epic.DespairShield;
import model.item.legendary.Axe;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.map.Shop;
import model.map.Treasure;
import model.monster.Chicken;
import model.monster.Robot;
import model.monster.Soldat;

/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 * 
 * GUIDE :
 * 
 * Key.A to create an Item in the bag (It's just for testing, it'll be useless
 * for the end) - Key.(ZQSD) to move the item in the grid - Key.R to rotate the
 * item clockwise - Key.ESCAPE (esc) to confirm and add the item in the backpack
 * 
 * Key.I to initiate a combat - Using an item cost 1 AP, when it reach 0, enemy
 * play. - Click on a item to use it
 * 
 * TO DO : - LA MAP ZEBI
 */
public class GameController {
	/**
	 * Default constructor, which does basically nothing.
	 */
	public GameController() {
	}

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
		if (event != null)
			switch (event) {
			case PointerEvent pointerEvent -> {
				// If we click down on the screen
				if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) {
					var res = GameDataClick.click(pointerEvent.location().x(), pointerEvent.location().y());
					switch (res.type()) {
					case ITEM -> {
						var currentItem = (Item) res.value();
						data.setDragItem(currentItem);
						GameDataClick.setOldPosition(pointerEvent.location().x(), pointerEvent.location().y());
						GameDataClick.updateBoundingBox(data.dragItem(), pointerEvent.location().x(), pointerEvent.location().y());
					}
					case MAP_OR_BAG -> {
						if (!data.getShop() && !GameDataCombat.combat() && data.dragItem() == null && data.event() == null) {
							data.swapMapOrBag();
						}
					}
					case BAG -> {
						if (data.mapOrBag() && GameDataCombat.combat()) {
							GameDataCombat.heroAction(data, (XY) res.value());
						}
						data.bag().unlockCaseBackpack((XY) res.value());
					}
					case EVENT_CHOICE -> {
						if ((int) res.value() == 1) {
							data.event().choose1(data);
						}
						if ((int) res.value() == 2) {
							data.event().choose2(data);
						}
						if ((int) res.value() == 3) { // Quand on clique sur le bouton de fin
							// Ajouter les conséquences de fin d'event
							data.event().restartEvent();
							data.outEvent();
						}
						;

					}
					case MAP -> {
						var coord = (XY) res.value();
						if (coord.x() != -1 && coord.y() != -1) {
							if (data.map().getHeroAccessible().contains(coord) || data.map().getHeroVisited().contains(coord)) {
								var shortestPath = data.map().heroShortestPath(data.map().getHeroPos(), coord);
								data.setShortestPath(shortestPath);
								IO.println(shortestPath);
								IO.println(shortestPath.stream()
																				.map(XY::toString)
																				.collect(Collectors.joining(" --> ")));
								data.map().setHeroPos(coord);
								var coordHero = new XY(data.map().getHeroPos().x(), data.map().getHeroPos().y());
								switch (data.map().getGrid()[coordHero.y()][coordHero.x()]) {
								case EnemyRoom room -> {
									if (!room.getAlreadyVisited()) {
										data.swapMapOrBag();
										GameDataCombat.startCombat(new ArrayList<>(List.of(new Chicken(), new Chicken())), data);
										data.map().updateMap(coord);
										room.nowVisited();
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
									data.setShop(true);
									new GameDataShop(shop, true, data);
									data.map().updateMap(coord);
								}
								default -> {}
								}
							}
						}
					}
					case NOTHING -> {
					}
					default -> throw new IllegalArgumentException("Unexpected value: " + res.type());
					}
				}

				if (pointerEvent.action() == PointerEvent.Action.POINTER_MOVE) {
					if (data.dragItem() != null) {
						GameDataClick.moveDragItem(data.dragItem(), pointerEvent.location().x(), pointerEvent.location().y());
						GameDataClick.setOldPosition(pointerEvent.location().x(), pointerEvent.location().y());
						GameDataClick.binHover(pointerEvent.location().x(), pointerEvent.location().y());
					}
				}
				if (data.dragItem() != null && pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
					int x = pointerEvent.location().x();
					int y = pointerEvent.location().y();
					GameDataClick.clickUp(x, y);
					data.setDragItem(null);
				}
				GameView.draw(context, data, view);
				data.setMouseCoord(new XY(pointerEvent.location().x(), pointerEvent.location().y()));
			}
			default -> {
			}
			}
		// If event button is pressed

		if (event != null)
			switch (event) {
			case KeyboardEvent key -> {
				if (key.action() == KeyboardEvent.Action.KEY_RELEASED) {
					switch (key.key()) {
					case Key.A -> {
						if (data.dragItem() == null && !GameDataCombat.combat() && data.mapOrBag()) {
							GameDataClick.addDragItem(new Gold(12));
							GameDataClick.addDragItem(new Bow());
							GameDataClick.addDragItem(new Arrow());
						}
					}
					case Key.I -> {
						if (GameDataCombat.combat() == false) {
							GameDataCombat.startCombat(new ArrayList<>(List.of(new Soldat(), new Robot())), data);
						}
					}

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
					default -> {
					}
					}
					GameView.draw(context, data, view);
				}
			}
			default -> {
			}
			}
		return true;
	}

	/**
	 * Sets up the game, then launches the game loop.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 */
	public static void memoryGame(ApplicationContext context) {
		var screenInfo = context.getScreenInfo();
		var data = new GameData(screenInfo);
		FontLoader.load_font(screenInfo);
		var view = GameView.initGameGraphics(screenInfo.width(), screenInfo.height(), data.bag().getGridSize());
		GameView.draw(context, data, view);
		while (true) {
			if (!gameLoop(context, data, view)) {
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
		Application.run(Color.WHITE, GameController::memoryGame);
	}
}
