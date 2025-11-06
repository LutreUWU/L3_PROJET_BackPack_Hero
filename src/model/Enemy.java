package model;

import java.util.Objects;

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
		if (HP < 0) HP = 0;
	}
	
	public boolean isDead(Hero hero) {
		if (HP <= 0) {
			Objects.requireNonNull(hero);
			hero.add("xp", (int) max_HP / 4);
		};
		return false;
	}
	
}