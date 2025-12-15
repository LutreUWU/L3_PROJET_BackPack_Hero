package model;

public enum Direction {
	UP, RIGHT, DOWN, LEFT;
	
	/** Rotation horaire */
  public Direction next() {
      return values()[(this.ordinal() + 1) % values().length];
  }
}
