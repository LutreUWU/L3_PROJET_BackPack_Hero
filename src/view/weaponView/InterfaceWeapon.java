package view.weaponView;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.ScreenInfo;

import item.Backpack;
import item.Block;
import item.Item_Object;


public class InterfaceWeapon { // #
	private final int GRID_SIZE = 100;
	private ApplicationContext user_context;
	private ScreenInfo user_screenInfo;
	private Backpack user_backpack;
	
	private void drawVirtualWeapon(ApplicationContext context, ScreenInfo screenInfo, Item_Object item) {
		/* Method drawing a virtual Weapon in the grid of the backpack.
		 * The weapon IS NOT is not include in the bag.
		 * It's for helping the user to choose where he wants to place his weapon.
		 * 
		 * @param context	 The windows who wants to draw
		 * @param screenInfo The dimension of the windows
		 * @param item		 Which item we wants to draw
		 */
		if (item == null) {
	        throw new IllegalArgumentException("Virtual item is null");
	    }
		for (var block : item.shape()) {
			switch (item.id()) {
				case 1 ->{
					var drawSword = new SwordView(context, screenInfo, GRID_SIZE, block.y(), block.x());
					drawSword.draw();
				}
				default ->{}
			}
		}
	}
		
	public void weaponGrid(ApplicationContext context, ScreenInfo screenInfo, Backpack bag, Item_Object item) {
		/* Draw a "virtual grid" above the grid of the backpack for moving the item and choosing where we wants to put it.
		 * We draw a virtual grid because thanks to this, we can travel all items in the bag.
		 * 
	 	 * @param context	 The windows who wants to draw
		 * @param screenInfo The dimension of the windows
		 * @param bag		 The actual user bag
		 * @param item		 Which item we wants to draw
		 * 
		 * @throw 			 Error if item is null
		 */
		if (item == null) {
	        throw new IllegalArgumentException("Virtual item is null");
	    }
		user_context = context;
		user_screenInfo = screenInfo;
		user_backpack = bag;
		drawVirtualWeapon(user_context, user_screenInfo, item);
		
	}
	
	private boolean border_backpack(Item_Object item, int addX, int addY) {
		/* Check if the item don't surpass the grid at the new coordinate of the item (y + addY, x + addX)
		 * 
		 * @param item  Item we wants to check
		 * @param addX 	New coordinate x
		 * @param addY 	New coordinate y
		 * @return true if available, else false
		 */
		 var b = item.shape();
		 for (var block : b) {
			 var y = block.y() + addY;
			 var x = block.x() + addX;
			 if (y < 0 || y > 4 || x < 0 || x > 6){
				 return false;
			 }
			 if (user_backpack.grid()[y][x] == -2){
				 return false;
			 }
		 }
		 return true;
	}
	
	public void move_item(Item_Object item, int addX, int addY) {
		/* Move each block of the item by (x + addX, y + addY)
		 * 
		 * @param item  Item we wants to move
		 * @param addX 	How many tiles horizontally ( > 0 : RIGHT // < 0 : LEFT)
		 * @param addY 	How many tiles vertically ( > 0 : BOTTOM // < 0 : TOP)
		 */
		if (item != null) {
			if (border_backpack(item, addX, addY)) {
				var b = item.shape();
				for (int i = 0; i < b.length; i++) {
					b[i] = new Block(b[i].x() + addX, b[i].y() + addY);
				}
		// Refresh the virtual grid since the original grid is refreshing every time we press a button
				weaponGrid(user_context, user_screenInfo, user_backpack, item); 
			} else {
				weaponGrid(user_context, user_screenInfo, user_backpack, item);
			}
	    }
	}
	
	public void rotate_item(Item_Object item) {
		/* Rotate the item 
		 * @param item  Item we wants to rotate
		 */
		if (item != null) {
			item.rotateXY(user_backpack);
			weaponGrid(user_context, user_screenInfo, user_backpack, item);
	    }
	}
} // #
