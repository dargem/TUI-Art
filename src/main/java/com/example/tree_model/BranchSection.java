package com.example.tree_model;

import com.example.utils.Point;

public class BranchSection 
{
    private final Point location;
    private final double length;
    private final double angle; // radians
    private final double width;

    public BranchSection(final Point location, final double length, final double angle, final double width)
    {
        this.location = location;
        this.length = length;
        this.angle = angle;
        this.width = width;
    }

    public Point getLocation()
    {
        return location;
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
