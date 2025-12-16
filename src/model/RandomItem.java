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
		if (randomIndexWithGauss < 0)
			randomIndexWithGauss = 0;
		if (randomIndexWithGauss >= sizeList - 1)
			randomIndexWithGauss = sizeList - 1;
		return allItem.get(randomIndexWithGauss);
	}
}

/**
return switch(allItem.get(randomIndexWithGauss)) {
case Sword _ -> new Sword();
case Gold _ -> new Gold(1);
case Gant _ -> new Gant();
case Massue _ -> new Massue();
case DespairShield _ -> new DespairShield();
case Axe _ -> new Axe();
case Mimicry _ -> new Mimicry();
default -> throw new IllegalArgumentException("Unexpected value: " + allItem.get(randomIndexWithGauss));
*/