package model.monster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Effect;

/**
 * Class of a Chicken
 */
public class Robot implements Enemy{
	/**
	 * - HP :				 	When it reach 0, the enemy die
	 * - Shield : 		Can mitigate damage received
	 * - xp :					XP he drops when he die
	 * - lst_attack : List of all attack the enemy has 
	 * - action : 		To register which action the enemy will do next turn
	 */
	private int HP = 50;
	private int shield = 0;
	private final int xp = 20;
	private final List<String> lst_attack = List.of("Hoo...", "HOHOHOH", "HEHEHEH");
	private String action;
	private final Map<Effect, Integer> effects = new HashMap<>();
	// For graphism
	private final String img = "robot";
	private final double sizeY = 1.2;
	private final double sizeX = 1.2;
	
	
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
		action = lst_attack.get(randomNumbers.nextInt(2));
		return action;
	}
	
	/**
	 * Apply the action the monster will do, and choose randomly the next action of the monster
	 */
	@Override
	public void action() {
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
	public String toString() {
		return "Revuz PRIME";
	}
}
