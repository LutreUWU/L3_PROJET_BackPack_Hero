package model;

public class Enemy {
	private int HP;
	private int max_HP;
	private int location; // 1, 2, or 3
	private String name;
	
	public int getHP() {
		return HP;
	}
	
	public void subHP(int value) {
		HP -= value;
	}
	
	public boolean isDead() {
		return HP <= 0;
	}
	
}