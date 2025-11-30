package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.rendering.LineBasedRenderable;
import com.example.representations.Coord;
import com.example.representations.DirectedSegment;
import com.example.utils.Bound;
import com.example.utils.Direction;

public class Branch implements LineBasedRenderable{
    private boolean alive;
    private final ArrayList<DirectedSegment> segment_list;
    private final BranchPropogator branch_propogator;

    public Branch(BranchPropogator branch_propogator, DirectedSegment trunk_segment, Direction direction)
    {
        this.alive = true;
        this.branch_propogator = branch_propogator;
        this.segment_list = new ArrayList<>();
        
        // initialize the branch with its first segment
        final DirectedSegment first_segment = branch_propogator.createFirstBranch(trunk_segment, direction);
        segment_list.add(first_segment);
    }

    public Coord getFinalCoord()
    {
        return segment_list.getLast().getEndCoord();
    }

    public Coord getStartCoord()
    {
        return segment_list.getFirst().getStartCoord();
    }

    public boolean getAlive()
    {
        return alive;
    }

    private void extendBranch()
    {
        if (branch_propogator.checkDies(segment_list.getLast()))
        {
            alive = false;
            return;
        }
        
        final DirectedSegment new_segment = branch_propogator.extendBranch(segment_list.getLast());
        segment_list.add(new_segment);
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> bound_segments = new ArrayList<>();

        do
        {
            extendBranch();
            bound_segments.add(segment_list.getLast());
        }
        while (alive && bound.checkIsInXBound(segment_list.getLast()));

        //System.out.print(bound_segments.size());
        return bound_segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        final Iterator<DirectedSegment> segment = segment_list.iterator();
        while (segment.hasNext())
        {
            if (bound.checkLowerTrimmable(segment.next()))
            {
                segment.remove();
            }
        }
    }
}