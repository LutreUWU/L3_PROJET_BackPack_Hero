package item;

public class Sword implements Item_Object{
	private Item weapon = new Item();
	private int id = 1;
	public Sword() {
		/* Initialize a sword. 
		 */
		weapon.create(3);
		weapon.shape();
	}
	
	@Override
	public int id() {
		return id;
	}
	
	@Override
	public Block[] shape(){
		return weapon.shape();
	}
	
	@Override
	public void rotateXY(Backpack bag) {
		weapon.rotateXY(bag);
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Name : Sword\n").append("ID : 1\n");
		for (var block : weapon.shape()) {
			builder.append(block).append("\n");
		}
		
		return builder.toString();
	}
	
	public void setXY(int x, int y) {
		/* Initialize the position of the item at the coordinate in parameter.
		 * The center of the item is where the initialization start.
		 * For example if we call setXY(2, 2), the methods will initialize the item at this coordinate :
		 * 
		 *  o # (2, 3)
		 *  o # (2, 2)
		 *  o # (2, 4)
		 *  
		 *  @param x int x coordinate
		 *  @param y int y coordinate
		 */
		weapon.shape()[0] = new Block(x, y);
		weapon.shape()[1] = new Block(weapon.shape()[0].x(), weapon.shape()[0].y() - 1);
		weapon.shape()[2] = new Block(weapon.shape()[0].x(), weapon.shape()[0].y() + 1);
	}

}



