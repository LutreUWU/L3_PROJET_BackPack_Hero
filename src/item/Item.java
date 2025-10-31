package item;

public class Item {
	private Block b[];
	
	public void create(int item_size) {
		/* Method that initialize the list of Block for an item.
		 * The number of Block is the number of tile the item will take.
		 * 
		 * @param item_size size of the item;
		 */
		b = new Block[item_size];
		for (int i = 0; i < item_size; i++) {
			b[i] = new Block(-1, -1);
		}
	}

	public int length() {
		/* Return the size of an item.
		 * 
		 */
		return b.length;
	}
	
	public Block[] shape() {
		/* Return the shape of an item.		
		 * 							
		 * @return list of Block where each Block is the coordinate (x, y) of one tile.	
		 */
		return b;
	}
	public void setXY(int x, int y) {} ;
	public void updateXY(int direction) {}; // for rotation
}

