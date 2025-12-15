package model;

public record XY(int x, int y) {
	@Override
	public String toString(){
		return "(x : " + x + ", y : " + y + ")";
	}
}