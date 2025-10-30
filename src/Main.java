import model.ItemFactory;

public class Main {
	public static void main() {
	  var arme = ItemFactory.createItem("Baguette");
	  var arme2 = ItemFactory.createItem("Croissant Gun");
	  IO.println("WEAPON :\n");
	  IO.println(arme);
	  IO.println(arme2);
	  IO.println("ARMOR :\n");
	  var armure = ItemFactory.createItem("Cheese Armor");
	  IO.println(armure);
	  IO.println("SHIELD :\n");
	  var shield = ItemFactory.createItem("Umbrella");
	  IO.println(shield);
	  IO.println("MAGIC :\n");
	  var magie = ItemFactory.createItem("Magic Wine");
	  IO.println(magie);
	}
	
}
