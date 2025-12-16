package com.example.rendering;
import java.util.ArrayList;

import com.example.representations.Board;
/**
 * Context object as part of the strategy pattern
 * Allows implementing multiple rasterization strategies
 */
public class RasterizerContext 
{
    private RasterizerStrategy rasterizer_strategy;

    /**
     * Construct a rasterizer context with initial strategy
     * @param rasterizer_strategy the strategy used for rasterization
     */
    public RasterizerContext(RasterizerStrategy rasterizer_strategy)
    {
        this.rasterizer_strategy = rasterizer_strategy;
    }

    /**
     * Used to change the rasterizer strategy used
     * @param rasterizer_strategy the new strategy
     */
    public void setRasterizerStrategy(RasterizerStrategy rasterizer_strategy) 
    {
        this.rasterizer_strategy = rasterizer_strategy;
    }

    /**
     * Rasterizes segments onto a give board
     * @param shape_list the segments to be rasterized (floating point to 2d)
     * @param board the board to be written on
     */
    public void rasterizeShapes(ArrayList<RenderableShape> shape_list, Board board)
    {
        for (RenderableShape renderable : shape_list)
        {
            RasterizationContext context = new RasterizationContext(board, renderable.provider());
            renderable.shape().acceptVisitor(rasterizer_strategy, context);
        }
    }
}
