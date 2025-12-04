package com.example.representations.polygon_decomposer;

import com.example.representations.Coord;

public interface PolygonDecomposerStrategy 
{
    /**
     * Decomposes a simple polygon into triangles
     * @param vertices vertices of the polygon (in order), can be clock or anticlockwise
     * @return An array of array of coords, in form Coord[n][3] (array triangles)
     */
    public abstract Coord[][] decomposePolygon(Coord[] vertices);    
}
