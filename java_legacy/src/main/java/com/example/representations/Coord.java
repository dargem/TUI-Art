package com.example.representations;

/**
 * An immutable record for holding coordinates
 */
public record Coord(double x, double y) 
{
    /**
     * Returns a new Coord, translated from current location
     * @param dx change in x
     * @param dy change in y
     * @return translated coordinate
     */
    public Coord translate(double dx, double dy)
    {
        return new Coord(this.x + dx, this.y + dy);
    }
}