package com.example.utils;

import com.example.representations.Coord;
import com.example.representations.Vector2d;

/**
 * Utility class used for 2d vector math
 */
public final class VectorMath 
{
    // private constructor to stop instantiation
    private VectorMath() {}

    /**
     * Find the dot product of 2 vectors
     * @param a the first vector
     * @param b the second vector
     * @return the dot product
     */
    public static double dotProduct(Vector2d a, Vector2d b)
    {
        return a.x() * b.x() + a.y() * b.y();
    }

    /**
     * Find the cross product of 2 vectors
     * @param a the first vector
     * @param b the second vector
     * @return the cross product
     */
    public static double crossProduct(Vector2d a, Vector2d b)
    {
        return a.x() * b.y() - a.y() * b.x();
    }

    /**
     * Finds the cross product using coordinates
     * Interprets a coordinate as a vector from the origin
     * @param a the first coordinate
     * @param b the second coordinate
     * @return the cross product
     */
    public static double crossProductOfCoordinates(Coord a, Coord b)
    {
        return a.x() * b.y() - a.y() * b.x();
    }
}
