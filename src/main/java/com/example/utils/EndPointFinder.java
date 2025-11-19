package com.example.utils;

import com.example.representations.Point;

public final class EndPointFinder {
    
    private EndPointFinder() {}

    public static Point findEnd(final Point start_point, final double angle, final double length)
    {
        final double end_x = start_point.x() + length * Math.sin(angle);
        final double end_y = start_point.y() + length * Math.cos(angle);
        return new Point(end_x, end_y);
    }

    public static double findEndX(final double start_x, final double angle, final double length)
    {
        return start_x + length * Math.sin(angle);
    }
}
