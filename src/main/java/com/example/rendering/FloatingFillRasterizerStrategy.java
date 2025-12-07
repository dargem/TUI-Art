package com.example.rendering;
import java.util.Arrays;

import com.example.rendering.polygon_decomposer.PolygonDecomposerStrategy;
import com.example.representations.Board;
import com.example.representations.Coord;
import com.example.representations.shapes.Beam;
import com.example.representations.shapes.Polygon;
import com.example.utils.IterationUtils;

public class FloatingFillRasterizerStrategy implements RasterizerStrategy
{
    // https://playtechs.blogspot.com/2007/03/raytracing-on-grid.html
    // java implementation of this modified bresenham line algorihtm for floating points
    // then modified using edges of a constructed rectangle
    private PolygonDecomposerStrategy triangle_decomposer;
    private final int DEFAULT_MAX_SHAPE_SIZE = 200;
    private final int[] buf_min_x = new int[DEFAULT_MAX_SHAPE_SIZE];
    private final int[] buf_max_x = new int[DEFAULT_MAX_SHAPE_SIZE];

    public FloatingFillRasterizerStrategy(PolygonDecomposerStrategy triangle_decomposer)
    {
        this.triangle_decomposer = triangle_decomposer;
        Arrays.fill(buf_min_x, Integer.MAX_VALUE);
        Arrays.fill(buf_max_x, Integer.MIN_VALUE);
    }

    public void setPolygonDecomposerStrategy(PolygonDecomposerStrategy triangle_decomposer)
    {
        this.triangle_decomposer = triangle_decomposer;
    }

    @Override
    public Void visitBeam(Beam beam, Board board)
    {
        // split a segment into 4 lines
        Coord start_point = beam.getStartCoord();
        Coord end_point = beam.getEndCoord();

        // the magnitude offset wanted
        double half_width = beam.getWidth() / 2.0;

        // magnitude of the shift, considering 0 degrees is upwards
        final double x_shift = half_width * Math.sin(beam.getAngle() + (Math.PI / 2));
        final double y_shift = half_width * Math.cos(beam.getAngle() + (Math.PI / 2));

        /* 
        Create points, needs to iterate in a cycle around shape

            B
                C

        A
            D
        */
        final double A_y = start_point.y() - y_shift;
        final double B_y = end_point.y() - y_shift;
        final double C_y = end_point.y() + y_shift;
        final double D_y = start_point.y() + y_shift; 

        // create a y re-scalar value, this is needed because its going to iterate through fixed size array
        // and having tons of empty space isn't ideal since y will increase as program runs
        // e.g. y is in thousands shift it down to base at 0, then shift it up when inserting to board

        final int y_displacement = (int) Math.floor(
            Math.min(
                Math.min(A_y, B_y), 
                Math.min(C_y, D_y)
            )
        );

        // for each coord shift its y downwards so its y value is centered around 0
        // filling out the vertices to iterate around
        final Coord[] vertices = {
            new Coord(start_point.x() - x_shift, A_y - y_displacement),
            new Coord(end_point.x() - x_shift, B_y - y_displacement),
            new Coord(end_point.x() + x_shift, C_y - y_displacement),
            new Coord(start_point.x() + x_shift, D_y - y_displacement)
        };


        // Then find the range of y's needed, allows sizing the min x / min y arrays
        final int y_range = -y_displacement + (int) Math.ceil(
            Math.max(
                Math.max(A_y, B_y),
                Math.max(C_y, D_y)
            )
        );

        fillMinMaxArrays(vertices);

        for (int y_indice = 0; y_indice < y_range; y_indice++)
        {
            //System.out.println(y_indice);
            for (int x_indice = buf_min_x[y_indice]; x_indice <= buf_max_x[y_indice]; x_indice++)
            {
                board.addTile(x_indice, y_indice + y_displacement, beam.getTile());
            }
            buf_min_x[y_indice] = Integer.MAX_VALUE;
            buf_max_x[y_indice] = Integer.MIN_VALUE;
        }

        return null;
    }

    @Override
    public Void visitPolygon(Polygon polygon, Board board) 
    {
        int y_displacement = (int) Math.floor(polygon.getMinY());
        int y_max = -y_displacement + (int) Math.floor(polygon.getMaxY());

        // where triangle is an array of 3 long coord array
        Coord[][] triangles = triangle_decomposer.decomposePolygon(polygon.getVertices());
        for (Coord[] triangle : triangles)
        {
            // A triangle will always be made of a 3 length Coord[]
            Coord[] coords = {
                triangle[0].translate(0, -y_displacement),
                triangle[1].translate(0, -y_displacement),
                triangle[2].translate(0, -y_displacement)
            };

            fillMinMaxArrays(coords);
        }

        for (int y_indice = 0; y_indice < y_max; y_indice++)
        {
            //System.out.println(y_indice);
            for (int x_indice = buf_min_x[y_indice]; x_indice <= buf_max_x[y_indice]; x_indice++)
            {
                //board.addTile(x_indice, y_indice + y_displacement, polygon.getTile());
                // need an actual tile getting solution
            }
            buf_min_x[y_indice] = Integer.MAX_VALUE;
            buf_max_x[y_indice] = Integer.MIN_VALUE;
        }

        return null;
    }

    private void fillMinMaxArrays(Coord[] vertices)
    {
        for (int i = 0; i < vertices.length; i++)
        {
            //System.out.println("rasterising line");
            final double x0 = vertices[i].x();
            final double x1 = IterationUtils.getItem(vertices, i + 1).x();
            final double y0 = vertices[i].y();
            final double y1 = IterationUtils.getItem(vertices, i + 1).y();

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
                if (x < 0 || y< 0)
                {
                    continue;
                }
                //System.out.println(x + " " + y);
                if (x < buf_min_x[y]) {
                    buf_min_x[y] = x;
                }

                if (x > buf_max_x[y]) {
                    buf_max_x[y] = x;
                }
                //board.addTile(x, y, segment.getTile());
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
