package item;

/**
 * Since every Item has a different shape, ID, setXY,
 * we use an interface.
 * 
 * They all have the same rotate function, but we put here to avoid copy paste in every function.
 */
public interface Item_Object {
	public Block[] shape();
	public int id();
	public void setXY(int x, int y);
	public void rotateXY(Backpack bag);
}
