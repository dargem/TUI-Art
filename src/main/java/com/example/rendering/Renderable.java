package com.example.rendering;

import java.util.ArrayList;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;

public interface Renderable {
    /**
     * Get an array list of segments that are positioned onscreen for rendering
     * @param min_y Returned segments must be at least this high
     * @param max_y Returned segments must be at most this high
     * @return ArrayList of DirectedSegment that conform to these input parameters
     */
    public ArrayList<DirectedSegment> returnSegments(Bound bound);

    /**
     * Remove segments that are below a threshold to save memory
     * @param min_y Minimum height of a segment for it to continue
     */
    public void trimSegments(double min_y);
}