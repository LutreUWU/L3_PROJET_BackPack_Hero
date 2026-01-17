package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import model.item.common.Gold;

public class Hero {
  // Statistics of the hero
	private int HP = 40;
	private int maxHP = 40;
	private int currentProtection = 0; // damage countered
	private int currentArmor = 0; // as a percentage (between 0 and 100%)
	private int energyPoint = 3;
	private int maxEnergyPoint = 3;
	private int manaPoint = 0;
	private int curseRefuse = 0;
	private float boostDmg = 0;
	private final Map<Effect, Integer> effects = new HashMap<>();
	
	private int xp = 0; // (between 0 and 10 + (level - 1) * 2)
	private int level = 1;
	
	// For graphic interface
	private int sizeX = 150;
	private int sizeY = (int) (sizeX * 1.2);
	
	
	public int maxXP() {
		return 10 + (level - 1) * 2;
	}
	
	//==============================
  //   METHODS FOR ATTRIBUTES
  //==============================
	
	public void setPos(XY coord) {
		Objects.requireNonNull(coord);
	}
	
	public void add(String string, int value) {
		/**
		 * Function to add for all attribtues
		 * @param string : What we want to add
		 * @param value : how many points to add
		 */
		switch(string.toLowerCase()) {
			case "maxHP" -> maxHP += value;
			case "hp" -> {HP += value;
										if (HP > maxHP) HP = maxHP;}
			case "protection" -> currentProtection += value;
			case "armor" -> currentArmor += value;
			case "energy" -> energyPoint += value;
			case "mana" -> manaPoint += value;
			case "xp" -> {xp += value; while (xp >= maxXP()) {xp -= maxXP(); level++;}}
			case "curse" -> curseRefuse += value;
		}
	}
	/**
	 * Function to substract for all attribtues
	 * @param string : What we want to substract
	 * @param value : how many points to substract
	 */
	public void sub(String string, int value) {
		switch(string.toLowerCase()) {
			case "maxhp" -> {maxHP -= value;
												if (HP > maxHP) HP = maxHP;
												if (currentProtection > maxHP) currentProtection = maxHP;
			}
			case "hp" -> checkProtection(value);
			case "protection" -> currentProtection -= value;
			case "armor" -> currentArmor -= value;
			case "energy" -> energyPoint -= value;
			case "mana" -> manaPoint -= value;
		}
	}
	
	private void checkProtection(int value) {
		int remainingDamage = value;
    if (currentProtection > 0) {
        int absorbed = Math.min(currentProtection, remainingDamage);
        currentProtection -= absorbed;
        remainingDamage -= absorbed;
    }
    if (remainingDamage > 0) {
        HP -= remainingDamage;
    }
	}

	public void reset() {
		/**
		 * Function to reset for some attribtues
		 * @param string : What we want to reset
		 */
		currentProtection = 0;
		currentArmor = 0;
		
	}
	
	// Getter
	public int getCurseRefuse() {
		return curseRefuse;
	}
	
	public int getMaxHP() {
		return maxHP;
	}

	public int getHP() {
		return HP;
	}

	public int getCurrentProtection() {
		return currentProtection;
	}

	public int getCurrentArmor() {
		return currentArmor;
	}

	public int getEnergyPoint() {
		return energyPoint;
	}

	public int getManaPoint() {
		return manaPoint;
	}
	
	public int getMaxEnergyPoint() {
		return maxEnergyPoint;
	}

	public int getXp() {
		return xp;
	}
	
	public int getSizeX() {
		return sizeX;
	}
	
	public int getSizeY() {
		return sizeY;
	}

	public int getLevel() {
		return level;
	}
	
	public float getBoostDmg() {
		return boostDmg;
	}
	
	public void resetBoostDmg() {
		boostDmg = 0;
	}
	
	public void addBoostDmg(int value) {
		if (value <= 0) throw new IllegalArgumentException("! VALUE MUST BE NOT NEGATIVE !");
		boostDmg += value;
	}
	
	public void subBoostDmg(int value) {
		if (value <= 0) throw new IllegalArgumentException("! VALUE MUST BE NOT NEGATIVE !");
		boostDmg -= value;
	}
	
	public void addEffect(Effect effect, int value) {
		if(effects.getOrDefault(effect, -1) < value) effects.put(effect, value);
	}
	
	/**
	 * Update all effects and remove them if necessary
	 */
	public void updateEffects() {
    effects.replaceAll((_, v) -> v - 1);
    effects.values().removeIf(v -> v <= 0);
	}

	public Map<Effect, Integer> getEffects() {
		return effects;
	}
	
	public void setHP(int value) {
		HP = value;
	}
}
