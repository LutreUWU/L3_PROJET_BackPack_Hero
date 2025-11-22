package model;

public enum Direction {
	UP, RIGHT, DOWN, LEFT;
	
	public Direction next() {
    Direction[] vals = values();
    return vals[(this.ordinal() + 1) % vals.length];
	}
}
