package com.example.rendering;

import com.example.representations.Board;
import com.example.representations.Coord;
import com.example.representations.DirectedSegment;
import com.example.representations.Tile;
import com.example.representations.CoordPair;

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

        // magnitude of the shift, considering 0 degrees is upwards
        double x_shift = hypotenuse_shant * Math.sin(segment.getAngle() + (Math.PI / 2));
        double y_shift = hypotenuse_shant * Math.cos(segment.getAngle() + (Math.PI / 2));
        
        // left / right of start
        Coord A = new Coord(start_point.x() + x_shift, start_point.y() + y_shift);
        Coord B = new Coord(start_point.x() - x_shift, start_point.y() - y_shift);
        // left / right of the end
        Coord C = new Coord(end_point.x() + x_shift, end_point.y() + y_shift);
        Coord D = new Coord(end_point.x() - x_shift, end_point.y() - y_shift);

        CoordPair[] coord_pairs = new CoordPair[2];
        //coord_pairs[0] = new CoordPair(A, B);
        //coord_pairs[1] = new CoordPair(A, C);
        //coord_pairs[2] = new CoordPair(B, D);
        //coord_pairs[3] = new CoordPair(C, D);
        coord_pairs[0] = new CoordPair(A, C);
        coord_pairs[1] = new CoordPair(B, D);
        
        for (CoordPair coord_pair : coord_pairs)
        {
            //System.out.println("rasterising line");
            final double x0 = coord_pair.start().x();
            final double x1 = coord_pair.end().x();
            final double y0 = coord_pair.start().y();
            final double y1 = coord_pair.end().y();

            double dx = Math.abs(x1-x0);
            double dy = Math.abs(y1-y0);
            int x = (int) Math.floor(x0);
            int y = (int) Math.floor(y0);

            int n = 1;
            int x_inc, y_inc;
            double error;

            if (dx == 0)
            {
                x_inc = 0;
                error = Double.POSITIVE_INFINITY;
            }
            else if (x1 > x0)
            {
                x_inc = 1;
                n += (int) Math.floor(x1) - x;
                error = (Math.floor(x0) + 1 - x0) * dy;
            }
            else
            {
                x_inc = -1;
                n += x - (int) Math.floor(x1);
                error = (x0 - Math.floor(x0)) * dy;
            }

            if (dy == 0)
            {
                y_inc = 0;
                error = Double.NEGATIVE_INFINITY;
            }
            else if (y1 > y0)
            {
                y_inc = 1;
                n += (int) Math.floor(y1) - y;
                error -= (Math.floor(y0) + 1 - y0) * dx;
            }
            else
            {
                y_inc = -1;
                n += y - (int) Math.floor(y1);
                error -= (y0 - Math.floor(y0)) * dx;
            }
            for (; n>0; --n)
            {
                if (x <= 0 || y<= 0)
                {
                    continue;
                }
                //System.out.println(x + " " + y);
                board.addTile(x, y, segment.getTile());
                //System.out.println("added tile");
                if (error > 0)
                {
                    y += y_inc;
                    error -= dx;
                }
                else
                {
                    x += x_inc;
                    error += dy;
                }
            }       
            
            //System.out.println("finished rasterising line");
        }
    }
}
