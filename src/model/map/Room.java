package model.map;

import java.util.Objects;

public class Room {
	final private XY[] accessible = new XY[4];
	final private Enemy[] opponent; // null or 1,2,3
	private Treasure treasure;
	private Shop shop;
	private boolean exit;
	
	
	public Room(Enemy[] opponent, Treasure treasure, Shop shop, boolean exit) {
		Objects.requireNonNull(exit);
		if (!isValide()) throw new IllegalArgumentException("The Room must be of a single type");
		this.opponent = opponent;
		this.treasure = new Treasure();
		this.shop = new Shop();
		this.exit = exit;
	}
	
	public boolean isValide() {
		int count = 0;
		if (shop != null) count++;
		if (treasure != null) count++;
		if (exit) count++;
		return (count == 1);
	}
	

}
