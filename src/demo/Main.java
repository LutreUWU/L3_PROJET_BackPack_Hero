package demo;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;

import item.Backpack;
import item.Sword;
import view.Interface;


public class Main { // #
	public static void main(String[] args) { // ##
		Application.run(Color.WHITE, context ->{ // ###
		  // Information
		  Interface ui = new Interface();
		  Backpack bag = new Backpack();
		  var screenInfo = context.getScreenInfo();
		  // Test for adding a weapon in the bag
		  var arme =  new Sword();
		  arme.setXY(3, 2); // Center of the backpack
		  bag.add_itemToGrid(arme);
		  // 
		  ui.drawBag(context, 0, 0, screenInfo, bag);
	      while (true) {
	    	  Event event = context.pollOrWaitEvent(10); 
	    	  if (event == null) {
	    		  continue;
	    	  }
	    	  if (event instanceof KeyboardEvent key && key.action() == KeyboardEvent.Action.KEY_RELEASED) {
	    		  switch(key.key()) {
		    		  case Key.Z -> bag.move_item(arme, 0, -1);
		    		  case Key.D -> bag.move_item(arme, 1, 0);
		    		  case Key.S -> bag.move_item(arme, 0, 1);
		    		  case Key.Q -> bag.move_item(arme, -1, 0);
		    		  case Key.E -> {
		    			context.dispose();
		                System.out.println("Fin du programme");
		                return;
		    		  }
		    		  default -> {}
	    		  }
	              ui.drawBag(context, 0, 0, screenInfo, bag);
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