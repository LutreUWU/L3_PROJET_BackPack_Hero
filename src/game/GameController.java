package game;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import game.data.GameDataBackpack;
import item.Sword;

/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 * 
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
   * @param data 	GameData of the game.
   * @param view 	GameView of the game.
   * @return True if the game continue, False if we press the button to stop
   */
  private static boolean gameLoop(ApplicationContext context, GameData data, GameView view) {
	Event event = context.pollOrWaitEvent(10); 
	if (event instanceof PointerEvent pointerEvent) {
	  if (pointerEvent.action() == PointerEvent.Action.POINTER_MOVE) {
	    int mouseX = pointerEvent.location().x();
	    int mouseY = pointerEvent.location().y();
	    IO.println(mouseX + " , " + mouseY);
	  }
	}
	// If event is a button pressed 
	if (event instanceof KeyboardEvent key && key.action() == KeyboardEvent.Action.KEY_RELEASED) {
	  GameView.draw(context, data, view);
	  switch(key.key()) {
	    // A to add a weapon in the bag
	  	case Key.A ->{ 
	  	  if (data.weapon() == null) {
	  	    var sword = new Sword();
	  	    sword.setXY(3, 2); // Center of the item
	  	    data.setWeapon(sword); 
	  	    GameView.updateWeaponDraw(context, data);
	  	  }
	  	}
		// Moving the selected weapon, do nothing if no weapon is selected
		case Key.Z -> {
		  GameDataBackpack.move_item(data.weapon(), 0, -1);
		  GameView.updateWeaponDraw(context,data);
		}
		case Key.D -> {
		  GameDataBackpack.move_item(data.weapon(), 1, 0);
		  GameView.updateWeaponDraw(context, data);
		}
		case Key.S -> {
		  GameDataBackpack.move_item(data.weapon(), 0, 1);
		  GameView.updateWeaponDraw(context, data);
		}
		case Key.Q -> {
	      GameDataBackpack.move_item(data.weapon(), -1, 0);
	      GameView.updateWeaponDraw(context, data);
		}
		case Key.R -> {
	      GameDataBackpack.rotate_item(data.weapon());
		  GameView.updateWeaponDraw(context, data);
		}
		// Confirm the placement of the weapon (and check if we can put here)
	    case Key.ESCAPE -> {
		  if(GameDataBackpack.add_ItemToBackpack(data.weapon())){
			data.setWeapon(null);
			GameView.draw(context, data, view);
		  } else { // if we can't place it here, just refresh the draw of the item.
			GameDataBackpack.move_item(data.weapon(), 0, 0);
			GameView.updateWeaponDraw(context, data);
		  }
		}
		// Leave the game
		case Key.E -> {
          return false;
		}
		default -> {}
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
    int grid_size = 100;
    var data = new GameData(80);
    new GameDataBackpack(data.bag());
    var view = GameView.initGameGraphics(width, height, grid_size);
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
	//var arme = ItemFactory.createItem("Baguette");
	//var arme2 = ItemFactory.createItem("Croissant Gun");
	//IO.println("WEAPON :\n");
	//IO.println(arme);
	//IO.println(arme2);
	//IO.println("ARMOR :\n");
	//var armure = ItemFactory.createItem("Cheese Armor");
	//IO.println(armure);
	//IO.println("SHIELD :\n");
	//var shield = ItemFactory.createItem("Umbrella");
	//IO.println(shield);
	//IO.println("MAGIC :\n");
	//var magie = ItemFactory.createItem("Magic Wine");
	//IO.println(magie); 
}
