package loader;

import java.awt.geom.AffineTransform;

import model.BoundingBox;

/**
 * Represents rendering information for a graphical element.
 * 
 * This record holds:
 * - an AffineTransform that defines the position, rotation, and scaling
 *   to apply when rendering the element,
 * - a BoundingBox that defines the element's position and size in the scene.
 * 
 * It is typically used to calculate drawing positions and detect interactions
 * in the game's graphical interface.
 * 
 * @param transform the AffineTransform to apply when rendering the element
 * @param box the BoundingBox representing the element's bounds
 */
public record RenderData(AffineTransform transform, BoundingBox box) {}

