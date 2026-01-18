package game.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Random;

import game.GameData;
import loader.MathLoader;
import model.BoundingBox;
import model.Item;
import model.RandomItem;
import model.XY;
import model.monster.Crabe;
import model.monster.Enemy;
import model.monster.Robot;

/**
 * The game data with all methods for combat manipulation. 
 * It's separated from GameData.java for easier read
 * 
 * All methods here are used when we initiate a combat.
 */
public class GameDataCombat {
	/*
	 * Constructor that does nothing
	 */
	public GameDataCombat() {}
	
	private static boolean combat = false; // To know if we're currently fighting or not
	private static boolean curseEvent = false; // To know if we're currently in a curse event or no
	private static Item hoverItem = null; // If in curse event, To know if we're holding an item
	private static Enemy target; // Which ennemy we hit with our items
  private static List<Enemy> lstEnemy; // List of all ennemies
  private static LinkedHashMap<Enemy, BoundingBox> enemyBox = new LinkedHashMap<>(); // The box of all current ennemies on the screen
  private static ArrayList<String> log = new ArrayList<>(); // History of all actions
  private static int totalExp; // Exp we get after finishing the combat
  private static int levelBeforeCombat; // Level before starting combat
  private static int nbMana; // Mana before starting combat
  
  /**
	 * Starts a new combat sequence.
	 * Initializes enemies, hero resources, positions enemies on screen,
	 * applies passive effects and sets the first target.
	 *
	 * @param monsters list of enemies to fight
	 * @param data global game data
	 */
	public static void startCombat(List<Enemy> monsters, GameData data) {
		if (combat) {
			return;
		}
		Objects.requireNonNull(monsters);
		Objects.requireNonNull(data);
		totalExp = 0;
		GameDataClick.resetDragItemLst();
		checkBackgroundChange(monsters.iterator(), data);
		nbMana = data.bag().getManaInBag();
		lstEnemy = monsters;
		lstEnemy.forEach(monster -> monster.resetStats());
		levelBeforeCombat = data.hero().getLevel();
		getEnemyBox(lstEnemy, data.hero().getSizeX(), data.hero().getSizeY());
		lstEnemy.forEach(monster -> monster.preAction());
		setTarget(lstEnemy.getFirst());
		log = new ArrayList<>();
		useAllPassive(data);
		log.add("Le combat démarre !");
		combat = true;
	}
	
	/**
	 * Check all ennemies and change the current background if a specific mob is inside
	 * 
	 * The background change especially when meeting a boss.
	 * 
	 * @param monsters Iterator containing all monsters
	 * @param data		 data of the game
	 */
	private static void checkBackgroundChange(Iterator<Enemy> monsters, GameData data) {
		while(monsters.hasNext()) {
			switch (monsters.next()) {
	      case Robot _ -> data.setBG("BG_BOSS_ROBOT");
	      case Crabe _ -> data.setBG("BG_BOSS_CRABE");
	      default -> {}
			}
		}
	}
	
	/**
	 * Applies all passive effects from the hero's items.
	 * Passive effects may modify items, enemies or hero stats.
	 *
	 * @param data global game data
	 */
	private static void useAllPassive(GameData data) {
		data.hero().resetBoostDmg();
		ListIterator<Item> it = data.bag().bagItemLst().listIterator();
		while (it.hasNext()) {
			var item = it.next();
			var newItem = item.usePassive(target, lstEnemy, data);
			data.bag().removeItemFromBackpack(item);
			data.bag().addItemToBackpack(newItem);
		}
	}
	
