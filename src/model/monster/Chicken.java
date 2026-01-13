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
 * Class of a Chicken
 */
public class Chicken implements Enemy{
	/**
	 * - HP :				 	When it reach 0, the enemy die
	 * - Shield : 		Can mitigate damage received
	 * - action : 		To register which action the enemy will do next turn
	 */
	final private int maxHP = 20;
	private int HP = 20;
	private int shield = 0;
	private final Map<Effect, Integer> effects = new HashMap<>();
	private String action;
	private static final EnemyInfo info = new EnemyInfo(20, 4, List.of("Morsure", "Protection"), 0.8, 0.4, "chicken");
	
	@Override
	public void resetStats() {
		HP = maxHP;
		shield = 0;
		effects.clear();
	}
	
	/**
	 * Add an effect to the enemy
	 * @param effect (Enum of all item)
	 * @param value (Number of time the effect will be used)
	 */
	@Override
	public void addEffect(Effect effect, int value) {
		if(effects.getOrDefault(effect, -1) < value) effects.put(effect, value);
	}
	
	/**
	 * Update all effects and remove them if necessary
	 */
	@Override
	public void updateEffects() {
    effects.replaceAll((k, v) -> v - 1);
    effects.values().removeIf(v -> v <= 0);
	}
	
	/**
	 * Chose randomly an action between all attacks the enemy has.
	 * 
	 * @return Action he'll do 
	 */
	@Override
	public String preAction() {
		Random randomNumbers = new Random();
		action = info.attacks().get(randomNumbers.nextInt(2));
		return action;
	}
	
	/**
	 * Apply the action the monster will do, and choose randomly the next action of the monster
	 */
	@Override
	public void action(GameData data) {
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
		return "Poulet malicieux";
	}
}
