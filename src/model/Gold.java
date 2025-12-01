package model;

public class Gold {
	private int gold = 100;
	private int size_count = 1; // 1, 2, 3 or 4
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold2) {
		if (gold < 0) throw new IllegalArgumentException("Gold must be not negative");
		gold = gold2;
	}

	public void addGold(int value) {
		gold += value;
	}
	
	public void subGold(int value) {
		gold -= value;
	}
	
	public void updateGoldSize() {
		if (gold <= 10) size_count = 1;
		else if (gold <= 25) size_count = 2;
		else if (gold <= 50) size_count = 3;
		else size_count = 4;
	}
	
}
