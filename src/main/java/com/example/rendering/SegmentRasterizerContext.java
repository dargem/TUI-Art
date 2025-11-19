package com.example.rendering;
import java.util.ArrayList;

import com.example.representations.DirectedSegment;
/**
 * Context object as part of the strategy pattern
 * Allows implementing multiple rasterization strategies
 */
public class SegmentRasterizerContext {
    private SegmentRasterizerStrategy segmentRasterizerStrategy;

    public SegmentRasterizerContext(SegmentRasterizerStrategy segmentRasterizerStrategy)
    {
        this.segmentRasterizerStrategy = segmentRasterizerStrategy;
    }

    public void setSegmentRasterizerStrategy(SegmentRasterizerStrategy segmentRasterizerStrategy) 
    {
        this.segmentRasterizerStrategy = segmentRasterizerStrategy;
    }

    public void rasterizeSegments(ArrayList<DirectedSegment> segment_list)
    {
        segmentRasterizerStrategy.rasterizeSegments(segment_list);
    }
}
