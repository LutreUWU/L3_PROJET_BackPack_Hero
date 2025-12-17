package loader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLoader {	
	private final static Map<String, BufferedImage> bgImages = new HashMap<>();
	
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
		        // Retirer l'extension
		        String key = fileName.substring(0, fileName.lastIndexOf('.'));
		        bgImages.put(key, loadImg(f));
		    }
		}
	}
	
	public static Map<String, BufferedImage> loadAllImage() {
		addFolder("data/BG");
		addFolder("data/monster");
		addFolder("data/item/weapon");
		addFolder("data/item/other");
		addFolder("data/icon");
		addFolder("data/Hero");
		return bgImages;
	}
}