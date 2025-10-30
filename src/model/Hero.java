package model;

public class Hero {
	// HP Stats
	private int max_HP = 40;
	private int HP = 40;
	
	private int energy_point = 3;
	private int mana_point = 0;
	private int gold = 0;
	
	
	public void addSubHP(int value) {
		HP += value;
	}
	
	public void addSubMaxHP(int value) {
		max_HP += value;
	}
}
