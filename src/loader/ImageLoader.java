package loader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/*
 * This class is responsible for loading all images used in the game.
 * Images are loaded from predefined folders using the java.nio
 * Two maps are used:
 * 
 * - bgImages: background and miscellaneous images indexed by file name
 * - itemImagesByID: item images indexed by their unique identifier
 */
public record ImageLoader(Map<String, BufferedImage> bgImages, Map<Integer, BufferedImage> itemImagesByID) {
	/*
	 * Creates an ImageLoader and loads all images from predefined folders
	 */
	public ImageLoader() {
		this(new HashMap<>(), new HashMap<>());
		addFolder(Path.of("data/BG"));
		addFolder(Path.of("data/monster"));
		addFolder(Path.of("data/icon"));
		addFolder(Path.of("data/Hero"));
		addFolder(Path.of("data/item/other"));
		addFolder(Path.of("data/item/weapon"));
	}
	
	/**
	 * Loads an image from the given file path.
	 *
	 * @param path path to the image file
	 * 
	 * @return the loaded image as a BufferedImage
	 * @throws IllegalStateException if the image cannot be loaded or is invalid
	 */
	private BufferedImage loadImg(Path path) {
		try (var input = Files.newInputStream(path)) {
			BufferedImage img = ImageIO.read(input);
			if (img == null) {
				throw new IOException("Invalid image file: " + path);
			}
			return img;
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load image: " + path, e);
		}
	}
	
	/**
	 * Loads all image files from the given directory and stores them
	 * in the appropriate map depending on their type.
	 *
	 * @param folderPath path of the directory containing image files
	 * 
	 * @throws IllegalArgumentException if the given path is not a directory
	 * @throws IllegalStateException if the directory cannot be read
	 */
	private void addFolder(Path folderPath) {
		if (!Files.isDirectory(folderPath)) {
			throw new IllegalArgumentException("Not a directory: " + folderPath);
		}
		try (var paths = Files.list(folderPath)) {
			paths
				.filter(Files::isRegularFile)
				.filter(this::isImageFile)
				.forEach(path -> {
					String fileName = path.getFileName().toString();
					String key = fileName.substring(0, fileName.lastIndexOf('.'));
					if (folderPath.endsWith("data/item/weapon")) {
						itemImagesByID.put(getItem(key), loadImg(path));
					} else {
						bgImages.put(key, loadImg(path));
					}
				});
		} catch (IOException e) {
			throw new IllegalStateException("Error reading directory: " + folderPath, e);
		}
	}
	
	/**
	 * Checks whether the given path corresponds to a supported image file.
	 *
	 * @param path path to check
	 * @return true if the file has a supported image extension, false otherwise
	 */
	private boolean isImageFile(Path path) {
		String name = path.getFileName().toString().toLowerCase();
		return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
	}
	
	/**
	 * Converts an item name into its corresponding item ID.
	 *
	 * @param name name of the item
	 * @return the unique ID associated with the item
	 * @throws IllegalArgumentException if the item name is unknown
	 */
	private int getItem(String name) {
		return switch (name) {
			case "keyDoor" -> 1;
			case "gold" -> 2;
			case "sword" -> 3;
			case "despairShield" -> 4;
			case "mimicry" -> 5;
			case "massue" -> 6;
			case "gant" -> 7;
			case "axe" -> 8;
			case "arrow" -> 9;
			case "bow" -> 10;
			case "poisonArrow" -> 11;
			case "bomb" -> 12;
			case "curse" -> 13;
			case "shield" -> 14;
			case "fireBall" -> 15;
			case "manaStone" -> 16;
			case "enchantedDiamondSword" -> 17;
			case "cookie" -> 18;
			default -> throw new IllegalArgumentException("Unexpected item name: " + name);
		};
	}
}
