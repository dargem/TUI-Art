package com.example.representations.shapes;

public class Polygon implements Shape
{
    
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
}
