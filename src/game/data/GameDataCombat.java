package game.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
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
	private static Item hoverItem = null;
	private static Enemy target;
  private static List<Enemy> lstEnemy;
  private static LinkedHashMap<Enemy, BoundingBox> enemyBox = new LinkedHashMap<>();
  private static ArrayList<String> log = new ArrayList<>();
  private static int totalExp = 0;
  private static int levelBeforeCombat;
  private static int nbMana;
  
	/**
	 * Methods that treats the loop for the combat. The loop stops when the hero or the enemy die. 
	 * 
	 * @param monster The data of the monster we fight
	 * @param data		The data of the game
	 */
	public static void startCombat(List<Enemy> monsters, GameData data) {
		if (combat) {
			return;
		}
		Objects.requireNonNull(monsters);
		Objects.requireNonNull(data);
		GameDataClick.resetDragItemLst();
		nbMana = data.bag().getManaInBag();
		lstEnemy = monsters;
		lstEnemy.forEach(monster -> monster.resetStats());
		levelBeforeCombat = data.hero().getLevel();
		getEnemyBox(lstEnemy, data.screenInfo(), data.hero().getSizeX(), data.hero().getSizeY());
		lstEnemy.forEach(monster -> monster.preAction());
		setTarget(lstEnemy.get(0));
		log = new ArrayList<>();
		log.add("Le combat démarre !");
		useAllPassive(data);
		combat = true;
	}
	
	private static void useAllPassive(GameData data) {
		ListIterator<Item> it = data.bag().bagItemLst().listIterator();
		while (it.hasNext()) {
			var item = it.next();
			var newItem = item.usePassive(target, lstEnemy, data);
			it.set(newItem);
		}
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
	private static void getEnemyBox(List<Enemy> lstEnemy, ScreenInfo screenInfo, int heroSizeX, int heroSizeY) {
		double gap = 1.3;
		for (int i = 0; i < lstEnemy.size(); i++) {
			var enemy = lstEnemy.get(i);
			double sizeX = heroSizeX * enemy.getInfo().sizeX();
	  	double sizeY = heroSizeY * enemy.getInfo().sizeY();
	  	double offsetOdd = heroSizeX * gap * (i + 1 / 2);
	  	double offsetEven = heroSizeX * gap * i / 2;
	  	double northWestX = (i % 2 == 1) ? screenInfo.width() * 0.70 - offsetOdd - enemy.getInfo().sizeX() 
	  																	 : screenInfo.width() * 0.70 + offsetEven + enemy.getInfo().sizeY();
	  	boolean isTopRow;
	    if (i == 0) {
	        isTopRow = true; 
	    } else if ((i-1) % 4 < 2) {
	        isTopRow = false; 
	    } else {
	        isTopRow = true; 
	    }	  	
	    double northWestY =  screenInfo.height() * 0.5 + (isTopRow ? 0 : heroSizeY / 2 + heroSizeY - sizeY);
	  	var NW = new XY((int) northWestX, (int) northWestY);
	  	var SE = new XY(NW.x() + (int) sizeX, NW.y() + (int) sizeY);
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
	public static void heroAction(GameData data) {
		Objects.requireNonNull(data);
		log = new ArrayList<>();
		var item = hoverItem;
		if (item != null) {
			if (item.info().AP() > data.hero().getEnergyPoint()) {
				addLog("Vous n'avez pas assez d'AP pour utiliser " + item.toString());
			}
			else if (item.info().mana() > nbMana) {
				addLog("Vous n'avez pas assez de mana pour utiliser " + item.toString());
			}
			else if (!data.bag().itemConnectedToMana(item)) {
				addLog(item.toString() + " n'est pas connecté à une source de mana");
			}
			else {
				useItemOnEnemies(data, item);
				killMonster(data);
				if(data.hero().getEnergyPoint() <= 0) {
					endTour(data);
				}
			}
		}
		data.bag().updateManaConnected();
	}
	
	/**
	 * Use the item against enemies.
	 * 
	 * @param data {@code GameData} of the game
	 * @param item {@code Item} we wants to use
	 */
	private static void useItemOnEnemies(GameData data, Item item) {
		data.bag().removeItemFromBackpack(item);
		data.hero().sub("energy", item.info().AP());
		nbMana -= item.info().mana();
		var newItem = item.use(target, lstEnemy, data);
		if (newItem.durability() != 0) data.bag().addItemToBackpack(newItem);
	}

	/**
	 * This method is called when there's no enemy left.
	 * It will reset AP, give the exp, give the weapon and end the combat.
	 * 
	 * @param data {@code GameData} of the game.
	 */
	private static void endCombat(GameData data) {
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
				totalExp += enemy.getInfo().xp();
				it.remove();
				enemyBox.remove(enemy);
				if (target == enemy && !lstEnemy.isEmpty()) {
					setTarget(lstEnemy.getFirst());
				}
				getEnemyBox(lstEnemy, data.screenInfo(), data.hero().getSizeX(), data.hero().getSizeY());	
			}
		}
		if (lstEnemy.isEmpty()) {
			endCombat(data);
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
		useAllPassive(data);
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
			// FAIRE LA METHODE POUR AJOUTER LE SCORE DANS LE FICHIER TXT
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
		Objects.requireNonNull(enemy);
		target = enemy;
	}
	
	public static Enemy getTarget(){
		return target;
	}
	
	public static List<Enemy> getLstEnemy(){
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
	
	public static void setHoverItem(Item item) {
		hoverItem = item;
	}
	
	public static Item getHoverItem() {
		return hoverItem;
	}
	
	public static int getNbMana() {
		return nbMana;
	}
}
