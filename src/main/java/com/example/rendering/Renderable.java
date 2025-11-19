package com.example.rendering;

import java.util.ArrayList;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;

public interface Renderable 
{
    /**
     * Get an array list of segments that are positioned onscreen for rendering
     * @param bound A Bound object used for checking if points are within it
     * @return ArrayList of DirectedSegment that conform to these input parameters
     */
    public abstract ArrayList<DirectedSegment> returnBoundSegments(Bound bound);

    /**
     * Remove segments that are below a threshold to save memory
     * @param bound A bound object used for checking if points are within it
     */
    public abstract void trimSegments(Bound bound);
}