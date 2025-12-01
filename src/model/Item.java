package model;

import model.monster.Enemy;

public interface Item {
    Block[] shape();
    void setXY(XY coord);
    void use(Enemy enemy);
    Direction direction();
    void setDirection(Direction d);
    Rarity getRarity();
    int getScore();
    int getID();
    
    default int getWidth() {
        Block[] b = shape();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        for (Block block : b) {
            int x = block.x();
            if (x < minX) minX = x;
            if (x > maxX) maxX = x;
        }
        return maxX - minX + 1;
    }

    default int getHeight() {
        Block[] b = shape();
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Block block : b) {
            int y = block.y();
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
        return maxY - minY + 1;
    }

    default int final_score() {
    	return getRarity().ordinal() * getScore();
    }
    
    default void rotateXY() {
        Block[] b = shape();
        if (b == null || b.length == 0) return;
        int cx = b[0].x();
        int cy = b[0].y();
        for (int i = 0; i < b.length; i++) {
            int x = b[i].x() - cx;
            int y = b[i].y() - cy;

            int newX = -y;
            int newY = x;
            b[i] = new Block(cx + newX, cy + newY);
        }
        setDirection(direction().next());
    }
}
