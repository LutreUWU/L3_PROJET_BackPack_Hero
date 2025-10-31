package item;

public class Sword implements Item_Object{
	private Item weapon = new Item();
	private int id = 1;
	private Block[] b;

	public Sword() {
		/* Initialize a sword.
		 * 
		 */
		weapon.create(3);
		b = weapon.shape();
	}
	
	@Override
	public int id() {
		/* To access ID item.
		 * 
		 * @return integer ID
		 */
		return id;
	}
	
	@Override
	public Block[] shape(){
		/* To access the shape of the item.
		 * 
		 * @return list of Block where each Block is the coordinate (x, y) of one tile.
		 */
		return b;
	}
	
	@Override
	public String toString() {
		var builder = new StringBuilder();
		builder.append("Name : Sword\n").append("ID : 1\n");
		for (var block : b) {
			builder.append(block).append("\n");
		}
		
		return builder.toString();
	}
	
	@Override
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
		b[0] = new Block(x, y);
		b[1] = new Block(b[0].x(), b[0].y() - 1);
		b[2] = new Block(b[0].x(), b[0].y() + 1);
	}
	

}



