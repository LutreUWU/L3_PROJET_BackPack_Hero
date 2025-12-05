package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import game.data.GameDataBackpack;
import game.data.GameDataClick;
import game.data.GameDataCombat;
import loader.FontLoader;
import model.Gold;
import model.Item;
import model.XY;
import model.item.common.KeyDoor;
import model.item.common.Sword;
import model.item.rare.Gant;
import model.item.superrare.Massue;
import model.map.EnemyRoom;
import model.map.EventRoom;
import model.map.Exit;
import model.map.Healer;
import model.map.LockedDoor;
import model.monster.Chicken;
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
	private static boolean gameLoop(ApplicationContext context, GameData data) {
		Event event = context.pollOrWaitEvent(10);
		if (event != null) switch(event) {
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
//							IO.println("ETAPE 0");
//							IO.println("ID ACTUEL : " + currentItem.getID());
							/*
							if (currentItem.getID() == 1 && data.bag().bagItemLst().contains(new KeyDoor())) { // It's a key
//								IO.println("ETAPE 1");
								var currentPos = data.map().getHeroPos();
								switch(data.map().getGrid()[currentPos.y()][currentPos.x()]) {
									case LockedDoor lockedDoor ->  {
//										IO.println("ETAPE 2");
										lockedDoor.unlock();
										data.map().updateMap(currentPos);
										GameDataBackpack.removeItemFromBackpack(currentItem);
									}
								default -> {IO.println("ETAPE 3");}
								}
							}*/
						}
						case MAP_OR_BAG -> {
							if (!GameDataCombat.combat() && data.dragItem() == null && data.event() == null) {
								data.swapMapOrBag();
							}
						}
						case BAG -> {
							if (data.mapOrBag() && GameDataCombat.combat()) {
								GameDataCombat.heroAction(data, (XY) res.value());
							}
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
									data.map().setHeroPos(coord);

									var coordHero = new XY(data.map().getHeroPos().x(), data.map().getHeroPos().y());
									switch (data.map().getGrid()[coordHero.y()][coordHero.x()]) {
									case EnemyRoom room -> {
										data.swapMapOrBag();
										GameDataCombat.startCombat(new ArrayList<>(List.of(new Chicken(), new Chicken())), data);
										data.map().updateMap(coord);
									}
									case EventRoom eventRoom -> {
										if (!eventRoom.getAlreadyVisited()) {
											var linkedEvent = eventRoom.getEvent();
											data.inEvent(linkedEvent);
											data.map().updateMap(coord);
											eventRoom.visitedEvent();
										}
									}
									case LockedDoor roomDoor -> { if (roomDoor.getLock()) {
																								var linkedEvent = roomDoor.getEvent();
																								data.inEvent(linkedEvent);}
																							}
									case Healer healerRoom -> { if (!healerRoom.getAlreadyVisited()) {
										var linkedEvent = healerRoom.getEvent();
										data.inEvent(linkedEvent);
										data.map().updateMap(coord);}
									}
									case Exit roomExit -> {var linkedEvent = roomExit.getEvent();
																		data.inEvent(linkedEvent);
																		data.map().updateMap(coord);}
									default -> {IO.println(coord);
															data.map().updateMap(coord);}
									}
								}
							}
						}
						}
					}

					if (pointerEvent.action() == PointerEvent.Action.POINTER_MOVE) {
						if (data.dragItem() != null) {
							GameDataClick.moveDragItem(data.dragItem(), pointerEvent.location().x(), pointerEvent.location().y());
							GameDataClick.setOldPosition(pointerEvent.location().x(), pointerEvent.location().y());
						}
					}
					if (data.dragItem() != null && pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
						int x = pointerEvent.location().x();
						int y = pointerEvent.location().y();
						var res = GameDataClick.bagClick(x, y);
						if (res.x() != -1) {
							data.dragItem().setXY(GameDataClick.bagClick(x, y));
							if (GameDataBackpack.addItemToBackpack(data.dragItem())) {
								data.removeItemFromDrag(data.dragItem());
							} else {
								GameDataClick.addDragItem(data.dragItem());
							}
						}
						data.setDragItem(null);
					}
					GameView.draw(context, data);
					data.setMouseCoord(new XY(pointerEvent.location().x(), pointerEvent.location().y()));
		}
		default -> IO.println(event);
		}
		// If event button is pressed
		
		if (event != null) switch (event) {
		case KeyboardEvent key -> {
			if (key.action() == KeyboardEvent.Action.KEY_RELEASED) {
				switch (key.key()) {
				case Key.A -> {
					if (data.dragItem() == null && !GameDataCombat.combat() && data.mapOrBag()) {
						GameDataClick.addDragItem(new KeyDoor());
					}
				}
				// Start a combat against a RAT
				case Key.I -> {
					if (GameDataCombat.combat() == false) {
						GameDataCombat.startCombat(new ArrayList<>(List.of(new Chicken(), new Soldat(), new model.monster.Robot())),
								data);
					}
				}

				case Key.R -> {
					if (data.dragItem() != null) {
						GameData.rotateItem(data.dragItem());
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
				GameView.draw(context, data);
			}
		}
		default -> {}
		}
		return true;
	}

	/**
	 * Sets up the game, then launches the game loop.
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 */
	private static void memoryGame(ApplicationContext context) {
		var screenInfo = context.getScreenInfo();
		var data = new GameData(screenInfo);
		FontLoader.load_font();
		GameView.initGameGraphics(screenInfo.width(), screenInfo.height(), data.bag().getGridSize());
		GameView.draw(context, data);
		while (true) {
			if (!gameLoop(context, data)) {
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
