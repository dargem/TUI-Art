package com.example.tree_model;
import java.util.LinkedList;

import com.example.config.TrunkParams;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;
import com.example.utils.Point;

public class Trunk 
{
    private final TrunkParams PARAMETERS;
    private LinkedList<BranchSection> trunk_list;
    private final double start_x;

    public Trunk(TrunkParams params, Point location)
    {
        this.PARAMETERS = params;
        this.trunk_list = new LinkedList<>();
        this.start_x = location.x();

        trunk_list.add(
            new BranchSection(
                location, 
                PARAMETERS.section_length(), 
                0, 
                PARAMETERS.width()
            )
        );
    }

    public void extendTrunk()
    {
        final BranchSection branch_section = trunk_list.getLast();
        final Point next_point = EndPointFinder.findEnd(
            branch_section.getLocation(), 
            branch_section.getAngle(), 
            branch_section.getLength()
        );

    }

    private double find_angle(double current_x)
    {
        // this doesn't care about prior states, can consider doing that in the future
        // e.g. if further left then start, then positive offset
        final double needed_realignment = start_x - current_x;
        final double scale = NumberGenerator.getRandomNumber() + needed_realignment * PARAMETERS.centralness();
        return scale * PARAMETERS.angle_variance();
    }






}
