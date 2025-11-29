package game.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageLoader {	
	private static Map<String, BufferedImage> bgImages = new HashMap<>();
	
	private static BufferedImage load_img(File name) {
    try {
			BufferedImage img = ImageIO.read(name);
			return img;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    return null;
	}
	
	private static void add_folder(String pathFolder) {
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
		        bgImages.put(key, load_img(f));
		    }
		}
	}
	
	
	public static Map<String, BufferedImage> load_image() {
		add_folder("data/BG");
		add_folder("data/monster");
		add_folder("data/weapon");
		bgImages.put("bag", load_img(new File("data/bag.png")));
		bgImages.put("Roland", load_img(new File("data/Roland.png")));
		return bgImages;
	}
}