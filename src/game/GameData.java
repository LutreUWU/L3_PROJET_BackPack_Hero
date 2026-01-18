package game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.forax.zen.ScreenInfo;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Backpack;
import model.EnemyRepository;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.XY;
import model.item.common.KeyDoor;
import model.item.common.Sword;
import model.item.epic.Bow;
import model.map.Floor;
import model.map.Shop;
import model.map.eventManager.LinkedEvent;

/**
 * Stores all global data related to the current game session. This class
 * centralizes the state of the game such as hero data, inventory, map, events,
 * score, and screen information.
 *
 * GameData acts as the main data holder shared between the different systems of
 * the game (combat, UI, map, events, shop ...).
 */
public class GameData {
	/** Current background identifier */
	private static String BGName = "BG1";
	/** Indicates whether the game has ended */
	private boolean endGame = false;
	/** Indicates whether the score lobby screen is displayed */
	private boolean scoreLobby = false;
	/** Player backpack */
	private static Backpack backpack;
	/** Current floor map */
	private static Floor map;
	/** Player hero */
	private static Hero hero;
	/** Current floor */
	private static int floor;
	/** Screen size information */
	/** Item currently dragged by the player */
	private static Item dragItem = null;
	/** Indicates whether the dragged item is over the trash bin */
	private static boolean onBin = false;
	/** True if bag is displayed, false if map is displayed */
	private static boolean mapOrBag = true;
	/** Indicates whether the player is currently in a shop */
	private static boolean shop = false;
	/** Current shop instance */
	private static Shop shopLst;
	/** Current active event */
	private static LinkedEvent event;
	/** Current mouse position */
	private static XY mouseCoord;
	/** Player score */
	private static double score;
	/** Shortest path displayed on the map */
	private static List<XY> shortestPath = new ArrayList<>();

	/**
	 * Initializes all game data at the start of a new game. Creates hero, backpack,
	 * map, repositories and starter items.
	 *
	 * @param screenInfo_ screen dimensions information
	 */
	public GameData(ScreenInfo screenInfo_) {
		Objects.requireNonNull(screenInfo_);
		backpack = new Backpack(screenInfo_.height());
		hero = new Hero();
		floor = 1;
		score = 0.0;
		ItemRepository.createItemRepository();
		EnemyRepository.createEnemyRepository();
		map = new Floor(floor);
		new GameDataHero(hero);
		new GameDataClick(this, screenInfo_);
		addStarterPack();
	}

	/**
	 * Adds the initial items to the hero inventory when starting a new game.
	 */
	private void addStarterPack() {
		GameDataClick.addDragItem(new KeyDoor());
		GameDataClick.addDragItem(new Sword());
		GameDataClick.addDragItem(new Bow());
	}

	/**
	 * Toggles between map view and backpack view. True displays the bag, false
	 * displays the map.
	 */
	public void swapMapOrBag() {
		if (mapOrBag) {
			mapOrBag = false;
		} else {
			mapOrBag = true;
		}
	}

	/**
	 * Sets the shortest path on the map.
	 *
	 * @param shortestPath2 path to display
	 */
	public void setShortestPath(List<XY> shortestPath2) {
		Objects.requireNonNull(shortestPath2);
		shortestPath = shortestPath2;
	}

	/**
	 * Rotates an item and returns the rotated version.
	 *
	 * @param item item to rotate
	 * @return rotated item
	 */
	public static Item rotateItem(Item item) {
		Objects.requireNonNull(item);
		return item.rotateXY();
	}

