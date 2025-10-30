package demo;

import java.awt.Color;

import com.github.forax.zen.Application;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.KeyboardEvent.Key;
import com.github.forax.zen.PointerEvent;

import view.Interface;
import model.Backpack;


public class Main { // #
	public static void main(String[] args) { // ##
		Application.run(Color.WHITE, context ->{ // ###
			
		  Interface ui = new Interface();
		  Backpack bag = new Backpack();
		  var screenInfo = context.getScreenInfo();
	      ui.drawGrid(context, 0, 0, screenInfo, bag);
	      
	      while (true) {
			  var event = context.pollOrWaitEvent(10); // Lancer la détection d'event (s'il ne recoit rien au bout de 10 ms, renvoie null)
			  if (event == null) {
			    continue;
			  }
			  switch (event) {
		        case PointerEvent e:{ // Si on a fait un clique
		          var location = e.location(); // On récupère les coordonnées du clique
		          // Et on crée un cercle bleu
		          ui.drawRectangle(context, location.x(), location.y(), 20, 20, Color.BLUE);
		          break;
		    	}
		        case KeyboardEvent e: // Si on a cliqué sur une touche
			      if(e.key() == Key.E) { // Si on a cliqué sur la touche E, on quitte la fenêtre
			        context.dispose();
			        IO.println("fin");
			        return;
			      }
			    default:
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