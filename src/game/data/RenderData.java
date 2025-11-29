package game.data;

import java.awt.geom.AffineTransform;

import model.BoundingBox;

public record RenderData(AffineTransform transform, BoundingBox box) {}

