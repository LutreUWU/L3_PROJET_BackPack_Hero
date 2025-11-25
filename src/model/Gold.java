package model;

public class Gold {
	private int gold = 0;
	
	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void addGold(int value) {
		gold += value;
	}
	
	public void subGold(int value) {
		gold += value;
	}
}
