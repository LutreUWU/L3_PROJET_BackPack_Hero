package model.monster;

import java.util.List;

/**
 * Record containing all fixed informations about an ennemy
 * 
 */
public record EnemyInfo(int maxHP, int xp,  List<String> attacks, double sizeX, double sizeY, String img){
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
