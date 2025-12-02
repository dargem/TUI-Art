package com.example.rendering;

import java.util.ArrayList;

import com.example.representations.shapes.Shape;
import com.example.utils.Bound;

public interface ShapeBasedRenderable 
{
    /**
     * Grows line based renderables, restricting them to the bound
     * Returns these newly created lines for rendering
     * @param bound A Bound object used for checking if points are within it
     * @return ArrayList of newly made DirectedSegment that conform to these input parameters
     */
    public abstract ArrayList<Shape> growAndFetchRenderable(Bound bound);

    /**
     * Remove segments that are below a threshold to save memory
     * @param bound A bound object used for checking if points are outside it
     */
    public abstract void trimSegments(Bound bound);
}