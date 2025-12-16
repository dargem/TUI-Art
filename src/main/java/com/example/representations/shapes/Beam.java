package com.example.representations.shapes;

import com.example.rendering.tile_providing.TileProvider;
import com.example.representations.Coord;
import com.example.representations.Tile;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;

public class Beam implements Shape
{
    private final Coord start_location;
    private final Coord end_location;
    private final double length;
    private final double angle; // radians
    private final double width;
    private final TileProvider tile_provider;

    public Beam(Coord location, double length, double angle, double width, TileProvider tile_provider)
    {
        this.start_location = location;
        this.length = length;
        this.angle = angle;
        this.width = width;
        this.end_location = EndPointFinder.findEnd(start_location, angle, length);
        this.tile_provider = tile_provider;
    }

    public Beam(Coord start_location, Coord end_location, double length, double angle, double width, TileProvider tile_provider)
    {
        this.start_location = start_location;
        this.length = length;
        this.angle = angle;
        this.width = width;
        this.end_location = end_location;
        this.tile_provider = tile_provider;
    }

    public Tile getTile()
    {
        return tile_provider.getTile(angle);
    }

    public Coord getStartCoord()
    {
        return start_location;
    }

    /**
     * Finds a random coord along the segments length
     * @return a coord along the segment
     */
    public Coord getRandomIntersection()
    {
        final double hypotenuse_length = NumberGenerator.getRandomNumber() * length;
        return new Coord(
            start_location.x() + Math.sin(angle) * hypotenuse_length,
            start_location.y() + Math.cos(angle) * hypotenuse_length
        );
    }

    public Coord getEndCoord()
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

    /**
     * Accepts a visitor, allowing them to perform operations on Beam
     * Used as part of the visitor pattern
     * @param <C> The type of the visitor's context object
     * @param <R> the return type of the visitor's operation
     * @param visitor the visitor of the Beam
     * @param context the visitor's context object
     */
    @Override
    public <C, R> R acceptVisitor(ShapeVisitor<C, R> visitor, C context) 
    {
        return visitor.visitBeam(this, context);
    }

    /**
     * Creates a new beam that is the translated version of this one
     * @param dx the x translation needed
     * @param dy the y translation needed
     * @return The new translated shape
     */
    @Override
    public Beam translate(double dx, double dy) 
    {
        return new Beam(
            start_location.translate(dx, dy), 
            end_location.translate(dx, dy), 
            length, angle, width, tile_provider
        );
    }

    @Override
    public double getMinY() 
    {
        return Math.min(start_location.y(), end_location.y());
    }

    @Override
    public double getMaxY() 
    {
        return Math.max(start_location.y(), end_location.y());
    }
}
