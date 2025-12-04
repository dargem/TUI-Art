package com.example.utils;

import com.example.representations.Coord;

public final class EndPointFinder 
{
    
    private EndPointFinder() {}

    /**
     * Finds the end coordinate of an inputted line
     * @param start_point start coordinate of line
     * @param angle bearing of line
     * @param length length of the line
     * @return end coordinate
     */
    public static Coord findEnd(final Coord start_point, final double angle, final double length)
    {
        final double end_x = start_point.x() + length * Math.sin(angle);
        final double end_y = start_point.y() + length * Math.cos(angle);
        return new Coord(end_x, end_y);
    }

    /**
     * Finds the end X coordinate of a line
     * @param start_x the initial x starting coordinate
     * @param angle the bearing of the line
     * @param length the length of the line
     * @return end coordinate   
     */
    public static double findEndX(final double start_x, final double angle, final double length)
    {
        return start_x + length * Math.sin(angle);
    }
}
