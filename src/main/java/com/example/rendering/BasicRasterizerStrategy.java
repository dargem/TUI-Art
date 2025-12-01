package com.example.rendering;

import com.example.representations.Board;
import com.example.representations.shapes.Beam;

public class BasicRasterizerStrategy implements SegmentRasterizerStrategy   
{

    @Override
    public void rasterizeSegment(Beam segment, Board board)
    {
        //System.out.println("rasterising line");
        final double x0 = segment.getStartCoord().x();
        final double x1 = segment.getEndCoord().x();
        final double y0 = segment.getStartCoord().y();
        final double y1 = segment.getEndCoord().y();

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
