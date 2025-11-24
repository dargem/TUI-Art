package com.example.models.tree_model;

import com.example.config.BranchParams;
import com.example.rendering.TileProvider;
import com.example.representations.DirectedSegment;
import com.example.representations.Coord;
import com.example.utils.NumberGenerator;

public class BranchPropogator {
    private final BranchParams parameters;
    private final TileProvider tile_provider;
    // y = x^(1/3), then *1.2 just for a bit more variation, need 1.0 or its int division
    private final double BASE_BRANCH_ANGLE = Math.PI/3;

    public BranchPropogator(BranchParams params, TileProvider tile_provider)
    {
        this.parameters = params;
        this.tile_provider = tile_provider;
    }

    public boolean checkDies()
    {
        return NumberGenerator.getRandomNumber() < parameters.death_chance();
    }

    public DirectedSegment createFirstBranch(DirectedSegment trunk_segment)
    {
        // choose whether its a left/right branch, setting it to be an offset off the trunk
        double angle = trunk_segment.getAngle();
        angle += NumberGenerator.getRandomNumber() < 0.5 ? BASE_BRANCH_ANGLE : -BASE_BRANCH_ANGLE;   
        // add a random angle for variation
        angle += NumberGenerator.getRandomNumber(-1, 1) * parameters.angle_variance();

        final double length = parameters.initial_length();
        final Coord start_location = trunk_segment.getEndLocation();
        final double width = trunk_segment.getWidth() * 0.8; // branches start slightly thinner
        
        return new DirectedSegment(start_location, length, angle, width, tile_provider);
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

        return new DirectedSegment(next_location, next_length, next_angle, next_width, tile_provider);
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
