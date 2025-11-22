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
import game.data.GameDataCombat;
import game.data.GameDataHero;
import game.data.GameDataMap;
import model.monster.Chicken;
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
 * - Package model is useless, you can erase it
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
		    var res = GameData.item_click(pointerEvent.location().x(), pointerEvent.location().y(), context.getScreenInfo());
		    var click = res.entrySet().iterator().next();
		    if(!(click.getKey().equals("Nothing"))) {
		    	if(data.mapOrBag() && click.getKey().equals("Bag")) {
			    	if (GameDataCombat.combat()) { 
				    	GameDataCombat.hero_action(data, click.getValue());
				    }
			    }
		    	if(click.getKey().equals("MapOrBag")) {
		    		if (!GameDataCombat.combat() && data.weapon() == null) {
		    			data.swapMapOrBag();
		    		}
		    	}
		    }
		    GameView.draw(context, data);
		    if (data.weapon() != null) {
	  	    GameView.updateWeaponDraw(context, data);
			  }
		  }
		}
		// If event button is pressed 
		if (event instanceof KeyboardEvent key && key.action() == KeyboardEvent.Action.KEY_RELEASED) {
		  GameView.draw(context, data);
		  switch(key.key()) {
		    // A to add a weapon in the bag
		  	case Key.A ->{ 
		  	  if (data.weapon() == null && !GameDataCombat.combat() && data.mapOrBag()) {
		  	    var sword = new Sword();
		  	    sword.setXY(3, 2); // Center of the item
		  	    data.setWeapon(sword); 
		  	  }
		  	}
		  	// Start a combat against a RAT
		  	case Key.I ->{ 
		  		if(GameDataCombat.combat() == false) {
		  			GameDataCombat.start_combat(new ArrayList<>(List.of(new Chicken())) , data);
			  		GameDataCombat.refreshCombatDraw(context, data);
		  		}
		  	}
				// Moving the selected weapon, do nothing if no weapon is selected
				case Key.Z -> GameDataBackpack.move_item(data.weapon(), 0, -1);
				case Key.D -> GameDataBackpack.move_item(data.weapon(), 1, 0);
				case Key.S -> GameDataBackpack.move_item(data.weapon(), 0, 1);
				case Key.Q -> GameDataBackpack.move_item(data.weapon(), -1, 0);
				case Key.R -> GameDataBackpack.rotate_item(data.weapon());
				// Confirm the placement of the weapon (and check if we can put here)
		    case Key.ESCAPE -> {
				  if(GameDataBackpack.add_ItemToBackpack(data.weapon())){
						data.setWeapon(null);
						GameView.draw(context, data);
				  }
				}
				// Leave the game
				case Key.E -> {
		      return false;
				}
				default -> {}
			}	  
		  if (data.weapon() != null) {
  	    GameView.updateWeaponDraw(context, data);
		  }
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
    var width = screenInfo.width();
    var height = screenInfo.height();
    var data = new GameData(height);
    new GameDataBackpack(data.bag());
    new GameDataHero(data.hero());
    new GameDataMap(data.map());
    GameView.initGameGraphics(width, height, data.bag().grid_size());
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
