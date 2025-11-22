package com.example.rendering;
import java.util.ArrayList;

import com.example.representations.Board;
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

    /**
     * Rasterizes segments onto a give board
     * @param segment_list the segments to be rasterized (floating point to 2d)
     * @param board the board to be written on
     */
    public void rasterizeSegments(ArrayList<DirectedSegment> segment_list, Board board)
    {
        for (DirectedSegment segment : segment_list)
        {
            segmentRasterizerStrategy.rasterizeSegment(segment, board);
        }
    }


}
