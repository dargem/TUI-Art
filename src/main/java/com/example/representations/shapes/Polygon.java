package com.example.representations.shapes;
import java.util.Arrays;

import com.example.representations.Coord;

/**
 * A polygon represents a simple polygon
 * It can be convex or concave
 * It cannot intersect itself or have holes
 * Consecutive vertices cannot be colinear
 * Single unbroken line going from vertices to vertice
 * In the same order as Coord[] vertices
 */
public class Polygon implements Shape
{
    private final Coord[] vertices;
    private Double min_y = null;
    private Double max_y = null;

    public Polygon(Coord[] coords)
    {
        this.vertices = coords;
    }
    
    public Coord[] getVertices()
    {
        return vertices;
    }

    /**
     * Accepts a visitor allowing it to perform an operation on the Polygon
     * Used as part of the visitor pattern
     * @param <C> the type of the visitor's context object
     * @param <R> the return type of the visitor's operation
     * @param visitor the visitor of the polygon
     * @param context the visitor's context object
     * @return the result of the visitor's operation
     */
    @Override
    public <C, R> R acceptVisitor(ShapeVisitor<C, R> visitor, C context)
    {
        return visitor.visitPolygon(this, context);
    }

    /**
     * Creates a new polygon that is the translated version of this one
     * @param dx the x translation needed
     * @param dy the y translation needed
     * @return The new translated shape
     */
    @Override
    public Polygon translate(double dx, double dy)
    {
        Coord[] new_vertices = Arrays.stream(vertices)
                                .map(d -> d.translate(dx, dy))
                                .toArray(Coord[]::new);
        return new Polygon(new_vertices);
    }

    /**
     * @return the minimum y of the vertices
     */
    @Override
    public double getMinY() 
    {
        if (min_y != null)
        {
            return min_y;
        }

        min_y = vertices[0].y();
        for (Coord vertice : vertices)
        {
            if (vertice.y() < min_y)
            {
                min_y = vertice.y();
            }
        }
        return min_y;
    }

    /**
     * @return the maximum y of the shape
     */
    @Override
    public double getMaxY() 
    {
        if (max_y != null)
        {
            return max_y;
        }

        max_y = vertices[0].y();
        for (Coord vertice : vertices)
        {
            if (vertice.y() > max_y)
            {
                max_y = vertice.y();
            }
        }
        return max_y;
    }
}
