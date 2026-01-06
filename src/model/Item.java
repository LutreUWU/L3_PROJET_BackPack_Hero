package model;

import java.util.List;

import game.GameData;
import model.item.ItemStats;
import model.monster.Enemy;

public interface Item {
		XY[] shape();
		Direction direction();
		ItemStats info();
		int durability();
    boolean canMerge();
    Item addDurability(int nb);
    Item subDurability(int nb);
    Item setXY(XY coord);
    Item use(Enemy enemy, List<Enemy> lstenemy, GameData data);
    Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data);
    Item rotateXY();
    boolean isConductive();
}
