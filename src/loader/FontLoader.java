package loader;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.github.forax.zen.ScreenInfo;

public class FontLoader {		
	private static int h1;
	private static int h2;
	private static int h3;
	private static int span;

	private static BufferedImage load_font(String name) {
		try {
	    File fontFile = new File(name);
	    Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
	    // choisir une taille
	    customFont = customFont.deriveFont(14f);
	    // enregistrer la police dans l'environnement graphique
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(customFont);
	    
		} catch (FontFormatException | IOException e) {
		    e.printStackTrace();
		}
    return null;
	}
	
	private static void add_folder(String pathFolder) {
		File folder = new File(pathFolder);
		File[] files = folder.listFiles((dir, name) -> {
		    String lower = name.toLowerCase();
		    return lower.endsWith(".otf") || lower.endsWith(".ttf");
		});
		if (files != null) {
		    for (File f : files) {
		        load_font(f.getAbsolutePath());
		    }
		}
	}
	
	
	public static void load_font(ScreenInfo screenInfo) {
		add_folder("data/font");
		h1 = (int) (screenInfo.width() * 0.025);
		h2 = (int) (screenInfo.width() * 0.02);
		h3 = (int) (screenInfo.width() * 0.013);
		span = (int) (screenInfo.width() * 0.008);
	}
	
	public static int getH1() {
		return h1;
	}
	
	public static int getH2() {
		return h2;
	}
	
	public static int getH3() {
		return h3;
	}
	
	public static int getSpan() {
		return span;
	}
}


