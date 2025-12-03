package com.example.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.example.representations.Coord;

public final class PolygonHelper
{
    // private constructor to stop instantiation
    private PolygonHelper() {}

    /**
     * Decomposes a simple polygon into triangles
     * Returns an array, of an array of 3 Coord objects
     * @param vertices vertices of the polygon (in order)
     * @return An array of array of coords, in form Coord[n][3];
     */
    public static Coord[][] triangleDecompose(Coord[] vertices)
    {
        // validate inputs
        if (vertices == null || vertices.length < 3)
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

        // there are (n.edges - 2) triangles in a polygon
        final int num_triangles = vertices.length - 2;
        int triangle_count = 0;
        Coord[][] triangles = new Coord[num_triangles][3];

        ArrayList<Integer> index_list = new ArrayList<>();
        for (int i = 0; i < vertices.length; ++i)
        {
            index_list.add((i));
        }

        while (index_list.size() > 3)
        {
            for (int i = 0; i < index_list.size(); i++)
            {
                // getItem manages wrap around
                int a = index_list.get(i);
                int b = ArrayUtils.getItem(index_list, i - 1);
                int c = ArrayUtils.getItem(index_list, i + 1);

                Coord coord_a = vertices[a];
                Coord coord_b = vertices[b];
                Coord coord_c = vertices[c];
            }
        }

        // now add the final triangle
        triangles[triangles.length - 1] = new Coord[]
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
     * Returns true if it contains any colinear edges
     * @param vertices vertices of polygon
     * @return true if it has any colinear, false if it doesn't
     */
    private static boolean containsColinearEdges(Coord[] vertices)
    {
        throw new UnsupportedOperationException("not implemented contains colinear edges");
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
}