	/**
	 * Computes and assigns a bounding box for each enemy.
	 * Enemy size is relative to the hero's size and screen dimensions.
	 *
	 * @param lstEnemy 	 list of enemies
	 * @param screenInfo screen size information
	 * @param heroSizeX  hero width
	 * @param heroSizeY  hero height
	 */
	private static void getEnemyBox(List<Enemy> lstEnemy, int heroSizeX, int heroSizeY) {
		double gap = 0.5;
		double totalWidth = 0;
    int startX = (int) (MathLoader.getScreenWidth() * 0.75);
    for (int i = 0; i < lstEnemy.size(); i++) { // Get the total width of ALL ENNEMIES
			var enemy = lstEnemy.get(i);
			double sizeX = heroSizeX * enemy.getInfo().sizeX();
    	totalWidth += sizeX + gap * heroSizeX;
    }
    startX -= totalWidth / 2;
		for (int i = 0; i < lstEnemy.size(); i++) { // Then we start this "total width"
			var enemy = lstEnemy.get(i);
			double sizeX = heroSizeX * enemy.getInfo().sizeX();
	  	double sizeY = heroSizeY * enemy.getInfo().sizeY();
	    double northWestY =  MathLoader.getScreenHeight() * 0.6 + heroSizeY - sizeY;
	  	var NW = new XY(startX, (int) northWestY);
	  	var SE = new XY(NW.x() + (int) sizeX, NW.y() + (int) sizeY);
	  	startX += sizeX + gap * heroSizeX;
	  	enemyBox.put(enemy, new BoundingBox(NW, SE));
		}
	}
	
	/**
	 * Handles the hero action when an item is used.
	 * Checks energy and mana requirements before applying the item effect.
	 * Ends the hero turn if no energy remains.
	 *
	 * @param data global game data
	 */
	public static void heroAction(GameData data) {
		Objects.requireNonNull(data);
		log = new ArrayList<>();
		var item = hoverItem;
		if (item != null) {
			if (checkItemCond(item, data)) {
				useItemOnEnemies(data, item);
				if(data.hero().getHP() <= 0) {
					data.endGame();
				}
				killMonster(data);
				if(data.hero().getEnergyPoint() <= 0) {
					endTour(data);
				}
			}
			data.bag().updateManaConnected(); // Update all connected items after using an item
		}
	}
	
