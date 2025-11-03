package game;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import item.Sword;

/**
 * The SimpleGameController class deals with the main game loop, including
 * retrieving raw user actions, sending them for analysis to the GameView and
 * GameData, and dealing with time events.
 * 
 */
public class SimpleGameController {
  /**
   * Default constructor, which does basically nothing.
   */
  public SimpleGameController() {
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
  private static boolean gameLoop(ApplicationContext context, SimpleGameData data, SimpleGameView view) {
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
		  SimpleGameView.draw(context, data, view);
		  switch(key.key()) {
		  	  // A to add a weapon in the bag
		  	  case Key.A ->{ 
		  		  if (data.weapon() == null) {
		  			var sword = new Sword();
		  	        sword.setXY(3, 2); // Center of the item
		  	        data.setWeapon(sword); 
		  	        SimpleGameView.updateWeaponDraw(context, data);
		  		  }
		  	  }
		  	  // Moving the selected weapon, do nothing if no weapon is selected
    		  case Key.Z -> {
    			  data.move_item(data.weapon(), 0, -1);
    			  SimpleGameView.updateWeaponDraw(context,data);
    		  }
    		  case Key.D -> {
    			  data.move_item(data.weapon(), 1, 0);
    			  SimpleGameView.updateWeaponDraw(context, data);
    		  }
    		  case Key.S -> {
    			  data.move_item(data.weapon(), 0, 1);
    			  SimpleGameView.updateWeaponDraw(context, data);
    		  }
    		  case Key.Q -> {
    			  data.move_item(data.weapon(), -1, 0);
    			  SimpleGameView.updateWeaponDraw(context, data);
    		  }
    		  case Key.R -> {
    			  data.rotate_item(data.weapon());
    			  SimpleGameView.updateWeaponDraw(context, data);
    		  }
    		  // Confirm the placement of the weapon (and check if we can put here)
    		  case Key.ESCAPE -> {
    			  if(data.add_ItemToBackpack(data.weapon())){
    				  data.setWeapon(null);
    				  SimpleGameView.draw(context, data, view);
    			  } else { // if we can't place it here, just refresh the draw of the item.
    				  data.move_item(data.weapon(), 0, 0);
    				  SimpleGameView.updateWeaponDraw(context, data);
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
    var data = new SimpleGameData(80);
    var view = SimpleGameView.initGameGraphics(width, height, grid_size);
    SimpleGameView.draw(context, data, view);
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
    Application.run(Color.WHITE, SimpleGameController::memoryGame);
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
