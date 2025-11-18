package com.example.model_layer;

import com.example.config.GrowthParams;

public class BranchPropogator {
    private final GrowthParams params;

    public BranchPropogator(GrowthParams params)
    {
        this.params = params;
    }

    public BranchSection extendBranch(BranchSection branchSection)
    {
        // First calculate start branch of next using original
        Point location = branchSection.getLocation();
        double angle = branchSection.getAngle();
        double length = branchSection.getLength();
        location.x();

    }

    private Point findNextPoint(double x_start, double y_start, double angle, double length)
    {
        double end_x = x_start + length*Math.sin(angle);
        double end_y = y_start + length*Math.cos(angle);
        return Point(end_x, end_y);
    }
}
