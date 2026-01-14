package com.example.rendering.polygon_decomposer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import com.example.representations.Coord;
import com.example.representations.Vector2d;
import com.example.utils.IterationUtils;
import com.example.utils.VectorMath;
import com.example.utils.WindingOrder;
public final class PolygonHelper
{
    // threshold for removing colinear vertices
    private static final double EPSILON_THRESHOLD = 1e-4;
    // private constructor to stop instantiation
    private PolygonHelper() {}

    /**
     * Checkes if polygon meets simple polygon requirements
     * @param vertices vertices of the polygon
     * @return true if it is simple, false if not
     */
    public static boolean isSimplePolygon(Coord[] vertices)
    {
        // potentially implement bentley ottman in the future
        // for now just a lazy loop through
        int n = vertices.length;
        HashSet<Integer> neighbours = new HashSet<>(Arrays.asList(0, 1, n-1));

        for (int i = 0; i < n; ++i)
        {
            Coord a = vertices[i];
            Coord b = IterationUtils.getItem(vertices, i + 1);

            for (int j = 0; i < n; ++i)
            {
                Coord c = vertices[j];
                Coord d = IterationUtils.getItem(vertices, i + 1);

                // Skip neighboring edges that share endpoints
                if (neighbours.contains(Math.abs(i - j)))
                {
                    continue;
                }

                if (PolygonHelper.isIntersectingSegment(a,b,c,d))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 2d cross product using p_to_q with r
     * A positive result means r is counterclockwise
     * A negative result means r is clockwise
     * A zero means r is collinear with ->q
     * @param p coordinate line start
     * @param q coordinate line end
     * @param r vector
     * @return the cross product if p_to_q and r
     */
    public static double orient(Coord p, Coord q, Coord r)
    {
        return (q.x() - p.x())*(r.y() - p.y()) - (q.y() - p.y())*(r.x() - p.x());
    }

    /**
     * Checks whether a point is on the segment
     * Assumes the inputted point is already colinear with p->q
     * @param p start of line coord
     * @param q end of line coord
     * @param r 
     * @return
     */
    public static boolean isOnSegment(Coord p, Coord q, Coord r)
    {
        return (
            Math.min(p.x(), r.x()) <= q.x() &&
            Math.max(p.x(), r.x()) >= q.x() &&
            Math.min(p.y(), r.y()) <= q.y() &&
            Math.max(p.y(), r.y()) >= q.y()
        );
    }

    /**
     * Checks whether two segments are intersecting
     * @param a start of 1st line
     * @param b end of 1st line
     * @param c start of 2nd line
     * @param d end of 2nd line
     * @return whether line 1 and 2 are intersecting
     */
    public static boolean isIntersectingSegment(Coord a, Coord b, Coord c, Coord d)
    {
        // https://www.youtube.com/watch?v=bbTqI0oqL5U
        double o1 = PolygonHelper.orient(a, b, c);
        double o2 = PolygonHelper.orient(a, b, d);
        double o3 = PolygonHelper.orient(c, d, a);
        double o4 = PolygonHelper.orient(c, d, b);

        // effectively an xor gate for signs with (a, b) and (c, d)
        // if c and d are pos/neg for a->b and a, b are pos/neg for c-> d
        // then they must be intersecting
        if (o1*o2 < 0 && o3 * o4 < 0)
        {
            return true;
        }

        // handles special cases where a point is lying on the line
        if (o1 == 0 && PolygonHelper.isOnSegment(a, c, b)) return true;
        if (o2 == 0 && PolygonHelper.isOnSegment(a, d, b)) return true;
        if (o3 == 0 && PolygonHelper.isOnSegment(c, a, d)) return true;
        return o4 == 0 && PolygonHelper.isOnSegment(c, b, d);
    }

    /**
     * Returns an array of colinear indices
     * @param vertices vertices of polygon
     * @return array of indices if that corresponding vertice is colinear
     */
    public static ArrayList<Integer> findNonColinearVertexIndices(Coord[] vertices)
    {
        ArrayList<Integer> non_colinear_indices = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++)
        {
            Coord a = vertices[i];
            Coord b = IterationUtils.getItem(vertices, i-1);
            Coord c = IterationUtils.getItem(vertices, i+1);

            Vector2d a_to_b = new Vector2d(a, b);
            Vector2d a_to_c = new Vector2d(a, c);

            double cross = VectorMath.crossProduct(a_to_b, a_to_c);

            // if the area is larger than the req threshold its fine and can be added
            if (Math.abs(cross) > EPSILON_THRESHOLD)
            {
                non_colinear_indices.add(i);
            }
        }
        return non_colinear_indices;
    }

    /**
     * Finds the winding ording of the vertices
     * @param vertices vertices of polygon
     * @return WindingOrder of the polygon
     */
    public static WindingOrder findWindingOrder(ArrayList<Integer> indices, Coord[] vertices)
    {
        double area = 0;
        for (int i = 0; i < indices.size(); i++)
        {
            Coord current = vertices[indices.get(i)];

            Coord next = vertices[IterationUtils.getItem(indices, i+1)];
            area += VectorMath.crossProductOfCoordinates(current, next);
        }

        return area > 0 ? WindingOrder.COUNTERCLOCKWISE : WindingOrder.CLOCKWISE;
    }

    public static boolean isPointInTriangle(Coord a, Coord b, Coord c, Coord p)
    {
        final Vector2d va_to_vb = new Vector2d(a, b);
        final Vector2d vb_to_vc = new Vector2d(b, c);
        final Vector2d vc_to_va = new Vector2d(c, a);

        final Vector2d va_to_vp = new Vector2d(a, p);
        final Vector2d vb_to_vp = new Vector2d(b, p);
        final Vector2d vc_to_vp = new Vector2d(c, p);

        // returns false if any are > 0, if they're > 0 its not in the triangle
        return !(VectorMath.crossProduct(va_to_vb, va_to_vp) > 0
                || VectorMath.crossProduct(vb_to_vc, vb_to_vp) > 0
                || VectorMath.crossProduct(vc_to_va, vc_to_vp) > 0);
    }
}