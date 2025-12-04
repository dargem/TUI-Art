package com.example.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.example.representations.Coord;
import com.example.representations.Vector2d;
public final class PolygonHelper
{
    // threshold for removing colinear vertices
    private static final double EPSILON_THRESHOLD = 1e-3;
    // private constructor to stop instantiation
    private PolygonHelper() {}
    /**
     * Decomposes a simple polygon into triangles
     * Returns an array, of an array of 3 Coord objects
     * Uses an ear clipping implementation
     * https://www.youtube.com/watch?v=hTJFcHutls8&list=PLSlpr6o9vURx4vjomFuwrFhvhV1nhJ_Jc&index=1
     * @param vertices vertices of the polygon (in order)
     * @return An array of array of coords, in form Coord[n][3];
     */
    public static Coord[][] triangleDecompose(Coord[] vertices)
    {

        // validate inputs
        if (vertices == null)
        {
            return new Coord[0][0];
        }

        if (!isSimplePolygon(vertices))
        {
            throw new RuntimeException("Polygon is not simple, vertices are: " + Arrays.toString(vertices));
        }

        switch(PolygonHelper.findWindingOrder(vertices))
        {
            case CLOCKWISE -> {}
            case COUNTERCLOCKWISE -> Collections.reverse(Arrays.asList(vertices));
            case INVALID -> throw new RuntimeException("Polygon has invalid winding order");
        }

        // don't want to select from colinear vertices or ear clipping doesn't help
        ArrayList<Integer> index_list = PolygonHelper.findNonColinearVertexIndices(vertices);
        
        // make sure its >= 3 so traingles can be formed
        if (index_list.size() < 3)
        {
            return new Coord[0][0];
        }

        // there are (n.edges - 2) triangles in a polygon
        final int num_triangles = index_list.size() - 2;
        int triangle_count = 0;
        Coord[][] triangles = new Coord[num_triangles][3];

        while (index_list.size() > 3)
        {
            for (int i = 0; i < index_list.size(); i++)
            {
                // getItem manages wrap around
                final int a = index_list.get(i);
                final int b = ArrayUtils.getItem(index_list, i - 1);
                final int c = ArrayUtils.getItem(index_list, i + 1);

                final Coord coord_a = vertices[a];
                final Coord coord_b = vertices[b];
                final Coord coord_c = vertices[c];

                final Vector2d va_to_vb = new Vector2d(coord_a, coord_b);
                final Vector2d va_to_vc = new Vector2d(coord_a, coord_c);

                // if the cross product is negative, it is reflexive
                // > PI/2 So a triangle can't be made here
                if (VectorMath.crossProduct(va_to_vc, va_to_vb) < 0)
                {
                    continue;
                }

                // so its convex vertex, therefore a triangle can be made
                // Assuming the proposed one isn't containing a vertex
                boolean is_ear = true;
                for (int j = 0; j < index_list.size(); ++j)
                {
                    final int indice = index_list.get(j);
                    if (indice == a || indice == b || indice == c)
                    {
                        continue;
                    }
                    
                    if (PolygonHelper.isPointInTriangle(coord_a, coord_b, coord_c, vertices[indice]))
                    {
                        is_ear = false;
                        break;
                    }

                }

                if (is_ear)
                {
                    // this is an ear so a triangle can be made
                    triangles[triangle_count++] = new Coord[]
                    {
                        vertices[b],
                        vertices[a],
                        vertices[c]
                    };

                    index_list.remove(i);
                    break;
                }
            }
        }

        // now add the final triangle
        triangles[triangle_count] = new Coord[]
        {
            vertices[index_list.get(0)],
            vertices[index_list.get(1)],
            vertices[index_list.get(2)]
        };

        return triangles;
    }

    /**
     * Checkes if polygon meets simple polygon requirements
     * @param vertices vertices of the polygon
     * @return true if it is simple, false if not
     */
    private static boolean isSimplePolygon(Coord[] vertices)
    {
        throw new UnsupportedOperationException("isSimplePolygon not implemented");
    }

    /**
     * Returns an array of colinear indices
     * @param vertices vertices of polygon
     * @return array of indices if that corresponding vertice is colinear
     */
    private static ArrayList<Integer> findNonColinearVertexIndices(Coord[] vertices)
    {
        ArrayList<Integer> non_colinear_indices = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++)
        {
            Coord a = vertices[i];
            Coord b = ArrayUtils.getItem(vertices, i-1);
            Coord c = ArrayUtils.getItem(vertices, i+1);

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
    private static WindingOrder findWindingOrder(Coord[] vertices)
    {
        
        throw new UnsupportedOperationException("not implemented");
    }

    private static boolean isPointInTriangle(Coord a, Coord b, Coord c, Coord p)
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