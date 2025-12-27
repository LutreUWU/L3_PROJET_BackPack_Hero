package model.monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.data.GameDataClick;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Curse;
import model.Effect;

/**
 * Class of a Chicken
 */
public class Chicken implements Enemy{
	/**
	 * - HP :				 	When it reach 0, the enemy die
	 * - Shield : 		Can mitigate damage received
	 * - xp :					XP he drops when he die
	 * - lstAttack : List of all attack the enemy has 
	 * - action : 		To register which action the enemy will do next turn
	 */
	private int maxHP = 20;
	private int HP = 20;
	private int shield = 0;
	private final int xp = 4;
	private final List<String> lstAttack = List.of("Morsure", "Protection");
	private final Map<Effect, Integer> effects = new HashMap<>();
	private String action;
	// For graphism
	private final String img = "chicken";
	private final double sizeX = 0.8;
	private final double sizeY = 0.4;
	
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
		for (var effect : effects.keySet()) {
			var value = effects.get(effect);
			if (value <= 1) {
				effects.remove(effect);
			} else {
				effects.put(effect, value - 1);
			}
		}
	}
	
	/**
	 * Chose randomly an action between all attacks the enemy has.
	 * 
	 * @return Action he'll do 
	 */
	@Override
	public String preAction() {
		Random randomNumbers = new Random();
		action = lstAttack.get(randomNumbers.nextInt(2));
		return action;
	}
	
	/**
	 * Apply the action the monster will do, and choose randomly the next action of the monster
	 */
	@Override
	public void action() {
		switch(action) {
			case "Morsure" -> {
				GameDataHero.sub("HP", 3);
				GameDataCombat.addLog("Le poulet malicieux mord l'ennemi (-3PV)");
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
	public int getXP() {
		return xp;
	}
	
	@Override
	public String getImg() {
		return img;
	}
	
	@Override
	public double getSizeX() {
		return sizeX;
	}
	
	@Override
	public double getSizeY() {
		return sizeY;
	}
	
	@Override
	public int getMaxHP() {
		return maxHP;
	}
	
	@Override
	public String toString() {
		return "Poulet malicieux";
	}
}
