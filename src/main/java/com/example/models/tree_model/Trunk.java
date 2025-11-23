package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.config.TrunkParams;
import com.example.representations.DirectedSegment;
import com.example.representations.Coord;
import com.example.utils.Bound;
import com.example.utils.EndPointFinder;
import com.example.utils.NumberGenerator;
import com.example.rendering.LineBasedRenderable;
import com.example.rendering.TileProvider;

public class Trunk implements LineBasedRenderable
{
    private final TrunkParams parameters;
    private final LinkedList<DirectedSegment> trunk_list;
    private final double start_x;
    private final BranchFactory branch_factory;
    private final LinkedList<Branch> pending_branches;
    private final TileProvider tile_provider;

    public Trunk(TrunkParams params, Coord location, BranchFactory branch_factory, TileProvider tile_provider)
    {
        this.parameters = params;
        this.trunk_list = new LinkedList<>();
        this.start_x = location.x();
        this.branch_factory = branch_factory;
        this.pending_branches = new LinkedList<>();
        this.tile_provider = tile_provider;

        trunk_list.add(
            new DirectedSegment(
                location, 
                parameters.section_length(), 
                0, 
                parameters.width(),
                tile_provider
            )
        );
    }

    public void extendTrunk()
    {
        // find parameters for the next branch section
        final DirectedSegment branch_section = trunk_list.getLast();
        final Coord next_point = branch_section.getEndLocation();
        final double next_angle = find_angle(next_point.x(), branch_section.getAngle());

        // add the next branch section to the top
        trunk_list.add(new DirectedSegment(
                next_point, 
                parameters.section_length(), 
                next_angle, parameters.width(),
                tile_provider
            )
        );

        // check if a branch should be created
        if (shouldCreateBranch())
        {
            System.out.println("branch made");
            final Branch new_branch = branch_factory.getBranch(trunk_list.getLast());
            pending_branches.add(new_branch);
        }
    }

    private boolean shouldCreateBranch()
    {
        return NumberGenerator.getRandomNumber() < parameters.branch_chance();
    }

    public LinkedList<Branch> fetchAndClearPendingBranches()
    {
        final LinkedList<Branch> branches_to_return = new LinkedList<>(pending_branches);
        pending_branches.clear();
        return branches_to_return;
    }

    private double find_angle(double current_x, Double current_angle)
    {
        if (current_angle.isNaN())
        {
            current_angle = 0.0;
        }

        double random_angle;
        do
        {
            random_angle = current_angle + NumberGenerator.getRandomNumber(-1, 1) * parameters.angle_variance();
            //System.out.println(random_angle);
        }
        while (random_angle > Math.PI/3 || random_angle < -Math.PI/3);

        final double expected_x = EndPointFinder.findEndX(current_x, random_angle, parameters.section_length());
        final double wanted_offset = (start_x - expected_x) * parameters.centralness();
        return Math.asin(wanted_offset / parameters.section_length()) + random_angle;
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> bound_segments = new ArrayList<>();

        while (bound.checkIsInLooseBound(trunk_list.getLast()))
        {
            extendTrunk();
            bound_segments.add(trunk_list.getLast());
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
