package com.example.tree_model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.config.TrunkParams;
import com.example.rendering.Renderable;
import com.example.representations.DirectedSegment;
import com.example.representations.Point;
import com.example.utils.Bound;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;

public class Trunk implements Renderable
{
    private final TrunkParams parameters;
    private final LinkedList<DirectedSegment> trunk_list;
    private final double start_x;

    public Trunk(TrunkParams params, Point location)
    {
        this.parameters = params;
        this.trunk_list = new LinkedList<>();
        this.start_x = location.x();

        trunk_list.add(
            new DirectedSegment(
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
        final DirectedSegment branch_section = trunk_list.getLast();
        final Point next_point = branch_section.getEndLocation();
        final double next_angle = find_angle(next_point.x());

        // add the next branch section to the top
        trunk_list.add(new DirectedSegment(
                next_point, 
                parameters.section_length(), 
                next_angle, parameters.width()
            )
        );
    }

    private double find_angle(double current_x)
    {
        final double random_angle = NumberGenerator.getRandomNumber() * parameters.angle_variance();
        final double expected_x = EndPointFinder.findEndX(current_x, random_angle, parameters.section_length());
        // this doesn't care about prior states, can consider doing that in the future
        final double wanted_offset = (start_x - expected_x) * parameters.centralness();
        return Math.asin(wanted_offset / parameters.section_length());
    }

    @Override
    public ArrayList<DirectedSegment> returnBoundSegments(Bound bound)
    {
        final ArrayList<DirectedSegment> bound_segments = new ArrayList<>();
        for (DirectedSegment segment: trunk_list)
        {
            if (bound.checkIsInBound(segment))
            {
                bound_segments.add(segment);
            }
        }
        return bound_segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        final Iterator<DirectedSegment> segment = trunk_list.iterator();
        while (segment.hasNext()) 
        {
            if (bound.checkLowerTrimmable(segment.next())) 
            {
                segment.remove(); // safe removal with iterator
            }
        }
    }
}
