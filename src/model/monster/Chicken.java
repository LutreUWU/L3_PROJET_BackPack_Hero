package model.monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Effect;


/**
 * Represents a Chicken enemy in the game.
 * 
 * Attributes:
 * - HP: Health points. When it reaches 0, the enemy dies.
 * - Shield: Absorbs incoming damage before HP is reduced.
 * - Effects: Status effects applied to the enemy (e.g., poison, burn).
 * - Action: The action the enemy will perform on its next turn.
 */
public class Chicken implements Enemy{
  /** Current health points. */
	private int HP = 20;
  /** Current shield value. */
	private int shield = 0;
  /** Active effects on the enemy and their remaining duration. */
	private final Map<Effect, Integer> effects = new HashMap<>();
  /** Current action the enemy will perform next turn. */
	private String action;
  /** Static information about the enemy (damage, attacks, drop chance, etc.) */
	private static final EnemyInfo info = new EnemyInfo(20, 4, List.of("Morsure", "Protection"), 0.8, 0.4, "chicken");
	
	/**
	 * Default constructor that does nothing
	 */
	public Chicken() {}
	
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
		Objects.requireNonNull(data);
		switch(action) {
			case "Morsure" -> {
				GameDataHero.sub("HP", 3);
				GameDataCombat.addLog("Le poulet malicieux mord le héro (-3PV)");
				GameDataCombat.addLog("La morsure du poulet t'empoisonne");
				data.hero().addEffect(Effect.POISON, 2);
				}
			case "Protection" -> {
				shield += 2;
				GameDataCombat.addLog("Le poulet malicieux se protège (+2 Shield)");
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
	
  // ========================= GETTERS =========================
	
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
		return "Poulet malicieux";
	}
}
