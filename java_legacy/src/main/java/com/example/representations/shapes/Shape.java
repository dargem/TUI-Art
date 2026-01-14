package com.example.representations.shapes;

/**
 * Represents a geometric shapein the world
 * A shape is defined by its geometry and is immutable
 */
public interface Shape
{
    /**
     * Accepts a visitor, allowing it to perform an operation on the shape
     * Used as part of the visitor pattern
     * @param <C> The type of the visitor's context object
     * @param <R> The return type of the visitor's operation
     * @param visitor The visitor to accept
     * @param context The context passed to the visitor
     * @return the result of the visitor's operation
     */
    public abstract <C, R> R acceptVisitor(ShapeVisitor<C, R> visitor, C context);

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