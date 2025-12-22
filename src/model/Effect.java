package model;

public enum Effect {
	POISON(3);
	
	final private int damage;
	
	private Effect(int value) {
		damage = value;
	}
	
	public int getDamage() {
		return damage;
	}
	
}
