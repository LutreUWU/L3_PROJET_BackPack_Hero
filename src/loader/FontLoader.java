package loader;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import com.github.forax.zen.ScreenInfo;

/**
 * Utility class for loading custom fonts from the "data/font" directory
 * and calculating standardized font sizes based on the screen width.
 * 
 * Provides static methods to load fonts and retrieve font heights for different
 * heading levels (h1–h4) and spacing.
 */
public class FontLoader {		
	private static int h1;
	private static int h2;
	private static int h3;
	private static int h4;
	private static int span;

	/**
	 * Loads a single font from a file path and registers it with the graphics environment.
	 * 
	 * @param name 		The path to the font file (TTF or OTF)
	 */
	private static void load_font(String name) {
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
	}
	
	/**
	 * Loads all font files (TTF or OTF) from the given folder.
	 * 
	 * @param pathFolder the folder path containing font files
	 */
	private static void add_folder(String pathFolder) {
		File folder = new File(pathFolder);
		File[] files = folder.listFiles((_, name) -> {
		    String lower = name.toLowerCase();
		    return lower.endsWith(".otf") || lower.endsWith(".ttf");
		});
		if (files != null) {
		    for (File f : files) {
		        load_font(f.getAbsolutePath());
		    }
		}
	}
	
	/**
	 * Loads all fonts from the default "data/font" directory and calculates
	 * standard font sizes based on the screen width.
	 * 
	 * @param screenInfo the screen information used to calculate font sizes
	 */
	public static void load_font(ScreenInfo screenInfo) {
		add_folder("data/font");
		h1 = (int) (screenInfo.width() * 0.025);
		h2 = (int) (screenInfo.width() * 0.02);
		h3 = (int) (screenInfo.width() * 0.013);
		h4 = (int) (screenInfo.width() * 0.011);
		span = (int) (screenInfo.width() * 0.008);
	}
	
	/** @return the calculated h1 font size based on screen width */
	public static int getH1() {
		return h1;
	}
	/** @return the calculated h2 font size based on screen width */
	public static int getH2() { 
		return h2; 
	}
	
	/** @return the calculated h3 font size based on screen width */
	public static int getH3() { 
		return h3; 
	}
	
	/** @return the calculated h4 font size based on screen width */
	public static int getH4() { 
		return h4;
	}
	
	/** @return the calculated standard span size based on screen width */
	public static int getSpan() { 
		return span; 
	}
}


