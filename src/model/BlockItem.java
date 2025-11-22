package model;


/**
 * Class for item in backpack.
 * items are a list of block where each block has their own coordinate.
 */
public class BlockItem {
	/**
	 * List of Object Block
	 */
	private Block b[];
	/**
	 * Angle use for the rotation of an item
	 */
	private double angle = Math.toRadians(90);
	private Direction direction = Direction.UP; 
	
	/**
	 * Method that initialize the list of Block for an item.
	 * The number of Block is the number of tile the item will take.
	 * 
	 * @param item_size size of the item;
	 * @throw IllegalArgumentException if item_size if below 1 
	 */
	public void create(int item_size) {
		if (item_size < 1) {
			throw new IllegalArgumentException("size invalid");
		}
		b = new Block[item_size];
		for (int i = 0; i < item_size; i++) {
			b[i] = new Block(-1, -1);
		}
	}
	
	/**
	 * Clone the position in the bag of an item.
	 * This method is use for moving an object.
	 */
	public Block[] clone() {
		Block[] b_clone = new Block[b.length];
		for (int i = 0; i < b.length; i++) {
		    b_clone[i] = new Block(b[i].x(), b[i].y());
		}
		return b_clone;
	}
	
	/**
	 * Rotate the item clockwise by using trigonometry. 
	 * The formula is the same whatever shape it is, this is why it's here.
	 * 
	 * @param bag The grid of the bag, we need to check if the new coordinate after rotation can fit in the bag
	 */
	public void rotateXY(Backpack bag) {
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
		direction = direction.next();
		b = b_rotated;
	}
	
	/**
	 * Return the size of an item.
	 * 
	 * @return length of the item
	 */
	public int length() {
		/* Return the size of an item.
		 * 
		 */
		return b.length;
	}
	
	/**
	 * Return the shape of the item
	 * 
	 * @return list of Block where each Block is the coordinate (x, y) of one tile.	
	 */
	public Block[] shape() {
		return b;
	}
	
	public Direction direction() {
		return direction;
	}
}

