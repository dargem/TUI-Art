package com.example.utils;

import com.example.representations.Vector2d;

/**
 * Utility class used for 2d vector math
 */
public class VectorMath 
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
     * Find the dot product of 2 vectors
     * @param a the first vector
     * @param b the second vector
     * @return the dot product
     */
    public static double crossProduct(Vector2d a, Vector2d b)
    {
        return a.x() * b.y() - a.y() * b.x();
    }
}
