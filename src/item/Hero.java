package item;

public class Hero {
    // Statistics of the hero
	private int max_HP = 40;
	private int HP = 40;
	
	private int current_protection = 0; // damage countered
	private int current_armor = 0; // as a percentage (between 0 and 100%)
	private int energy_point = 3;
	private int mana_point = 0;
	private int gold = 0;
	
	private int xp = 0; // (between 0 and 10 + (level - 1) * 2)
	private int level = 1;
	
	
	// Getter
	public int getMax_HP() {
		return max_HP;
	}

	public int getHP() {
		return HP;
	}

	public int getCurrent_protection() {
		return current_protection;
	}

	public int getCurrent_armor() {
		return current_armor;
	}

	public int getEnergy_point() {
		return energy_point;
	}

	public int getMana_point() {
		return mana_point;
	}

	public int getGold() {
		return gold;
	}

	public int getXp() {
		return xp;
	}

	public int getLevel() {
		return level;
	}

}
