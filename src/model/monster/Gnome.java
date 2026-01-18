package model.monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Effect;

/**
 * Class of a Gnome
 */
public class Gnome implements Enemy{
  /** Maximum health points*/
	private int HP = 15;
  /** Shield points*/
	private int shield = 0;
  /** Active effects on the enemy*/
	private String action;
  /** Active effects on the enemy and their remaining duration. */
	private final Map<Effect, Integer> effects = new HashMap<>();
  /** Static information about the enemy (damage, attacks, sizeX, sizeY, etc.) */
	private static final EnemyInfo info = new EnemyInfo(15, 4, List.of("Slash", "Abattage"), 1, 0.8, "gnome");

	@Override
	public void resetStats() {
		HP = info.maxHP();
		shield = 0;
		effects.clear();
	}

	@Override
	public void addEffect(Effect effect, int value) {
		if(effects.getOrDefault(effect, -1) < value) effects.put(effect, value);
	}
	
	@Override
	public void updateEffects() {
    effects.replaceAll((_, v) -> v - 1);
    effects.values().removeIf(v -> v <= 0);
	}
	
	@Override
	public String preAction() {
		Random randomNumbers = new Random();
		action = info.attacks().get(randomNumbers.nextInt(2));
		return action;
	}
	
	@Override
	public void action(GameData data) {
		switch(action) {
			case "Slash" ->{
				GameDataHero.sub("HP", 5);
				GameDataCombat.addLog("Le gnome slash le héro (-5PV)");
			}
			case "Abattage" -> {
				GameDataHero.sub("HP", 10);
				this.subHP(3);
				GameDataCombat.addLog("Le gnome perd 3PV, mais slash fort le héro (-10PV)");
				}
			default -> {throw new IllegalArgumentException("Invalid attack : " + action);}
		}
		preAction();
	}
	
	/**
	 * Sub the HP by the value
	 * If he has a shield, subtract it
	 * 
	 */
	@Override
	public void subHP(int value) {
		if(shield >= value) {
			shield -= value;
			return;
		}
		HP -= value - shield;
		shield = 0;
	}
	
	// Getter 
	@Override
	public int getHP() {
		return HP;
	}
	
	@Override
	public Map<Effect, Integer> getEffects() {
		return effects;
	}
	
	@Override
	public int getShield() {
		return shield;
	}
	
	@Override
	public String getAction() {
		return action;
	}
	
	@Override
	public EnemyInfo getInfo() {
		return info;
	}
	
	@Override
	public String toString() {
		return "Gnome méchant";
	}
}
