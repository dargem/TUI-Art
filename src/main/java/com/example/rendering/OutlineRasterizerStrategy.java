package com.example.rendering;

import com.example.representations.Coord;
import com.example.game_board.Board;
import com.example.representations.DirectedSegment;

public class OutlineRasterizerStrategy implements SegmentRasterizerStrategy{
    // basically just outsources to basic rasterizer strategy with each side line

    @Override
    public void rasterizeSegment(DirectedSegment segment, Board board)
    {
        // split a segment into 4 lines
        Coord start_point = segment.getStartLocation();
        Coord end_point = segment.getEndLocation();


        // the magnitude offset wanted
        double hypotenuse_shant = segment.getWidth() / 2;

        // magnitude of the shift
        double x_shift = hypotenuse_shant * Math.cos(segment.getAngle() + (Math.PI / 2));
        double y_shift = hypotenuse_shant * Math.sin(segment.getAngle() + (Math.PI / 2));
        
        // left / right of start
        Coord A = new Coord(start_point.x() + x_shift, start_point.y() + y_shift);
        Coord B = new Coord(start_point.x() - x_shift, start_point.y() - y_shift);
        // left / right of the end
        Coord C = new Coord(end_point.x() + x_shift, end_point.y() + y_shift);
        Coord D = new Coord(end_point.x() - x_shift, end_point.y() - y_shift);

    }
}
