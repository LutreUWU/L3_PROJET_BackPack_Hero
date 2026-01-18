package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the player hero in the game.
 * 
 * The hero has statistics such as HP, armor, protection, energy points, mana,
 * experience, level, boost damage, and effects. This class provides methods
 * to manipulate and update these stats, including adding or subtracting
 * values, resetting temporary buffs, and managing status effects.
 */
public class Hero {
  /** Current health points of the hero */
  private int HP = 40;
  /** Maximum health points of the hero */
  private int maxHP = 40;
  /** Current temporary protection that absorbs damage before HP */
  private int currentProtection = 0; 
  /** Current armor that may reduce incoming damage */
  private int currentArmor = 0;
  /** Maximum energy points */
  private int maxEnergyPoint = 3;
  /** Current energy points */
  private int energyPoint = maxEnergyPoint;
  /** Current mana points */
  private int manaPoint = 0;
  /** Number of curses the hero can refuse */
  private int curseRefuse = 0;
  /** Percentage bonus added to hero damage */
  private float boostDmg = 0;
  /** Active effects applied to the hero and their remaining duration */
  private final Map<Effect, Integer> effects = new HashMap<>();
  /** Current experience points */
  private int xp = 0;
  /** Hero level */
  private int level = 1;
  /** Hero's width for graphical display */
  private int sizeX = 150;
  /** Hero's height for graphical display */
  private int sizeY = (int) (sizeX * 1.2);
	
  /**
   * Computes the maximum XP required for the next level.
   * 
   * @return maximum experience points for the current level
   */
	public int maxXP() {
		return 10 + (level - 1) * 2;
	}
	
	 /**
   * Sets the hero's position.
   * 
   * @param coord new coordinates of the hero
   * @throws NullPointerException if coord is null
   */
	public void setPos(XY coord) {
		Objects.requireNonNull(coord);
	}
	
	 /**
   * Adds a value to one of the hero's attributes.
   * Automatically handles HP overflow and leveling up if XP exceeds maxXP.
   * 
   * @param string Name of the attribute (hp, maxHP, protection, armor, energy, mana, xp, curse)
   * @param value  Amount to add
   */
	public void add(String string, int value) {
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
   * Subtracts a value from one of the hero's attributes.
   * Damage is first applied to protection, then to HP if needed.
   * 
   * @param string Name of the attribute (hp, maxHP, protection, armor, energy, mana)
   * @param value  Amount to subtract
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
	
	/**
   * Checks protection and applies remaining damage to HP.
   * 
   * @param value damage value to apply
   */
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

	/**
   * Resets temporary stats such as protection and armor.
   */
	public void reset() {
		currentProtection = 0;
		currentArmor = 0;
		
	}
	 /** Resets damage boost to zero */
	public void resetBoostDmg() {
		boostDmg = 0;
	}
	
	/**
   * Adds a boost to hero damage.
   * 
   * @param value value to add
   * @throws IllegalArgumentException if value is not positive
   */
	public void addBoostDmg(int value) {
		if (value <= 0) throw new IllegalArgumentException("! VALUE MUST BE NOT NEGATIVE !");
		boostDmg += value;
	}
	
	/**
   * Subtracts from hero damage boost.
   * 
   * @param value value to subtract
   * @throws IllegalArgumentException if value is not positive
   */
	public void subBoostDmg(int value) {
		if (value <= 0) throw new IllegalArgumentException("! VALUE MUST BE NOT NEGATIVE !");
		boostDmg -= value;
	}
	
	/**
   * Adds an effect to the hero. If the effect already exists, its duration is updated if higher.
   * 
   * @param effect the effect to add
   * @param value remaining turns for the effect
   */
	public void addEffect(Effect effect, int value) {
		Objects.requireNonNull(effect);
		if (value <= 0) throw new IllegalArgumentException("! VALUE MUST BE NOT NEGATIVE !");
		if(effects.getOrDefault(effect, -1) < value) effects.put(effect, value);
	}
	
	 /**
   * Updates all effects, decrementing their duration.
   * Removes effects whose duration reaches zero.
   */
	public void updateEffects() {
    effects.replaceAll((_, v) -> v - 1);
    effects.values().removeIf(v -> v <= 0);
	}

	 /** Getter for active effects */
	public Map<Effect, Integer> getEffects() {
		return effects;
	}
	
	/** Sets current HP directly */
	public void setHP(int value) {
		HP = value;
	}
	
	/** Getter for curse refusal count */
	public int getCurseRefuse() {
		return curseRefuse;
	}
	
	/** Getter for maximum HP */
	public int getMaxHP() {
		return maxHP;
	}

	 /** Getter for current HP */
	public int getHP() {
		return HP;
	}

  /** Getter for current protection */
	public int getCurrentProtection() {
		return currentProtection;
	}

  /** Getter for current armor */
	public int getCurrentArmor() {
		return currentArmor;
	}

  /** Getter for current energy points */
	public int getEnergyPoint() {
		return energyPoint;
	}

  /** Getter for current mana points */
	public int getManaPoint() {
		return manaPoint;
	}
	
  /** Getter for maximum energy points */
	public int getMaxEnergyPoint() {
		return maxEnergyPoint;
	}

  /** Getter for XP */
	public int getXp() {
		return xp;
	}
	
  /** Getter for graphical width */
	public int getSizeX() {
		return sizeX;
	}
	
  /** Getter for graphical height */
	public int getSizeY() {
		return sizeY;
	}

  /** Getter for hero level */
	public int getLevel() {
		return level;
	}

  /** Getter for damage boost */
	public float getBoostDmg() {
		return boostDmg;
	}
}
