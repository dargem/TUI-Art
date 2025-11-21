package com.example.rendering;
import com.example.representations.DirectedSegment;
import com.example.game_board.Board;

public interface SegmentRasterizerStrategy 
{
    /**
     * Rasterizes an array list of segments into coordinates
     * @param segment_list arraylist of directed segments'
     * @param board The board to be written to
     */
    public abstract void rasterizeSegment(DirectedSegment segment_list, Board board);
}
