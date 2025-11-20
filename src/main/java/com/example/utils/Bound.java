package com.example.utils;

import com.example.representations.DirectedSegment;
import com.example.representations.Point;


public class Bound {
    private final double min_x;
    private final double max_x;
    private final double min_y;
    private final double max_y;

    
    public Bound(double min_y, double max_y)
    {
        this.min_x = 0;
        this.max_x = TerminalStatus.getWidth();
        this.min_y = min_y;
        this.max_y = max_y;
    }

    /**
     * Checks whether a segment is out of left/right/bottom bound, fine to be above
     * @param segment inputted segment for checking
     * @return true if the whole segment is out of bounds
     */
    public boolean checkLowerTrimmable(DirectedSegment segment)
    {
        return checkLowerTrimmable(segment.getEndLocation())
            && checkLowerTrimmable(segment.getStartLocation());
    }

    private boolean checkLowerTrimmable(Point point)
    {
        return point.y() < min_y 
            || point.x() < min_x
            || point.x() > max_x;
    }

    /**
     * Checks whether a segment is within bounds
     * @param segment inputted segment for checking
     * @return true if any part is within bounds
     */
    public boolean checkIsInBound(DirectedSegment segment)
    {
        return checkIsInBound(segment.getStartLocation())
            || checkIsInBound(segment.getEndLocation());
    }

    private boolean checkIsInBound(Point point)
    {
        return point.y() > min_y && point.y() < max_y
            && point.x() > min_x && point.x() < max_x;
    }
}
