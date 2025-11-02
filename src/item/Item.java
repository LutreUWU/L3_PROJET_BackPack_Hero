package item;

public class Item {
	private Block b[];
	private double angle = Math.toRadians(90); // For rotation

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
	
	public Block[] clone() {
		/* Create a clone of the item.
		 * 
		 */
		Block[] b_clone = new Block[b.length];
		for (int i = 0; i < b.length; i++) {
		    b_clone[i] = new Block(b[i].x(), b[i].y());
		}
		return b_clone;
	}
	
	public void rotateXY(Backpack bag) {
		/* Rotate the item clockwise by using trigonometry.
		 * The formula is the same whatever shape it is, this is why it's here.
		 * 
		 * @param bag	The grid of the bag, we need to check if the new coordinate can fit in the bag
		 */
		int new_x, new_y;
		int cx = b[0].x(), cy = b[0].y();
		Block[] b_rotated = b.clone();
		for (int i = 0; i < b.length; i++) {
			new_x = (int) Math.round(cx + (b[i].x() - cx) * Math.cos(angle) - (b[i].y() - cy) * Math.sin(angle));
			new_y = (int) Math.round(cy + (b[i].x() - cx) * Math.sin(angle) + (b[i].y() - cy) * Math.cos(angle));
			if (new_x < 0 || new_x > 6 || new_y < 0 || new_y > 4 || bag.grid()[new_y][new_x] == -2) { // -2 = Inaccessible
				return;
			}
			b_rotated[i] = new Block(new_x, new_y);
			
		}
		b = b_rotated;
	}
}

