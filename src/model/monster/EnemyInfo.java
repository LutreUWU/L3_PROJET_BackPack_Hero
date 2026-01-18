package model.monster;

import java.util.List;

/**
 * Record containing all fixed informations about an ennemy
 * 
 * @param maxHP		HP Max of the mob
 * @param xp			XP of the mob
 * @param attacks	List of attacks the mob has
 * @param sizeX		SizeX of the mob on the screen
 * @param sizeY	  SizeY of the mob on the screen
 * @param img			Name of the img file
 */
public record EnemyInfo(int maxHP, int xp,  List<String> attacks, double sizeX, double sizeY, String img){
	
	/**
	 * Default constructor
	 * 
   * @throws IllegalArgumentException if value is not positive
	 */
  public EnemyInfo {
    if (maxHP < 0) {
      throw new IllegalArgumentException("maxHP must be > 0");
    }
    if (xp < 0) {
      throw new IllegalArgumentException("xp must be > 0");
    }
    if (attacks == null || attacks.isEmpty()) {
      throw new IllegalArgumentException("attacks cannot be null or empty");
    }
    if (img == null || img.isBlank()) {
      throw new IllegalArgumentException("img cannot be null or blank");
    }
    if (sizeX <= 0 || sizeY <= 0) {
      throw new IllegalArgumentException("size must be > 0");
    }
  }
}
