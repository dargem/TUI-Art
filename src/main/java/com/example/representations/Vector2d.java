package com.example.representations;

/**
 * A record used to hold a 2 dimensional vector
 * @param x the x value of vector
 * @param y the y value of the vector
 */ 
public record Vector2d(double x, double y) 
{
    /**
     * Constructs a vector from 2 points a and b
     * The vector produced is a -> b
     * @param a the start coord
     * @param b the end coord
     */
    public Vector2d(Coord a, Coord b)
    {
        this(b.x() - a.x(), b.y() - a.y());
    }
}
