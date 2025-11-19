package com.example.rendering;

import java.util.ArrayList;

import com.example.representations.DirectedSegment;

public abstract class Renderable {

    /**
     * Get an array list of segments that are positioned onscreen for rendering
     * @param min_y Returned segments must be at least this high
     * @param max_y Returned segments must be at most this high
     * @return ArrayList of DirectedSegment that conform to these input parameters
     */
    public abstract ArrayList<DirectedSegment> returnSegments(double min_y, double max_y);

    /**
     * Remove segments that are below a threshold to save memory
     * @param min_y Minimum height of a segment for it to continue
     */
    public abstract void trimSegments(double min_y);
}