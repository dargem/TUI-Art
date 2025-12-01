package com.example.representations.shapes;


/**
 * Represents a geometric shapein the world
 * A shape is defined by its geometry and is immutable
 */
public interface Shape
{
    /**
     * Accepts a visitor, allowing it to perform an operation on the shape
     * Used as part of visitor pattern
     * @param visitor The visitor to accept
     * @param <T> The return type of the visitor's operation
     * @return The result of the visitor's operation
     */
    public abstract <T> T acceptVisitor(ShapeVisitor<T> visitor);

    /**
     * Creates a new shape that is the translated version of this one
     * @param dx the x translation needed
     * @param dy the y translation needed
     * @return The new translated shape
     */
    public abstract Shape translate(double dx, double dy);


    public abstract double getMinY();
    public abstract double getMaxY();
}