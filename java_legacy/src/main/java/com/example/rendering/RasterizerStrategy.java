package com.example.rendering;

import com.example.representations.shapes.ShapeVisitor;

/**
 * To be implemnted by concrete drawing strategies
 * Inherits abstract methods from ShapeVisitor
 * Used to create different rasterisation strategies for drawing shapes
 * Void return type, Board context type
 */
public interface RasterizerStrategy extends ShapeVisitor<RasterizationContext, Void>
{
    // for now a placeholder to decouple a rasterizer strategy more
    // gives the freedom to add more things to the rasterizer strategy contract later
}
