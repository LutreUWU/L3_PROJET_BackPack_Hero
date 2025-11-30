package game.data;

import java.awt.geom.AffineTransform;

import model.BoundingBox;

/**
 * Record containings an {@code AffineTransform} of the img and his {@code BoundingBox} in the screen
 * 
 */
public record RenderData(AffineTransform transform, BoundingBox box) {}

