package com.example.models.tree_model;

import com.example.config.BranchParams;
import com.example.rendering.tile_providing.TileProvider;
import com.example.representations.Coord;
import com.example.representations.shapes.Beam;
import com.example.utils.Direction;
import com.example.utils.NumberGenerator;

public class BranchPropogator {
    private final double START_VARIATION = 0.5;
    private final double START_WIDTH_SCALAR = 0.6;
    private final BranchParams parameters;
    private final TileProvider tile_provider;
    // y = x^(1/3), then *1.2 just for a bit more variation, need 1.0 or its int division
    private final double BASE_BRANCH_ANGLE = Math.PI/2.5;

    public BranchPropogator(BranchParams params, TileProvider tile_provider)
    {
        this.parameters = params;
        this.tile_provider = tile_provider;
    }

    public boolean checkDies(Beam last_segment)
    {
        final double adjusted_death_chance = parameters.death_chance() / last_segment.getWidth();
        return NumberGenerator.getRandomNumber() < adjusted_death_chance;
    }

    public Beam createFirstBranch(Beam trunk_segment, Direction direction)
    {
        double angle = trunk_segment.getAngle();

        // choose whether its a left/right branch, setting it to be an offset off the trunk
        switch(direction)
        {
            case LEFT -> angle -= BASE_BRANCH_ANGLE;
            case RIGHT -> angle += BASE_BRANCH_ANGLE;
            default -> throw new RuntimeException("Direction " + direction + " not implemented in switch");
        }

        // add a random angle for variation
        angle += NumberGenerator.getRandomNumber(-1, 1) * parameters.angle_variance() * START_VARIATION;

        final double length = parameters.initial_length();
        final Coord start_location = trunk_segment.getEndCoord();
        final double width = trunk_segment.getWidth() * START_WIDTH_SCALAR; // branches start slightly thinner
        
        return new Beam(start_location, length, angle, width, tile_provider);
    }

    public Beam extendBranch(final Beam branch_section)
    {
        // First calculate start branch of next using original
        final double angle = branch_section.getAngle();
        final double length = branch_section.getLength();
        
        final Coord next_location = branch_section.getEndCoord();
        final double next_length = findNextLength(length);
        final double next_angle = findNextAngle(angle);
        final double next_width = findNextWidth(branch_section.getWidth());

        return new Beam(next_location, next_length, next_angle, next_width, tile_provider);
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
