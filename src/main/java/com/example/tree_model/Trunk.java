package com.example.tree_model;
import java.util.LinkedList;

import com.example.config.TrunkParams;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;
import com.example.utils.Point;

public class Trunk 
{
    private final TrunkParams parameters;
    private final LinkedList<BranchSection> trunk_list;
    private final double start_x;

    public Trunk(TrunkParams params, Point location)
    {
        this.parameters = params;
        this.trunk_list = new LinkedList<>();
        this.start_x = location.x();

        trunk_list.add(
            new BranchSection(
                location, 
                parameters.section_length(), 
                0, 
                parameters.width()
            )
        );
    }

    public void extendTrunk()
    {
        // find parameters for the next branch section
        final BranchSection branch_section = trunk_list.getLast();
        final Point location = branch_section.getLocation();
        final Point next_point = EndPointFinder.findEnd(
            location, 
            branch_section.getAngle(), 
            branch_section.getLength()
        );

        final double next_angle = find_angle(location.x());

        // add the next branch section to the top
        trunk_list.add(new BranchSection(
                next_point, 
                parameters.section_length(), 
                next_angle, parameters.width()
            )
        );
    }

    private double find_angle(double current_x)
    {
        // this doesn't care about prior states, can consider doing that in the future
        // e.g. if further left then start, then positive offset
        final double needed_realignment = start_x - current_x;
        final double needed_realignment_angle = Math.asin(needed_realignment / parameters.section_length());
        final double scale = NumberGenerator.getRandomNumber() + needed_realignment_angle * parameters.centralness();
        return scale * parameters.angle_variance();
    }
}
