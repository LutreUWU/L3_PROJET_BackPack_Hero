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
import java.util.Random;
import java.util.stream.Collectors;

import com.github.forax.zen.ScreenInfo;

import game.data.GameDataClick;
import game.data.GameDataHero;
import model.Backpack;
import model.EnemyRepository;
import model.Hero;
import model.Item;
import model.ItemRepository;
import model.XY;
import model.map.Floor;
import model.map.Shop;
import model.map.eventManager.LinkedEvent;

 /**
 * The SimpleGameData class stores all relevant pieces of information for the
 * game status.
 * 
 */
public class GameData {
	private boolean endGame = false;
  private boolean scoreLobby = false;
  private static Backpack backpack;
  private static Floor map;
  private static Hero hero;
  private static int floor;
  private static ScreenInfo screenInfo;
  private Item dragItem = null; 
  private boolean onBin = false;
  private boolean mapOrBag = true;
  private boolean shop = false;
  private Shop shopLst;
  private LinkedEvent event;
  private XY mouseCoord;
  /**
   * Sortest Path
   */
  private List<XY> shortestPath = new ArrayList<>();
  
  /**
   * Initialize data of the game 
   * 
   * @param gridSize size of the grid in the backpack
   */
  public GameData(ScreenInfo screenInfo_) {
  	Objects.requireNonNull(screenInfo_);
	  backpack = new Backpack(screenInfo_.height());
	  hero = new Hero(); 
	  floor = 1;
	  ItemRepository.createItemRepository();
	  EnemyRepository.createEnemyRepository();
	  map = new Floor(floor);
	  screenInfo = screenInfo_;
    new GameDataHero(hero);
    new GameDataClick(this);
	}

  /**
   * Switch the current value of the var mapOrBag
   * - true : We wants to display Bag
   * - false : We wants to display Map
   */
  public void swapMapOrBag() {
  	if (mapOrBag) {
  		mapOrBag = false;
  	} else {
  		mapOrBag = true;
  	}
  }
  
  public List<XY> getShortestPath () {
  	return shortestPath;
  }
  
  public void setShortestPath(List<XY> shortestPath2) {
  	Objects.requireNonNull(shortestPath2);
  	shortestPath = shortestPath2;
  }
  
  public static Item rotateItem(Item item) {
  	Objects.requireNonNull(item);
  	return item.rotateXY();
  }
  
  public void endGame(){
  	endGame = true;
  	scoreLobby = false;
  	Path scoreFile = Path.of("data", "score");
  	Random r= new Random();
  	double min = 0.8;
  	double max = 1.0;
  	var score = min + r.nextDouble() * (max - min) + 0.8 * floor * hero.getHP() + backpack.getGoldInBag() * 2;
  	try {
			submitScore(scoreFile, (int) score);
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
  
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
	  // Mettre à jour le score
	  scores.put("Joueur" + nbPlayer, newScore);
	  writeScores(path, sortScore(scores));
  }
  
  private Map<String, Integer> sortScore(Map<String, Integer> scores) {
  	return scores.entrySet()
  							 .stream()
  							 .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
  							 .collect(Collectors.toMap(Map.Entry::getKey, 
  									 											 Map.Entry::getValue,
  									 											 (a, b) -> a,
  									 											 LinkedHashMap::new));
  }
  
  private void writeScores(Path path, Map<String, Integer> scores) throws IOException {
	  try (var writer = Files.newBufferedWriter(path)) {
	    for (var entry : scores.entrySet()) {
	      writer.write(entry.getKey() + " : " + entry.getValue());
	      writer.newLine();
	    }
	  }
  }

  // ============
  // == GETTER ==
  // ============
  
  /**
   * Return the weapon we drags
   * 
   * @return Item_Object weapon
   */
  public Item dragItem() {
    return dragItem;
  }
  
  /**
   * Return the status of the button
   * 
   * @return true :  we display map
   * 				 false : we display bag
   */
  public boolean mapOrBag() {
  	return mapOrBag;
  }
 
  /**
   * Add the weapon we wants to move in the data.
   * If we don't add an item, this information is null.
   * 
   * @param item
   */
  public void setDragItem(Item item) {
    this.dragItem = item;
  }
  
  /**
   * Return the current backpack of the player from data
   * @return Objects backpack
   */
  public Backpack bag() {
    return backpack;
  }
  /**
   * Return the current map of the player from data
   * @return Objects MapGame
   */
  public Floor map() {
    return map;
  }
  
  /**
   * Go up one floor
   */
  public void newFloor() {
  	floor++;
  	shortestPath = null;
    map = new Floor(floor);
  }
  
  /**
   * Return the current floor of the player from data
   * @return int floor
   */
  public int floor() {
    return floor;
  }

  /**
   * Return the current hero's statue from data
   * @return
   */
  public Hero hero() {
    return hero;
  }
  
  public void inEvent(LinkedEvent new_event) {
  	event = new_event;
  	if (!mapOrBag) {
  		this.swapMapOrBag();
  	}
  }
  
  public void outEvent() {
  	event = null;
  }
  
  
  /**
   * Boolean to know if we're in a event or no
   * 
   * @return
   */
  public LinkedEvent event() {
    return event;
  }
  
  /**
   * Return the width and height of the screen
   * 
   * @return
   */
  public ScreenInfo screenInfo() {
    return screenInfo;
  }
 
	public XY getMouseCoord() {
		return mouseCoord;
	}

	public void setMouseCoord(XY mouse_coord) {
		this.mouseCoord = mouse_coord;
	}
	
	public void setBin(boolean statut) {
		this.onBin = statut;
	}
	
	public boolean getShop() {
		return shop;
	}
	
	
	public void setShop(boolean statut, Shop shop) {
		if (statut) swapMapOrBag();
		this.shop = statut;
		this.shopLst = shop;
	}
	
	public Shop getShopLst() {
		return shopLst;
	}
	
	public boolean getBin() {
		return onBin;
	}
	
	public void setScore(boolean statut) {
		this.scoreLobby = statut;
	}
	
	public boolean getScore() {
		return scoreLobby;
	}
	
	public boolean getEndGame() {
		return endGame;
	}
}
