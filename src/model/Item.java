package model;

import java.util.ArrayList;

import game.GameData;
import model.monster.Enemy;

public interface Item {
		XY[] shape();
		Rarity rarity();
		Direction direction();
		int durability();
    int score();
    int AP();
    boolean canMerge();
    Item addDurability(int nb);
    Item subDurability(int nb);
    int ID();
    Item setXY(XY coord);
    Item use(Enemy enemy, ArrayList<Enemy> lstenemy, GameData data);
    Item usePassive(Enemy enemy, ArrayList<Enemy> lstenemy, GameData data);
    Item rotateXY();
}
