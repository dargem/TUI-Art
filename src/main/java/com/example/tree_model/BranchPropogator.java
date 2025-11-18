package com.example.tree_model;

import com.example.config.BranchParams;
import com.example.utils.NumberGenerator;
import com.example.utils.Point;
import com.example.utils.EndPointFinder;

public class BranchPropogator {
    private final BranchParams PARAMETERS;

    public BranchPropogator(BranchParams params)
    {
        this.PARAMETERS = params;
    }

    public BranchSection extendBranch(final BranchSection branchSection)
    {
        // First calculate start branch of next using original
        final double angle = branchSection.getAngle();
        final double length = branchSection.getLength();
        
        final Point next_location = EndPointFinder.findEnd(branchSection.getLocation(), angle, length);
        final double next_length = findNextLength(length);
        final double next_angle = findNextAngle(angle);
        final double next_width = findNextWidth(branchSection.getWidth());

        return new BranchSection(next_location, next_length, next_angle, next_width);
    }

    private double findNextLength(final double length)
    {
        final double scale = NumberGenerator.getRandomNumber();
        return length * (1 - PARAMETERS.lengthDecay()*scale);
    }

    private double findNextAngle(final double angle)
    {
        final double scale = NumberGenerator.getRandomNumber(-1, 1);
        return angle + scale * PARAMETERS.angleVariance();
    }

    private double findNextWidth(final double width)
    {
        final double scale = NumberGenerator.getRandomNumber();
        return width * (1 - PARAMETERS.widthDecay()*scale);
    }
}
