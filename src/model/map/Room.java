package model.map;

import java.util.ArrayList;
import java.util.List;

import model.monster.Enemy;

public class Room {
	private List<XY> accessible = new ArrayList<>();
	
	final private Enemy[] opponent; // null or 1,2,3
	private Treasure treasure;
	private Shop shop;
	private Healer healer;
	private boolean exit;
	private boolean start;
	
	
	public Room(Enemy[] opponent, Treasure treasure, Shop shop, Healer healer, boolean exit, boolean start) {
    this.opponent = opponent;
    this.treasure = treasure;
    this.shop = shop;
    this.healer = healer;
    this.exit = exit;
    this.start = start;

    if (!isValide()) throw new IllegalArgumentException("Room must have exactly one type");
	}
	
	public void setAccessible(List<XY> list) {
    this.accessible = list;
	}
	
	public boolean isValide() {
		int count = 0;
		if (opponent != null) count++;
		if (shop != null) count++;
		if (treasure != null) count++;
		if (healer != null) count++;
		if (exit) count++;
		if (start) count++;
		return (count <= 1);
	}
	
	public char letterRoom() {
		if (opponent != null) return 'O';
		if (treasure != null) return 'T';
		if (shop != null) return 'S';
		if (healer != null) return 'H';
		if (exit) return 'E';
		if (start) return '$';
		return '.';
	}
	
	public List<XY> get_accessible(){
		return accessible;
	}
}