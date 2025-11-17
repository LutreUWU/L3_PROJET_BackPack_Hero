package game.data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import com.github.forax.zen.ApplicationContext;

import game.GameData;
import game.GameView;
import item.Item_Object;
import monster.Enemy;

/**
 * The game data with all methods for combat manipulation. 
 * It's separated from GameData.java for easier read
 * 
 * All methods here are used when we initiate a combat.
 */
public class GameDataCombat {
	/**
	 * - combat : We're in combat, else false
	 * - target : Since we can have multiple enemy, target is the enemy we're focusing
	 * - lst_enemy : List of all enemy we're fighting
	 */
	private static boolean combat = false;
	private static Enemy target;
  private static ArrayList<Enemy> lst_enemy;

	/**
	 * Methods that treats the loop for the combat. The loop stops when the hero or the enemy die. 
	 * 
	 * @param context {@code ApplicationContext} of the game.
	 * @param monster The data of the monster we fight
	 * @param data		The data of the game
	 */
	public static void start_combat(ApplicationContext context, ArrayList<Enemy> monsters, GameData data) {
		if (combat) {
			return;
		}
		Objects.requireNonNull(context);
		Objects.requireNonNull(monsters);
		Objects.requireNonNull(data);
		lst_enemy = monsters;
		target = monsters.getFirst();
		GameView.update_combat(context, data, lst_enemy);	
		combat = true;
	}
	
	/**
	 * This methods is called after the user click on a item in the backpack.
	 * This methods will use the item we chose in the backpack
	 * 
	 * @param context	{@code ApplicationContext} of the game.
	 * @param data		The data of the game.
	 * @param id			The ID of the item we click in the backpack.
	 */
	public static void hero_action(ApplicationContext context, GameData data, int id) {
		Objects.requireNonNull(context);
		Objects.requireNonNull(data);
		Optional<Item_Object> weapon =data.bag().item_lst().stream()
																											 .filter(item -> item.id() == id)
																											 .findFirst();
		weapon.ifPresent(item -> {
			item.use(target);
			lst_enemy.forEach(enemy -> enemy.action());
			GameDataHero.reset();
			
		});
		GameView.update_combat(context, data, lst_enemy);	
	}
	
	/**
	 * Getter to know if we're in a combat or nots
	 * @return
	 */
	public static boolean combat() {
		return combat;
	}
}
