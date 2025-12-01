package com.example.representations.shapes;

import com.example.representations.Coord;

public class Polygon implements Shape
{
    private final Coord[] coords;
    

    public Polygon(Coord[] coords)
    {
        this.coords = coords;
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
     * Creates a new shape that is the translated version of this one
     * @param dx the x translation needed
     * @param dy the y translation needed
     * @return The new translated shape
     */
    @Override
    public Shape translate(double dx, double dy)
    {
        return Polygon()
    }
}