	/**
	 * Ends the game, computes final score and writes it to the score file. Also
	 * stops combat and disables score lobby.
	 */
	public void endGame() {
		endGame = true;
		GameDataCombat.setCombatEvent(false);
		setShop(false, null);
		mapOrBag = true;
		scoreLobby = false;
		Path scoreFile = Path.of("data", "score");
		int newScore = (int) (score * (1.2 * floor + (hero().getLevel() / 2.0)));
		try {
			submitScore(scoreFile, newScore);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Submits a new score to the score file.
	 *
	 * @param path     score file path
	 * @param newScore score value
	 * @throws IOException if file access fails
	 */
	private void submitScore(Path path, int newScore) throws IOException {
		Map<String, Integer> scores = new HashMap<>();
		int nbPlayer = 1;
		if (Files.exists(path)) {
			try (var reader = Files.newBufferedReader(path)) {
				String line;
				while ((line = reader.readLine()) != null) {
					String[] parts = line.split(" : ");
					scores.put(parts[0], Integer.parseInt(parts[1]));
					nbPlayer++;
				}
			}
		}
		// Update Score
		scores.put("Joueur" + nbPlayer, newScore);
		writeScores(path, sortScore(scores));
	}

	/**
	 * Sorts scores in descending order.
	 *
	 * @param scores unsorted score map
	 * @return sorted score map
	 */
	private Map<String, Integer> sortScore(Map<String, Integer> scores) {
		return scores.entrySet().stream().sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, _) -> a, LinkedHashMap::new));
	}

	/**
	 * Writes scores to the score file.
	 *
	 * @param path   score file path
	 * @param scores score map
	 * @throws IOException if file writing fails
	 */
	private void writeScores(Path path, Map<String, Integer> scores) throws IOException {
		Objects.requireNonNull(scores);
		Objects.requireNonNull(path);
		try (var writer = Files.newBufferedWriter(path)) {
			for (var entry : scores.entrySet()) {
				writer.write(entry.getKey() + " : " + entry.getValue());
				writer.newLine();
			}
		}
	}

	/**
	 * Moves the player to the next floor. Generates a new map and updates the
	 * background.
	 */
	public void newFloor() {
		floor++;
		shortestPath = null;
		map = new Floor(floor);
		updateBG();
	}

	/**
	 * Enters an event and forces bag view.
	 *
	 * @param newEvent event to enter
	 * @throws NullPointerException if newEvent is null
	 */
	public void inEvent(LinkedEvent newEvent) {
		Objects.requireNonNull(newEvent);
		event = newEvent;
		if (!mapOrBag) {
			this.swapMapOrBag();
		}
	}

	/**
	 * Leaves the current event.
	 */
	public void outEvent() {
		event = null;
	}

	/**
	 * Sets the current mouse position.
	 *
	 * @param mouse_coord mouse coordinates
	 */
	public void setMouseCoord(XY mouse_coord) {
		Objects.requireNonNull(mouse_coord);
		GameData.mouseCoord = mouse_coord;
	}

	/**
	 * Sets whether the dragged item is over the trash bin.
	 *
	 * @param statut bin state
	 */
	public void setBin(boolean statut) {
		GameData.onBin = statut;
	}

	/**
	 * Sets the shop state and current shop instance.
	 *
	 * @param statut shop state
	 * @param shop shop instance
	 */
	public void setShop(boolean statut, Shop shop) {
		if (statut) {
			swapMapOrBag();
		}
		GameData.shop = statut;
		GameData.shopLst = shop;
	}

	/**
	 * Enables or disables the score lobby.
	 *
	 * @param statut score lobby state
	 */
	public void setScore(boolean statut) {
		this.scoreLobby = statut;
	}

	/**
	 * Sets the current background.
	 *
	 * @param name background identifier
	 */
	public void setBG(String name) {
		Objects.requireNonNull(name);
		BGName = name;
	}

	/**
	 * Updates the background based on the current floor.
	 */
	public void updateBG() {
		BGName = "BG" + floor;
	}

	/**
	 * Sets the currently dragged item.
	 *
	 * @param item dragged item
	 */
	public void setDragItem(Item item) {
		GameData.dragItem = item;
	}

	/**
	 * Returns the item currently being dragged.
	 *
	 * @return dragged item or null
	 */
	public Item dragItem() {
		return dragItem;
	}

	/**
	 * Returns whether the bag or map is currently displayed.
	 *
	 * @return true for bag, false for map
	 */
	public boolean mapOrBag() {
		return mapOrBag;
	}

	/**
	 * Returns the player backpack.
	 *
	 * @return backpack
	 */
	public Backpack bag() {
		return backpack;
	}

	/**
	 * Returns the current map.
	 *
	 * @return map
	 */
	public Floor map() {
		return map;
	}

	/**
	 * Returns the current floor number.
	 *
	 * @return floor number
	 */
	public int floor() {
		return floor;
	}

	/**
	 * Returns the hero instance.
	 *
	 * @return hero
	 */
	public Hero hero() {
		return hero;
	}

	/**
	 * Returns the current shortest path on the map.
	 *
	 * @return list of coordinates representing the path
	 */
	public List<XY> getShortestPath() {
		return shortestPath;
	}

	/**
	 * Returns the current event.
	 *
	 * @return event or null
	 */
	public LinkedEvent event() {
		return event;
	}

	/**
	 * Returns the current mouse position.
	 *
	 * @return mouse coordinates
	 */
	public XY getMouseCoord() {
		return mouseCoord;
	}

	/**
	 * Returns whether the player is in a shop.
	 *
	 * @return shop state
	 */
	public boolean getShop() {
		return shop;
	}

	/**
	 * Returns the current shop.
	 *
	 * @return shop
	 */
	public Shop getShopLst() {
		return shopLst;
	}

	/**
	 * Returns whether the dragged item is over the bin.
	 *
	 * @return bin state
	 */
	public boolean getBin() {
		return onBin;
	}

	/**
	 * Returns whether the score lobby is displayed.
	 *
	 * @return score lobby state
	 */
	public boolean getScoreLobby() {
		return scoreLobby;
	}

	/**
	 * Returns whether the game has ended.
	 *
	 * @return end game state
	 */
	public boolean getEndGame() {
		return endGame;
	}

	/**
	 * Returns the current score.
	 *
	 * @return score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Adds value to the score.
	 *
	 * @param value score increment
	 */
	public void addScore(int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Value must be positive");
		}
		score += value;
	}

	/**
	 * Returns the current background name.
	 *
	 * @return background name
	 */
	public String getBG() {
		return BGName;
	}
}
