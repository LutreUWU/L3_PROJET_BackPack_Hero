package loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import model.Item;

public class ImageLoader {	
	private final static Map<String, BufferedImage> bgImages = new HashMap<>();
	private final static Map<Integer, BufferedImage> itemImagesByID = new HashMap<>();
	
	private static BufferedImage loadImg(File name) {
    try {
			BufferedImage img = ImageIO.read(name);
			return img;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    return null;
	}
	
	private static void addFolder(String pathFolder) {
		File folder = new File(pathFolder);
		File[] files = folder.listFiles((dir, name) -> {
		    String lower = name.toLowerCase();
		    return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
		});
		if (files != null) {
		    for (File f : files) {
		        String fileName = f.getName();
		        String key = fileName.substring(0, fileName.lastIndexOf('.'));
		        if (pathFolder.equals("data/item/weapon")) {
		        	itemImagesByID.put(getItem(key), loadImg(f));
		        }
		        else {
		        	bgImages.put(key, loadImg(f));
		        }
		    }
		}
	}
	
	private static int getItem(String name) {
		return switch (name) {
		case "keyDoor" -> 1;
		// case gold -> 2
	  case "sword" -> 3;
	  case "despairShield" -> 4;
	  case "mimicry" -> 5;
	  case "massue" -> 6;
	  case "gant" -> 7;
	  case "axe" -> 8;
		case "arrow" -> 9;
	  case "bow"   -> 10;
	  case "poisonArrow" -> 11;
	  case "bomb" -> 12;
		default -> throw new IllegalArgumentException("Unexpected value: " + name);
		};
	}
	
	
	public static Map<String, BufferedImage> loadAllImage() {
		addFolder("data/BG");
		addFolder("data/monster");
		addFolder("data/icon");
		addFolder("data/Hero");
		addFolder("data/item/other");
		return bgImages;
	}
	
	public static Map<Integer, BufferedImage> loadAllImageByID() {
		addFolder("data/item/weapon");
		return itemImagesByID;
	}
}