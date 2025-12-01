package com.example.representations.shapes;
import java.util.Arrays;

import com.example.representations.Coord;

public class Polygon implements Shape
{
    private final Coord[] vertices;
    private Double min_y = null;
    private Double max_y = null;

    public Polygon(Coord[] coords)
    {
        this.vertices = coords;
    }
    
    /**
     * Accepts a visitor, allowing it to perform an operation on the shape
     * Used as part of visitor pattern
     * @param visitor The visitor to accept
     * @param <T> The return type of the visitor's operation
     * @return The result of the visitor's operation
     */
    @Override
    public <T> T acceptVisitor(ShapeVisitor<T> visitor)
    {
        return visitor.visitPolygon(this);
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
