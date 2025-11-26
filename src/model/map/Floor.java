package model.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import model.XY;
import model.monster.Enemy;

public class Floor {
	
	private final int ROW = 11;
  private final int LINE = 5;
	
	private Room[][] grid = new Room[LINE][ROW];
	
	// Count of each room
	private final int SHOP_COUNT = 1;
  private final int TREASURE_COUNT = 2;
  private final int EXIT_COUNT = 1;
  private final int ENEMY_COUNT = 3;
  private final int HEALER_COUNT = 1;
  
  final private HashSet<XY> hero_visited = new HashSet<>();
  final private HashSet<XY> hero_accessible = new HashSet<>();
  final private HashSet<XY> hero_visible = new HashSet<>();
  final private HashSet<XY> hero_visible_for_line = new HashSet<>();
  private XY hero_pos;

  
  public Floor(int floor) {
  	XY start = createAllRoom(1);
  	HashSet<XY> visited = new HashSet<>();
  	hero_visited.add(start);
  	createWay(visited, start);
  	hero_pos = start;
  	updateHeroAccessible();
  	updateHeroVisible();
  }


	public void setHero_pos(XY hero_pos) {
		this.hero_pos = hero_pos;
	}




	private void createWay(HashSet<XY> visited, XY start) {
    List<XY> accessible = new ArrayList<>(); addAcc(accessible, start.x(), start.y());
    for (var coord : accessible) {
        if (!visited.contains(coord)) {
          visited.add(coord);
          grid[start.y()][start.x()].addAccessible(coord);
          grid[coord.y()][coord.x()].addAccessible(start);
          createWay(visited, coord);
        }
    }
  }
  
  private void addAcc(List<XY> listacc, int x, int y) {
  	if (x > 0) listacc.add(new XY(x - 1, y));
  	if (y > 0) listacc.add(new XY(x, y - 1));
  	if (x < ROW - 1) listacc.add(new XY(x + 1, y));
  	if (y < LINE - 1) listacc.add(new XY(x, y + 1));
  	Collections.shuffle(listacc);
  }
  
  /*
  public void createAndRandomAccessible() {
  	for (int i = 0; i < LINE; i++) {
  		for (int j = 0; j < ROW; j++) {
  			List<XY> listacc = new ArrayList<>();
  			addAcc(listacc, j, i);
  			grid[i][j].setAccessible(listacc);
  		}
  	}
  }*/
  
  /**
   * Create All Room
   * @param floor
   * @return Starter Room
   */
  private XY createAllRoom(int floor) {
  	List<XY> list1 = createXYList();
  	List<XY> list2 = shuffleList(list1);
  	createSpecialRoom(list2, 1);
  	for (int i = 0; i < LINE; i++) {
  		for (int j = 0; j < ROW; j++) {
  			if (grid[i][j] == null) {
  				grid[i][j] = new Room(null, null, null, null, false, false);
  			}
  		}
  	}
  	return new XY(list2.get(8).x(), list2.get(8).y());
  }
  
  /**
   * Create all special room
   * @param list
   * @param floor
   */
  private void createSpecialRoom(List<XY> list, int floor) {
  	grid[list.get(0).y()][list.get(0).x()] = new Room(null, null, new Shop(floor), null, false, false); // Create shop
  	grid[list.get(1).y()][list.get(1).x()] = new Room(null, new Treasure(floor), null, null, false, false); // Create treasure
  	grid[list.get(2).y()][list.get(2).x()] = new Room(null, new Treasure(floor), null, null, false, false); // Create treasure
  	grid[list.get(3).y()][list.get(3).x()] = new Room(null, null, null, null, true, false); // Create exit
  	grid[list.get(4).y()][list.get(4).x()] = new Room(createEnemyList(floor), null, null, null, false, false); // Create Enemy
  	grid[list.get(5).y()][list.get(5).x()] = new Room(createEnemyList(floor), null, null, null, false, false); // Create Enemy
  	grid[list.get(6).y()][list.get(6).x()] = new Room(createEnemyList(floor), null, null, null, false, false); // Create Enemy
  	grid[list.get(7).y()][list.get(7).x()] = new Room(null, null, null, new Healer(floor), false, false); // Create shop
  	grid[list.get(8).y()][list.get(8).x()] = new Room(null, null, null, null, false, true); // Create start
  }
  
  public Enemy[] createEnemyList(int floor) {
  	// Classe a faire quand on aura bien organisé et a mettre dans la classe Enemy
  	return new Enemy[floor];
  }
  
  /**
   * @return a list of 55 XY
   */
  private List<XY> createXYList() {
  	List<XY> list = new ArrayList<>();
  	for (int i = 0; i < ROW; i++) {
  		for (int j = 0; j < LINE; j++) {
  			list.add(new XY(i, j));
  		}
  	}
  	return list;
  }
  
  /**
   * @param list of 55 XY
   * @return a new list shuffled
   */
  private List<XY> shuffleList(List<XY> list) {
  	List<XY> list2 = new ArrayList<>(list);
  	Collections.shuffle(list2);
  	return list2;
  }
  
  public void updateHeroVisible() {
  	for (var coord : hero_accessible) {
  		hero_visible.add(coord);
  		
  		for (var coord_acc : grid[coord.y()][coord.x()].get_accessible()) {
  			hero_visible.add(coord_acc);
  			hero_visible_for_line.add(coord_acc);
  			for (var coord_acc2 : grid[coord_acc.y()][coord_acc.x()].get_accessible()) {
  				hero_visible.add(coord_acc2);
  			}
  		}
  	}
  }
  
  public void updateHeroAccessible() {
  	hero_accessible.clear();
  	for (var coord : hero_visited) {
  		for (var coord_acc : grid[coord.y()][coord.x()].get_accessible()) {
  			if (!hero_visited.contains(coord_acc)) hero_accessible.add(coord_acc);
  		}
  	}
  }
  
  public Room[][] getGrid() {
    return grid;
  }
  
  public void addHeroVisited(XY coord) {
  	hero_visited.add(coord);
  }
  
  public HashSet<XY> getHeroVisited() {
    return hero_visited;
  }
  
  public HashSet<XY> getHeroVisible() {
    return hero_visible;
  }
  
  public HashSet<XY> getHeroVisibleLine() {
    return hero_visible_for_line;
  }
  
  public HashSet<XY> getHeroAccessible() {
    return hero_accessible;
  }
  
  public XY get_heroPos() {
  	return hero_pos;
  }
  
  @Override
  public String toString() {
  	StringBuilder chaine = new StringBuilder();
  	for (int i = 0; i < LINE; i++) {
  		for (int j = 0; j < ROW; j++) {
  			chaine.append(grid[i][j].letterRoom()).append(" ");
  		}
  		chaine.append("\n");
  	}
  	return chaine.toString();
  }
  
}
