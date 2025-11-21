package com.example.models.tree_model;

import com.example.config.BranchParams;
import com.example.representations.DirectedSegment;
import com.example.representations.Point;
import com.example.utils.NumberGenerator;

public class BranchPropogator {
    private final BranchParams parameters;
    // y = x^(1/3), then *1.2 just for a bit more variation
    private final double INITIAL_ANGLE_SCALAR = 1/3*1.2;

    public BranchPropogator(BranchParams params)
    {
        this.parameters = params;
    }

    public DirectedSegment createFirstBranch(DirectedSegment trunk_segment)
    {
        final double angle = Math.pow(NumberGenerator.getRandomNumber(), INITIAL_ANGLE_SCALAR);
        final double length = parameters.initial_length();
        final Point start_location = trunk_segment.getEndLocation();
        final double width = trunk_segment.getWidth() * 0.8; // branches start slightly thinner
        
        return new DirectedSegment(start_location, length, angle, width);
    }

    public DirectedSegment extendBranch(final DirectedSegment branch_section)
    {
        // First calculate start branch of next using original
        final double angle = branch_section.getAngle();
        final double length = branch_section.getLength();
        
        final Point next_location = branch_section.getEndLocation();
        final double next_length = findNextLength(length);
        final double next_angle = findNextAngle(angle);
        final double next_width = findNextWidth(branch_section.getWidth());

        return new DirectedSegment(next_location, next_length, next_angle, next_width);
    }

    private double findNextLength(final double length)
    {
        final double scale = NumberGenerator.getRandomNumber();
        return length * (1 - parameters.length_decay()*scale);
    }

    private double findNextAngle(final double angle)
    {
        final double scale = NumberGenerator.getRandomNumber(-1, 1);
        return angle + scale * parameters.angle_variance();
    }

    private double findNextWidth(final double width)
    {
        final double scale = NumberGenerator.getRandomNumber();
        return width * (1 - parameters.width_decay()*scale);
    }
}
