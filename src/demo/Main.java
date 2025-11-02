package demo;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;

import item.Backpack;
import item.Item_Object;
import item.Sword;
import view.Interface;
import view.weaponView.InterfaceWeapon;

/*
 * Press "A" to add an item in a bag (currently it's only a sword 1x3)
 * Interact with ZQSD to move the item.
 * Press ECHAP to confirm the placement.
 * 
 * WARNING :
 * If during the placement item, we press an invalid button (not in the switch), the item will disappear till we press a correct button.
 * One solution is to call the function drawBag() for each case ZQSD, but the code will be ugly.
 * I don't know yet how to resolve this.
 */

public class Main { // #
	public static void main(String[] args) { // ##
		Application.run(Color.WHITE, context ->{ // ###
		  // Information
		  Interface ui = new Interface();
		  InterfaceWeapon ui_weapon = new InterfaceWeapon();
		  Backpack bag = new Backpack();
		  Item_Object weapon = null; // To active "adding item in bag" button interface
		  var screenInfo = context.getScreenInfo();
		  //////////////
		  ui.drawBag(context, screenInfo, bag);
	      while (true) {
	    	  Event event = context.pollOrWaitEvent(10); 
	    	  if (event == null) {
	    		  continue;
	    	  }
	    	  // If event is a button pressed 
	    	  if (event instanceof KeyboardEvent key && key.action() == KeyboardEvent.Action.KEY_RELEASED) {
	    		  if (weapon != null) { // We refresh the bag only if we move a weapon inside.
	    			  ui.drawBag(context, screenInfo, bag);
	    		  }
	    		  switch(key.key()) {
	    		  	  // A to add a weapon in the bag
	    		  	  case Key.A ->{ 
	    		  		  if (weapon == null) {
	    		  			  weapon =  new Sword();
		    				  weapon.setXY(3, 2); // Center of the backpack
		    				  ui_weapon.weaponGrid(context, screenInfo, bag, weapon);
	    		  		  }
	    		  	  }
	    		  	  // Moving the selected weapon, do nothing if no weapon is selected
		    		  case Key.Z -> ui_weapon.move_item(weapon, 0, -1);
		    		  case Key.D -> ui_weapon.move_item(weapon, 1, 0);
		    		  case Key.S -> ui_weapon.move_item(weapon, 0, 1);
		    		  case Key.Q -> ui_weapon.move_item(weapon, -1, 0);
		    		  case Key.R -> ui_weapon.rotate_item(weapon);
		    		  // Confirm the placement of the weapon (and check if we can put here)
		    		  case Key.ESCAPE -> {
		    			  if (bag.add_ItemToBackpack(weapon)){
		    				  weapon = null;
		    				  ui.drawBag(context, screenInfo, bag);
		    			  } else { // if we can't place it here, just refresh the draw of the item.
		    				  ui_weapon.move_item(weapon, 0, 0);
		    			  }
		    		  }
		    		  // Leave the game
		    		  case Key.E -> {
		    			context.dispose();
		                System.out.println("Fin du programme");
		                return;
		    		  }
		    		  default -> {}
	    		  }
	    		  
	          }
			}
	      
		}); // ###
	} // ##
} // #

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