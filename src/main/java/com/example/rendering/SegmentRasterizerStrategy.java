package com.example.rendering;
import com.example.representations.Board;
import com.example.representations.shapes.Beam;

public interface SegmentRasterizerStrategy 
{
    /**
     * Rasterizes an array list of segments into coordinates
     * @param segment_list arraylist of directed segments'
     * @param board The board to be written to
     */
    public abstract void rasterizeSegment(Beam segment_list, Board board);
}
