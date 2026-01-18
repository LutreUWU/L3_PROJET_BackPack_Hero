package model;

public enum Direction {
	UP, RIGHT, DOWN, LEFT;

	public Direction next() {
		return switch (this) {
		case UP -> RIGHT;
		case RIGHT -> DOWN;
		case DOWN -> LEFT;
		case LEFT -> UP;
		};
	}
}
