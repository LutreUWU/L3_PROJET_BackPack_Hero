package model;

import java.util.Random;


/**
 * Utility class responsible for generating random items.
 *
 * This class is typically used to select or create items randomly
 * based on game rules such as rarity, floor level, probability,
 * or predefined item pools.
 *
 * It may be used for loot generation, shop inventory creation,
 * or rewards after combat or events.
 */
public class RandomItem {
	
	/**
	 * Default constructor that does nothing
	 */
	public RandomItem() {}
	
	/**
	 * Generate a random item with Gauss
	 * @param floor Current floor
	 * @return the random item
	 */
	public static Item generate(int floor) {
		var allItem = ItemRepository.getItemrankLst();
		var sizeList = allItem.size();
		Random rand = new Random();
		int randomIndexWithGauss = (int) rand.nextGaussian(floor, 1);
		if (randomIndexWithGauss < 0)
			randomIndexWithGauss = 0;
		if (randomIndexWithGauss >= sizeList - 1)
			randomIndexWithGauss = sizeList - 1;
		return allItem.get(randomIndexWithGauss);
	}
}