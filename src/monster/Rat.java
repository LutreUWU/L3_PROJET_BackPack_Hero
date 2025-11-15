package monster;

import java.util.List;
import java.util.Random;

public class Rat implements Enemy{
	private int HP = 20;
	private int shield = 0;
	private List<String> lst_attack = List.of("Morsure : -3 PV", "Protection : +3 Shield");
	
	@Override
	public int getHP() {
		return HP;
	}
	
	@Override
	public int getShield() {
		return shield;
	}
	
	@Override
	public String pre_action() {
		Random randomNumbers = new Random();
		return lst_attack.get(randomNumbers.nextInt(2));
	}
	
	@Override
	public String toString() {
		return "Rat des toilettes";
	}
}
