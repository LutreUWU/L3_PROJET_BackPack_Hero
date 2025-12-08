package model;

import java.util.ArrayList;

import model.monster.Enemy;

public interface Item {
    XY[] shape();
    void setXY(XY coord);
    void use(Enemy enemy, ArrayList<Enemy> lstenemy);
    Direction direction();
    void setDirection(Direction d);
    Rarity getRarity();
    int getScore();
    int getID();
    String getDescription();
    String getEffect();
    
    
    default int getWidth() {
        XY[] b = shape();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        for (XY block : b) {
            int x = block.x();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
        }
        return maxX - minX + 1;
    }

    default int getHeight() {
        XY[] b = shape();
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (XY block : b) {
            int y = block.y();
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
        return maxY - minY + 1;
    }

    default int finalScore() {
    	return (getRarity().ordinal() + 1) * getScore();
    }
    
    
    default void rotateXY() {
        XY[] b = shape();
        if (b == null || b.length == 0) return;
        int cx = b[0].x();
        int cy = b[0].y();
        for (int i = 0; i < b.length; i++) {
            int newX = -b[i].y();
            int newY = b[i].x();
            b[i] = new XY(cx + newX, cy + newY);
        }
        setDirection(direction().next());
    }
    
}
