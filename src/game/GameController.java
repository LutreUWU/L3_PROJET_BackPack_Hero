package game;

import java.awt.Color;
import java.awt.Robot;
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
import model.Item;
import model.XY;
import model.map.EnemyRoom;
import model.map.LockedDoor;
import model.monster.Chicken;
import model.monster.Soldat;
import model.weapon.Sword;

/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 * 
 * GUIDE : 
 * 
 * Key.A to create an Item in the bag (It's just for testing, it'll be useless for the end)
 * - Key.(ZQSD) to move the item in the grid
 * - Key.R to rotate the item clockwise
 * - Key.ESCAPE (esc) to confirm and add the item in the backpack
 * 
 * Key.I to initiate a combat
 * - Using an item cost 1 AP, when it reach 0, enemy play.
 * - Click on a item to use it
 * 
 * TO DO :
 * - LA MAP ZEBI
 */
public class GameController {
  /**
   * Default constructor, which does basically nothing.
   */
  public GameController() {}
  
  /**
   * Goes once in the game loop, which consists in retrieving user actions,
   * transmitting it to the GameView and GameData, and dealing with time events.
   * 
   * @param context {@code ApplicationContext} of the game.
   * @param data 	GameData of the game.
   * @return True if the game continue, False if we press the button to stop
   */
  private static boolean gameLoop(ApplicationContext context, GameData data) {
		Event event = context.pollOrWaitEvent(10); 
		if (event instanceof PointerEvent pointerEvent) {
			// If we click down on the screen
		  if (pointerEvent.action() == PointerEvent.Action.POINTER_DOWN) { 
		    var res = GameDataClick.click(pointerEvent.location().x(), pointerEvent.location().y());
		    switch(res.type()) {
		    	case ITEM -> {
		    		data.setDragItem((Item) res.value());
		    		GameDataClick.set_oldPosition(pointerEvent.location().x(), pointerEvent.location().y());
				  	GameDataClick.update_boundingBox(data.dragItem(), pointerEvent.location().x(), pointerEvent.location().y());
		    	}
		    	case MAP_OR_BAG -> {	
		    		if (!GameDataCombat.combat() && data.dragItem() == null) {
		    			data.swapMapOrBag();
		    		}
		    	}
		    	case BAG -> {
		    		if (data.mapOrBag() && GameDataCombat.combat()) {
		    			GameDataCombat.hero_action(data, (XY) res.value());
		    		}
		    	}
		    	case MAP ->{
		    		var coord = (XY) res.value();
		    		if (data.map().getHeroVisited().contains(coord)) {
		    			data.map().setHero_pos(coord);
		    			
		    		} else if (data.map().getHeroAccessible().contains(coord)) {
		    			data.map().setHero_pos(coord);
		    			data.map().addHeroVisited(coord);
		    			data.map().updateHeroAccessible();
		    			data.map().updateHeroVisible();
		    			var coordHero = new XY(data.map().get_heroPos().x(), data.map().get_heroPos().y());
		    			if (data.map().getGrid()[coordHero.y()][coordHero.x()] instanceof EnemyRoom) {
		    				data.swapMapOrBag();
				  			GameDataCombat.start_combat(new ArrayList<>(List.of(new Chicken(), new Chicken())) , data);
		    			}
		    		// PARTIE A SUPRIMER/MODIFIER PAR LA SUITE
		    		} else if (data.hero().getGold() >= 40 && data.map().getGrid()[coord.y()][coord.x()] instanceof LockedDoor) {
		    			var unlock_door = (LockedDoor) data.map().getGrid()[coord.y()][coord.x()];
		    			unlock_door.unlock();
		    			data.hero().sub("gold", 40);
		    			data.map().setHero_pos(coord);
		    			data.map().addHeroVisited(coord);
		    			data.map().updateHeroAccessible();
		    			data.map().updateHeroVisible();
		    		}
		    	}
		    	default -> {}
		    }	
		  }
		  if (data.dragItem() != null && pointerEvent.action() == PointerEvent.Action.POINTER_MOVE) {
		  	GameDataClick.move_item(data.dragItem(), pointerEvent.location().x(), pointerEvent.location().y());
		  	GameDataClick.set_oldPosition(pointerEvent.location().x(), pointerEvent.location().y());
		  }
		  if (data.dragItem() != null && pointerEvent.action() == PointerEvent.Action.POINTER_UP) {
		  	int x = pointerEvent.location().x();
		  	int y = pointerEvent.location().y();
		  	var res = GameDataClick.bag_click(x, y);
		    if(res.x() != -1) {
		    	data.dragItem().setXY(GameDataClick.bag_click(x, y));
		    	if(GameDataBackpack.add_ItemToBackpack(data.dragItem())){
						data.remove_itemMap(data.dragItem());
				  }
		    	else {
		    		GameDataClick.add_item(data.dragItem());
		    	}
	    	}
		    data.setDragItem(null);
		  }
		  GameView.draw(context, data);
		  data.setMouse_coord(new XY(pointerEvent.location().x(), pointerEvent.location().y()));	
		}
		// If event button is pressed 
		if (event instanceof KeyboardEvent key && key.action() == KeyboardEvent.Action.KEY_RELEASED) {
		  switch(key.key()) {
		    // A to add a weapon in the bag
		  	case Key.A ->{ 
		  	  if (data.dragItem() == null && !GameDataCombat.combat() && data.mapOrBag()) {
		  	    GameDataClick.add_item(new Sword()); 
		  	  }
		  	}
		  	// Start a combat against a RAT
		  	case Key.I ->{ 
		  		if(GameDataCombat.combat() == false) {
		  			GameDataCombat.start_combat(new ArrayList<>(List.of(new Chicken(), new Soldat(), new model.monster.Robot())), data);
		  		}
		  	}
		  	
		  	case Key.R ->{
		  		if (data.dragItem() != null) {
		  			GameData.rotate_item(data.dragItem());
				  	GameDataClick.update_boundingBox(data.dragItem(), data.getMouse_coord().x(), data.getMouse_coord().y());
		  		}
		  	}
				// Leave the game
				case Key.E -> {
		      return false;
				}
				default -> {}
			}	  
		  GameView.draw(context, data);
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
    GameView.initGameGraphics(screenInfo.width(), screenInfo.height(), data.bag().grid_size());
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
