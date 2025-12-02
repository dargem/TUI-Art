package com.example.rendering;
import java.util.ArrayList;

import com.example.representations.Board;
import com.example.representations.shapes.Shape;
/**
 * Context object as part of the strategy pattern
 * Allows implementing multiple rasterization strategies
 */
public class RasterizerContext 
{
    private RasterizerStrategy rasterizer_strategy;

    public RasterizerContext(RasterizerStrategy rasterizer_strategy)
    {
        this.rasterizer_strategy = rasterizer_strategy;
    }

    public void setRasterizerStrategy(RasterizerStrategy rasterizer_strategy) 
    {
        this.rasterizer_strategy = rasterizer_strategy;
    }

    /**
     * Rasterizes segments onto a give board
     * @param shape_list the segments to be rasterized (floating point to 2d)
     * @param board the board to be written on
     */
    public void rasterizeShapes(ArrayList<Shape> shape_list, Board board)
    {
        for (Shape shape : shape_list)
        {
            shape.acceptVisitor(rasterizer_strategy);
        }
    }
}
