package com.example.model_layer;

import com.example.config.GrowthParams;

public class BranchPropogator {
    private final GrowthParams PARAMETERS;

    public BranchPropogator(GrowthParams params)
    {
        this.PARAMETERS = params;
    }

    public BranchSection extendBranch(final BranchSection branchSection)
    {
        // First calculate start branch of next using original
        final double angle = branchSection.getAngle();
        final double length = branchSection.getLength();
        
        final Point next_location = findNextPoint(branchSection.getLocation(), angle, length);
        final double next_angle = findNextAngle(angle);
        final double next_length = findNextLength(length);



    }

    private Point findNextPoint(final Point start_point, final double angle, final double length)
    {
        final double end_x = start_point.x() + length*Math.sin(angle);
        final double end_y = start_point.y() + length*Math.cos(angle);
        return new Point(end_x, end_y);
    }

    private double findNextAngle(final double angle)
    {
        // generate a uniform random number between -1, 1
        final double scale = Math.random() * 2 - 1;
        return angle + scale * PARAMETERS.angleVariance();
    }
}
