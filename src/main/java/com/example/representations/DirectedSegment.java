package com.example.representations;

import com.example.rendering.TileProvider;
import com.example.utils.EndPointFinder;


public class DirectedSegment 
{
    private final Coord start_location;
    private final Coord end_location;
    private final double length;
    private final double angle; // radians
    private final double width;
    private final TileProvider tile_provider;

    public DirectedSegment(Coord location, double length, double angle, double width, TileProvider tile_provider)
    {
        this.start_location = location;
        this.length = length;
        this.angle = angle;
        this.width = width;
        this.end_location = EndPointFinder.findEnd(start_location, angle, length);
        this.tile_provider = tile_provider;
    }

    public Tile getTile()
    {
        return tile_provider.getTile();
    }

    public Coord getStartLocation()
    {
        return start_location;
    }

    public Coord getEndLocation()
    {
        return end_location;
    }

    public double getLength()
    {
        return length;
    }

    public double getAngle()
    {
        return angle;
    }

    public double getWidth()
    {
        return width;
    }
}
