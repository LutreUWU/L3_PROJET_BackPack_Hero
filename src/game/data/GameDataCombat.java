package game.data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import game.GameData;
import model.Item;
import model.XY;
import model.monster.Enemy;

/**
 * The game data with all methods for combat manipulation. 
 * It's separated from GameData.java for easier read
 * 
 * All methods here are used when we initiate a combat.
 */
public class GameDataCombat {
	/**
	 * - combat : True if we're in combat, else false
	 * - target : Since we can have multiple enemy, target is the enemy we're focusing
	 * - lst_enemy : List of all enemy we're fighting
	 */
	private static boolean combat = false;
	private static int target;
  private static ArrayList<Enemy> lstEnemy;

	/**
	 * Methods that treats the loop for the combat. The loop stops when the hero or the enemy die. 
	 * 
	 * @param monster The data of the monster we fight
	 * @param data		The data of the game
	 */
	public static void startCombat(ArrayList<Enemy> monsters, GameData data) {
		if (combat) {
			return;
		}
		Objects.requireNonNull(monsters);
		Objects.requireNonNull(data);
		lstEnemy = monsters;
		lstEnemy.forEach(monster -> monster.preAction());
		target = 0;
		combat = true;
	}
	
	/**
	 * This methods is called after the user click on a item in the backpack.
	 * This methods take an ID in parameter, it will checks in the bag if a weapon correspond to the ID, then it we'll use it.
	 * 
	 * @param data		The data of the game.
	 * @param object			The ID of the item we click in the backpack.
	 */
	public static void heroAction(GameData data, XY coord) {
		Objects.requireNonNull(data);
		int id = data.bag().grid()[coord.y()][coord.x()];
		Enemy targetEnemy = lstEnemy.get(target); 
		// A changer, pas ouf je pense
		Optional<Item> weapon = data.bag().bagItemLst().stream()
																											 .filter(item -> item.getID() == id)
																											 .findFirst();
		weapon.ifPresent(item -> {
			item.use(targetEnemy);
			if (targetEnemy.getHP() <= 0) {
				lstEnemy.remove(target);
				GameDataHero.add("xp", targetEnemy.getXP());
			}
			if (lstEnemy.isEmpty()) {
				GameDataHero.add("energy", (3 - data.hero().getEnergy_point()));
				combat = false;
			}
			else {
				if(data.hero().getEnergy_point() <= 0) {
					lstEnemy.forEach(enemy -> enemy.action());
					if(data.hero().getHP() == 0) {
						// TO DO 
					}
					GameDataHero.add("energy", 3);
				}
			}
			GameDataHero.reset();
		});
	}
	
	/**
	 * Getter to know if we're in a combat or nots
	 * @return
	 */
	public static boolean combat() {
		return combat;
	}
	
	public static ArrayList<Enemy> getLstEnemy(){
		return lstEnemy;
	}
}
