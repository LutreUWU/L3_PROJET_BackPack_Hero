package item;

public record Block(int x, int y) {
	@Override
	public String toString(){
		return "x : " + x + ", y : " + y;
	}
}
