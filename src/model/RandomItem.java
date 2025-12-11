package model;

import java.util.Random;

public class RandomItem {
	/**
	 * Generate a random item with Gauss
	 * @param current floor
	 * @return the random item
	 */
	public static Item generate(int floor) {
		var allItem = ItemRepository.getItemrankLst();
		var sizeList = allItem.size();
		var sixth = (int) (sizeList / 6);
		Random rand = new Random();
		int randomIndexWithGauss = (int) rand.nextGaussian(sixth * floor * 2, 1);
		IO.println(randomIndexWithGauss);
		if (randomIndexWithGauss < 0)
			randomIndexWithGauss = 0;
		if (randomIndexWithGauss >= sizeList - 1)
			randomIndexWithGauss = sizeList - 1;
		var item = allItem.get(randomIndexWithGauss).copy();
		return allItem.get(randomIndexWithGauss).copy();
	}
}