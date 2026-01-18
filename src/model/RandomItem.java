package model;

import java.util.Random;

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