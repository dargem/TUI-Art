package com.example.rendering;
import java.util.ArrayList;

import com.example.representations.DirectedSegment;

public interface SegmentRasterizerStrategy 
{
    /**
     * Rasterizes an array list of segments into coordinates
     * @param segment_list arraylist of directed segments'
     */
    public abstract void rasterizeSegments(ArrayList<DirectedSegment> segment_list);
}