	/**
	 * Check if all conditions to use an item is meet.
	 * 
	 * @param item Item we wants to check
	 * @param data Data of the game
	 * @return false if condition are not met, else true
	 */
	private static boolean checkItemCond(Item item, GameData data) {
		if (item.info().AP() > data.hero().getEnergyPoint()) {
			addLog("Vous n'avez pas assez d'AP pour utiliser " + item.toString());
			return false;
		}
		if (item.info().mana() > 0) {
			if (item.info().mana() > nbMana) {
				addLog("Vous n'avez pas assez de mana pour utiliser " + item.toString());
				return false;
			}
			else if (!data.bag().itemConnectedToMana(item)) {
				addLog(item.toString() + " n'est pas connecté à une source de mana");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Uses an item against the targeted enemy or all enemies.
	 * Removes the item from the backpack, consumes resources,
	 * applies the effect and re-adds the item if still usable.
	 *
	 * @param data global game data
	 * @param item item to use
	 */
	private static void useItemOnEnemies(GameData data, Item item) {
		data.bag().removeItemFromBackpack(item);
		data.hero().sub("energy", item.info().AP());
		nbMana -= item.info().mana();
		var newItem = item.use(target, lstEnemy, data);
		if (newItem.durability() != 0) data.bag().addItemToBackpack(newItem);
	}

	/**
	 * Ends the combat when all enemies are defeated.
	 * Restores hero energy, grants experience, score,
	 * unlocks inventory slots and generates a reward item.
	 *
	 * @param data global game data
	 */
	private static void endCombat(GameData data) {
		GameDataHero.add("energy", (data.hero().getMaxEnergyPoint() - data.hero().getEnergyPoint()));
		GameDataHero.add("xp", totalExp);
		data.addScore(totalExp);
		for (int i = 0; i < data.hero().getLevel() - levelBeforeCombat; i++) {
			Random r = new Random();
			data.bag().addCaseUnlock(3 + r.nextInt(1));
		}
		var itemGain = RandomItem.generate(data.floor());
		GameDataClick.addDragItem(itemGain);
		combat = false;
		data.updateBG();
	}

	/**
	 * Checks all enemies and removes those with zero or negative HP.
	 * Updates experience gain, enemy positions and target selection.
	 * Ends combat if no enemy remains.
	 *
	 * @param data global game data
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
				getEnemyBox(lstEnemy, data.hero().getSizeX(), data.hero().getSizeY());
			}
		}
		if (lstEnemy.isEmpty()) {
			endCombat(data);
		}
	}
	
	/**
	 * Ends the hero's turn.
	 * Applies effects to enemies and hero, triggers enemy actions,
	 * checks hero death and reapplies passive effects.
	 * 
	 * public method because we call it in GameDataClick when clicking the endbutton
	 *
	 * @param data global game data
	 */
	public static void endTour(GameData data) {
		Objects.requireNonNull(data);
		if (!curseEvent) {
			log = new ArrayList<>();
			applyEffectsToEnemy();
			applyEffectsToHero(data);
			killMonster(data);
			enemyAction(data);
			killMonster(data);
			if(data.hero().getHP() <= 0) {
				data.endGame();
			}
			if (data.floor() == 4) {
				data.endGame();
			}
			useAllPassive(data);
		}
	}
	
	/**
	 * Applies ongoing effects on all enemies,
	 * dealing damage and updating their effect durations.
	 */
	private static void applyEffectsToEnemy() {
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
	 * Applies ongoing effects on the hero,
	 * dealing damage and updating effect durations.
	 *
	 * @param data global game data
	 */
	private static void applyEffectsToHero(GameData data) {
		for (var effect : data.hero().getEffects().keySet()) {
			var dmg = effect.getDamage();
			data.hero().sub("hp", dmg);
			addLog("Le hero recoit " + dmg + " dégats à cause de l'effet " + effect);
		}
		data.hero().updateEffects();
	}
	
	/**
	 * Executes each enemy action during their turn
	 * and resets the hero energy for the next turn.
	 *
	 * @param data global game data
	 */
	private static void enemyAction(GameData data) {
		lstEnemy.forEach(enemy -> enemy.action(data));
		GameDataHero.reset();
		GameDataHero.add("energy", 3 - data.hero().getEnergyPoint());
	}
	
	/**
	 * Returns whether a combat is currently active.
	 *
	 * @return true if in combat, false otherwise
	 */
	public static boolean combat() {
		return combat;
	}
	
	/**
	 * Sets the currently targeted enemy.
	 *
	 * @param enemy enemy to target
	 */
	public static void setTarget(Enemy enemy) {
		Objects.requireNonNull(enemy);
		target = enemy;
	}
	
	/**
	 * Returns the currently targeted enemy.
	 *
	 * @return current target
	 */
	public static Enemy getTarget(){
		return target;
	}
	
	/**
	 * Returns the list of alive enemies.
	 *
	 * @return enemy list
	 */
	public static List<Enemy> getLstEnemy(){
		return lstEnemy;
	}
	
	/**
	 * Returns the bounding boxes of all enemies.
	 *
	 * @return enemy bounding boxes
	 */
	public static LinkedHashMap<Enemy, BoundingBox> getEnemyBox() {
		return enemyBox;
	}
	
	/**
	 * Adds a message to the combat log.
	 *
	 * @param content message to add
	 */
	public static void addLog(String content) {
		Objects.requireNonNull(content);
		log.add(content);
	}
	
	/**
	 * Returns an immutable copy of the combat log.
	 *
	 * @return combat log
	 */
	public static List<String> getLog() {
		return List.copyOf(log);
	}
	
	/**
	 * Sets the item currently hovered by the player.
	 *
	 * @param item hovered item
	 */
	public static void setHoverItem(Item item) {
		hoverItem = item;
	}
	
	/**
	 * Returns the currently hovered item.
	 *
	 * @return hovered item
	 */
	public static Item getHoverItem() {
		return hoverItem;
	}
	
	/**
	 * Returns the remaining mana during combat.
	 *
	 * @return available mana
	 */
	public static int getNbMana() {
		return nbMana;
	}
	
	/**
	 * Enables or disables the curse event state.
	 *
	 * @param bool curse state
	 */
	public static void setCurseEvent(boolean bool) {
		curseEvent = bool;
	}
	
	/**
	 * Returns whether a curse event is active.
	 *
	 * @return true if curse event is active
	 */
	public static boolean getCurseEvent() {
		return curseEvent;
	}
	
	/**
	 * Enables or disables the combat event state
	 *
	 * @param bool combat state
	 */
	public static void setCombatEvent(boolean bool) {
		combat = bool;
	}
	
}
