package model;

import java.util.Objects;

/**
 * Represents an axis-aligned bounding box defined by two corner points.
 * The box is described using its north-west (top-left) and south-east
 * (bottom-right) coordinates.
 *
 * This structure is commonly used for layout, collision detection,
 * and spatial calculations.
 *
 * @param northWest the top-left coordinate of the bounding box
 * @param southEast the bottom-right coordinate of the bounding box
 */
public record BoundingBox(XY northWest, XY southEast) {
	public BoundingBox {
		Objects.requireNonNull(northWest);
		Objects.requireNonNull(southEast);
	}
}
