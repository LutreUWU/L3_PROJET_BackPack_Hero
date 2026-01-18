package model;

import java.util.List;

import game.GameData;
import model.item.ItemStats;
import model.monster.Enemy;

/**
 * Represents an item that can be stored in the hero's backpack and used in combat.
 * 
 * An item has a shape, a direction, durability, and stats. It may have passive or
 * active effects, can be rotated, and may conduct mana.
 */
public interface Item {

  /**
   * Returns the shape of the item as an array of grid coordinates.
   * 
   * @return array of XY coordinates representing the item's blocks
   */
  XY[] shape();

  /**
   * Returns the current orientation of the item.
   * 
   * @return the item's direction
   */
  Direction direction();

  /**
   * Returns the item's statistics, such as rarity, ID, score, AP, and mana.
   * 
   * @return the item's ItemStats object
   */
  ItemStats info();

  /**
   * Returns the current durability of the item.
   * 
   * @return durability value
   */
  int durability();

  /**
   * Checks whether the item can merge with other items in the backpack.
   * 
   * @return true if the item can merge, false otherwise
   */
  boolean canMerge();

  /**
   * Returns a new item instance with increased durability.
   * 
   * @param nb amount to add to durability
   * @return a new item with updated durability
   * @throws IllegalArgumentException if nb is not positive
   */
  Item addDurability(int nb);

  /**
   * Returns a new item instance with decreased durability.
   * 
   * @param nb amount to subtract from durability
   * @return a new item with updated durability
   * @throws IllegalArgumentException if nb is not positive
   */
  Item subDurability(int nb);

  /**
   * Returns a new item instance placed at the specified coordinates.
   * 
   * @param coord new XY coordinates
   * @return a new item with updated position
   */
  Item setXY(XY coord);

  /**
   * Uses the item actively on a target enemy and possibly other enemies.
   * This may reduce durability or trigger effects.
   * 
   * @param enemy the primary target enemy
   * @param lstenemy list of all enemies in the combat
   * @param data the game data context
   * @return the updated item after use
   */
  Item use(Enemy enemy, List<Enemy> lstenemy, GameData data);

  /**
   * Uses the item's passive effect on the hero or enemies.
   * 
   * @param enemy the primary target enemy
   * @param lstEnemy list of all enemies in the combat
   * @param data the game data context
   * @return the updated item after passive use
   */
  Item usePassive(Enemy enemy, List<Enemy> lstEnemy, GameData data);

  /**
   * Returns a new item instance rotated 90 degrees clockwise.
   * 
   * @return the rotated item
   */
  Item rotateXY();

  /**
   * Checks whether the item conducts mana to connected items.
   * 
   * @return true if the item is conductive, false otherwise
   */
  boolean isConductive();
}
