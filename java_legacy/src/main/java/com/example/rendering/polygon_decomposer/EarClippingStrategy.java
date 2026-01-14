package com.example.rendering.polygon_decomposer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.example.representations.Coord;
import com.example.representations.Vector2d;
import com.example.utils.IterationUtils;
import com.example.utils.VectorMath;

public class EarClippingStrategy implements PolygonDecomposerStrategy
{
    /**
     * Decomposes a simple polygon into triangles
     * Returns an array, of an array of 3 Coord objects
     * Uses an ear clipping implementation
     * https://www.youtube.com/watch?v=hTJFcHutls8&list=PLSlpr6o9vURx4vjomFuwrFhvhV1nhJ_Jc&index=1
     * @param vertices vertices of the polygon (in order)
     * @return An array of array of coords, in form Coord[n][3];
     */
    @Override
    public Coord[][] decomposePolygon(Coord[] vertices)
    {

        // validate inputs
        if (vertices == null)
        {
            return new Coord[0][0];
        }

        if (!PolygonHelper.isSimplePolygon(vertices))
        {
            throw new RuntimeException("Polygon is not simple, vertices are: " + Arrays.toString(vertices));
        }


        // don't want to select from colinear vertices or ear clipping doesn't help
        ArrayList<Integer> index_list = PolygonHelper.findNonColinearVertexIndices(vertices);
        
        // make sure its >= 3 so traingles can be formed
        if (index_list.size() < 3)
        {
            return new Coord[0][0];
        }

        switch(PolygonHelper.findWindingOrder(index_list, vertices))
        {
            case CLOCKWISE -> {}
            case COUNTERCLOCKWISE -> Collections.reverse(Arrays.asList(vertices));
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
                final int b = IterationUtils.getItem(index_list, i - 1);
                final int c = IterationUtils.getItem(index_list, i + 1);

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
}
