package com.example.models.tree_model;

import com.example.config.BranchParams;
import com.example.representations.DirectedSegment;
import com.example.representations.Coord;
import com.example.utils.NumberGenerator;

public class BranchPropogator {
    private final BranchParams parameters;
    // y = x^(1/3), then *1.2 just for a bit more variation, need 1.0 or its int division
    private final double INITIAL_ANGLE_POWER_SCALAR = 1.0/3.0;
    private final double INITIAL_ANGLE_SCALAR = 1.3;

    public BranchPropogator(BranchParams params)
    {
        this.parameters = params;
    }

    public boolean checkDies()
    {
        return NumberGenerator.getRandomNumber() < parameters.death_chance();
    }

    public DirectedSegment createFirstBranch(DirectedSegment trunk_segment)
    {
        double angle = Math.pow(NumberGenerator.getRandomNumber(), INITIAL_ANGLE_POWER_SCALAR) * INITIAL_ANGLE_SCALAR;
        if (NumberGenerator.getRandomNumber() < 0.5)
        {
            angle *= -1;
        }
        final double length = parameters.initial_length();
        final Coord start_location = trunk_segment.getEndLocation();
        final double width = trunk_segment.getWidth() * 0.8; // branches start slightly thinner
        
        return new DirectedSegment(start_location, length, angle, width);
    }

    public DirectedSegment extendBranch(final DirectedSegment branch_section)
    {
        // First calculate start branch of next using original
        final double angle = branch_section.getAngle();
        final double length = branch_section.getLength();
        
        final Coord next_location = branch_section.getEndLocation();
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
        //System.out.print( angle + scale * parameters.angle_variance());
        return angle + scale * parameters.angle_variance();
    }

    private double findNextWidth(final double width)
    {
        final double scale = NumberGenerator.getRandomNumber();
        return width * (1 - parameters.width_decay()*scale);
    }
}
