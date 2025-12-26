package game.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import com.github.forax.zen.ScreenInfo;

import game.GameData;
import model.BoundingBox;
import model.Hero;
import model.Item;
import model.RandomItem;
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
	private static Enemy target;
  private static ArrayList<Enemy> lstEnemy;
  private static LinkedHashMap<Enemy, BoundingBox> enemyBox = new LinkedHashMap<>();
  private static ArrayList<String> log = new ArrayList<>();
  private static int totalExp;
  private static int levelBeforeCombat;
  
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
		GameDataClick.resetDragItemLst();
		lstEnemy = monsters;
		totalExp = 0;
		levelBeforeCombat = data.hero().getLevel();
		getEnemyBox(lstEnemy, data.screenInfo(), data.hero().getSizeX(), data.hero().getSizeY());
		lstEnemy.forEach(monster -> monster.preAction());
		setTarget(lstEnemy.get(0));
		log = new ArrayList<>();
		log.add("Le combat démarre !");
		combat = true;
	}
	
	/**
	 * Get the boundingBox of every enemy in the fight.
	 * Since every enemy size is based from the hero's size, we need this value
	 * 
	 * @param lstEnemy		list of enemies
	 * @param screenInfo	{@code screenInfo} of the window
	 * @param heroSizeX		sizeX of the Hero
	 * @param heroSizeY		sizeY of the Hero
	 */
	private static void getEnemyBox(ArrayList<Enemy> lstEnemy, ScreenInfo screenInfo, int heroSizeX, int heroSizeY) {
		for (int i = 0; i < lstEnemy.size(); i++) {
			var enemy = lstEnemy.get(i);
			double sizeX = heroSizeX * enemy.getSizeX();
	  	double sizeY = heroSizeY * enemy.getSizeY();
	  	double northWestX =  screenInfo.width() * 0.80 - heroSizeX + (i - (lstEnemy.size() - 1) / 2.0) * heroSizeX ;
	  	double northWestY =  screenInfo.height() * (0.5 - (0.1 * (i%2))) + (heroSizeY - sizeY);
	  	var NW = new XY((int) northWestX, (int) northWestY);
	  	var SE = new XY(NW.x() + (int) sizeX,NW.y() + (int) sizeY);
	  	enemyBox.put(enemy, new BoundingBox(NW, SE));
		}
	}
	
	/**
	 * This methods is called after the user click on a item in the backpack.
	 * This methods take an ID in parameter, it will checks in the bag if a weapon correspond to the ID, then it we'll use it.
	 * 
	 * @param data		The data of the game.
	 * @param object	The ID of the item we click in the backpack.
	 */
	public static void heroAction(GameData data, XY coord) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(coord);
		log = new ArrayList<>();
		var item = data.bag().getItem(coord.x(), coord.y());
		if (item != null) {
			data.bag().removeItemFromBackpack(item);
			var newItem = item.use(target, lstEnemy, data);
			if (newItem.durability() != 0) data.bag().addItemToBackpack(newItem);
			if(data.hero().getEnergyPoint() <= 0) {
				applyEffects();
			}
			killMonster(data);
			if(data.hero().getEnergyPoint() <= 0) {
				enemyAction(data.hero());
			}
			if (lstEnemy.isEmpty()) {
				GameDataHero.add("energy", (3 - data.hero().getEnergyPoint()));
				GameDataHero.add("xp", totalExp);
				for (int i = 0; i < data.hero().getLevel() - levelBeforeCombat; i++) {
					Random r = new Random();
					data.bag().addCaseUnlock(3 + r.nextInt(1));
				}
				var itemGain = RandomItem.generate(data.floor());
				GameDataClick.addDragItem(itemGain);
				combat = false;
			
			}
		}
	}
	
	/**
	 * Check the list of monster, and kill him if he has below 0 PV.
	 * Add the exp, update the bounding box and swap the target if necessary.
	 * 
	 * @param data {@code data} of the game
	 */
	private static void killMonster(GameData data) {
		Iterator<Enemy> it = lstEnemy.iterator();
		while (it.hasNext()) {
	    Enemy enemy = it.next();
			if(enemy.getHP() <= 0) {
				totalExp += enemy.getXP();
				it.remove();
				enemyBox.remove(enemy);
				if (target == enemy && !lstEnemy.isEmpty()) {
					setTarget(lstEnemy.getFirst());
				}
				getEnemyBox(lstEnemy, data.screenInfo(), data.hero().getSizeX(), data.hero().getSizeY());	
			}
		}
	}
	
	/**
	 * End the hero's turn, apply effects and trigger enemies action.
	 * Also reset the log.
	 * 
	 * @param data {@code data} of the game
	 */
	public static void endTour(GameData data) {
		Objects.requireNonNull(data);
		log = new ArrayList<>();
		applyEffects();
		killMonster(data);
		enemyAction(data.hero());
	}
	
	/**
	 * Apply effects that was on enemies.
	 */
	private static void applyEffects() {
		for (var enemy : lstEnemy) { 
			for (var effect : enemy.getEffects().keySet()) {
				var dmg = effect.getDamage();
				enemy.subHP(dmg);
				addLog(enemy + " recoit " + dmg + " dégats grâce à l'effet " + effect);
			}
			enemy.updateEffects();
		}
	}
	
	/**
	 * Apply the action of each enemies.
	 * 
	 * @param hero 
	 */
	public static void enemyAction(Hero hero) {
		Objects.requireNonNull(hero);
		lstEnemy.forEach(enemy -> enemy.action());
		if(hero.getHP() == 0) {
			// TO DO 
		}
		GameDataHero.reset();
		GameDataHero.add("energy", 3 - hero.getEnergyPoint());
	}
	
	/**
	 * Getter to know if we're in a combat or nots
	 * @return
	 */
	public static boolean combat() {
		return combat;
	}
	
	public static void setTarget(Enemy enemy) {
		target = enemy;
	}
	
	public static Enemy getTarget(){
		return target;
	}
	
	public static ArrayList<Enemy> getLstEnemy(){
		return lstEnemy;
	}
	
	public static LinkedHashMap<Enemy, BoundingBox> getEnemyBox() {
		return enemyBox;
	}
	
	public static void addLog(String content) {
		Objects.requireNonNull(content);
		log.add(content);
	}
	
	public static List<String> getLog() {
		return List.copyOf(log);
	}
}
