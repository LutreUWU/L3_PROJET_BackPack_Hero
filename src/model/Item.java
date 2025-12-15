package model;

import java.util.ArrayList;

import model.monster.Enemy;

public interface Item {
		XY[] shape();
		Rarity rarity();
		Direction direction();
    int score();
    int ID();
    Item setXY(XY coord);
    void use(Enemy enemy, ArrayList<Enemy> lstenemy);
    Item rotateXY();
}
