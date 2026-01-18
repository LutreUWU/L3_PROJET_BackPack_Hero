package model.item.superrare;

import java.util.ArrayList;
import java.util.List;

import game.GameData;
import game.data.GameDataCombat;
import game.data.GameDataHero;
import model.Curse;
import model.Direction;
import model.Effect;
import model.Item;
import model.Rarity;
import model.Synergy;
import model.XY;
import model.item.ItemStats;
import model.monster.Enemy;

public record Bomb(XY[] shape, Direction direction, ItemStats info, int durability) implements Item {
	private static final int DURABILITY = 1;
	private static final Rarity RARITY_VALUE = Rarity.RARE;
	private static final int ID_VALUE = 12;
	private static final int SCORE_VALUE = 25;
	private static final int AP_VALUE = 2;
	private static final int MANA_VALUE = 0;
	private static final ItemStats ITEM_STATS = new ItemStats(RARITY_VALUE, ID_VALUE, SCORE_VALUE, AP_VALUE, MANA_VALUE);
	
	public Bomb() {
		this(initShape(new XY(0, 0), Direction.UP), Direction.UP, ITEM_STATS, DURABILITY);
	}

	public Bomb(XY[] shape, Direction direction, int durability) {
		this(shape, direction, ITEM_STATS, durability);
	}
	
	public Bomb(XY coord, Direction direction, int durability) {
		this(initShape(coord, direction), direction, ITEM_STATS, durability);
	}

	private static XY[] initShape(XY coord, Direction direction) {
		XY[] b = new XY[1];
		b[0] = new XY(coord.x(), coord.y());
		for (int i = 0; i < direction.ordinal(); i++) {
			b = rotate90(b, b[0]);
		}
		return b;
	}

	private static XY[] rotate90(XY[] shape, XY pivot) {
		return shape;
	}
	
	@Override
  public boolean isConductive() {
  	return true;
  }

	@Override
	public Item addDurability(int nb) {
		if (nb <= 0)
			throw new IllegalArgumentException("! Not Negative value !");
		return new Bomb(shape, direction, durability + nb);
	}

	@Override
	public Item subDurability(int nb) {
		if (nb <= 0)
			throw new IllegalArgumentException("! Not Negative value !");
		return new Bomb(shape, direction, durability - nb);
	}

	@Override
	public boolean canMerge() {
		return false;
	}

	@Override
	public Bomb setXY(XY coord) {
		return new Bomb(coord, direction, durability);
	}
	
	@Override
  public Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
  	return new Bomb(shape, direction, durability);
  }

	@Override
	public Item use(Enemy enemy, List<Enemy> lstEnemy, GameData data) {
		Synergy.checkSynergie(data, this); // Check bonus
		var bonus = Synergy.getBonusDmg();
		var dmg = (int) (6 * (1 + data.hero().getBoostDmg() / 100)); 
		GameDataCombat.addLog("EXPLOSION ! Chaque ennemi perd " + dmg + " HP");
		if (bonus != 0) GameDataCombat.addLog("SYNERGIE ! Chaque ennemi perd " + bonus + " PV supplémentaire(s)");
		for (var target : lstEnemy) {
			target.subHP(dmg + bonus);
		}
		IO.println(subDurability(1).durability());
		return subDurability(1);
	}

	@Override
	public Bomb rotateXY() {
		return new Bomb(rotate90(shape(), shape()[0]), direction.next(), durability);
	}

	@Override
	public String toString() {
		return "Bombe";
	}
}
