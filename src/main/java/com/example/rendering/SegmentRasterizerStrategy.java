package com.example.rendering;
import com.example.representations.DirectedSegment;

public interface SegmentRasterizerStrategy 
{
    /**
     * Rasterizes an array list of segments into coordinates
     * @param segment_list arraylist of directed segments'
     */
    public abstract void rasterizeSegment(DirectedSegment segment_list);
}
