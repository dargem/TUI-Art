package com.example.rendering;

import com.example.representations.Board;
import com.example.representations.shapes.ShapeVisitor;

/**
 * To be implemnted by concrete drawing strategies
 * Inherits abstract methods from ShapeVisitor
 * Used to create different rasterisation strategies for drawing shapes
 */
public interface RasterizerStrategy extends ShapeVisitor<Void>
{
    // Inherits abstract methods from ShapeVisitor

    /**
     * Give a board to the drawing strategy
     * @param board the board for the strategy to draw on
     */
    public abstract void setBoard(Board board);
}
