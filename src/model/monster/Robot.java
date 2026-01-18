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
 * Class of a Robot
 */
public class Robot implements Enemy{
  /** Maximum health points*/
	private int HP = 50;
  /** Shield points*/
	private int shield = 0;
  /** Active of the enemy*/
	private String action;
  /** Active effects on the enemy and their remaining duration. */
	private final Map<Effect, Integer> effects = new HashMap<>();
  /** Static information about the enemy (damage, attacks, sizeX, sizeY, etc.) */
	private static final EnemyInfo info = new EnemyInfo(50, 20, List.of("Hoo...", "HOHOHOH", "HEHEHEH"), 1.2, 1.2, "robot");

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
			case "Hoo..." -> {
				this.subHP(3);
				GameDataCombat.addLog("Robot n'a pas eu de chance et perd 3PV ...");
			}
			case "HOHOHOH" -> {
				GameDataHero.sub("HP", 15);
				GameDataCombat.addLog("Robot est content et inflige 15PV au héro");
			}
			case "HEHEHEH" -> {
				shield = 20;
				GameDataCombat.addLog("Robot est peureux et gagne 20 de shield");
			}
			default -> {throw new IllegalArgumentException("Invalid attack : " + action);}
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
		return "Robot";
	}
}
