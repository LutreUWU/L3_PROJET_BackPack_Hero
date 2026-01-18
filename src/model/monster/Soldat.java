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
 * Class of a Soldat
 */
public class Soldat implements Enemy{
  /** Maximum health points*/
	private int HP = 25;
  /** Shield points*/
	private int shield = 0;
  /** Active of the enemy*/
	private String action;
  /** Active effects on the enemy and their remaining duration. */
	private final Map<Effect, Integer> effects = new HashMap<>();
  /** Static information about the enemy (damage, attacks, sizeX, sizeY, etc.) */
	private static final EnemyInfo info = new EnemyInfo(25, 6, List.of("Coup", "Bouclier"), 1, 1, "soldat");

	/**
	 * Default constructor that does nothing
	 */
	public void soldat() {}
	
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
			case "Coup" -> {
				GameDataHero.sub("HP", 5);
				GameDataCombat.addLog("Soldat a infligé 5PV au héro");
			}
			case "Bouclier" -> {
				shield += 10;
				GameDataCombat.addLog("Soldat s'applique un shield de 10 PV");
			}
		}
		preAction();
	}
	
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
		return "Soldat";
	}
}
