package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;
import com.example.rendering.LineBasedRenderable;

public class Branch implements LineBasedRenderable{
    private boolean alive;
    private final ArrayList<DirectedSegment> segment_list;
    private final BranchPropogator branch_propogator;

    public Branch(BranchPropogator branch_propogator, DirectedSegment trunk_segment)
    {
        this.alive = true;
        this.branch_propogator = branch_propogator;
        this.segment_list = new ArrayList<>();
        
        // initialize the branch with its first segment
        final DirectedSegment first_segment = branch_propogator.createFirstBranch(trunk_segment);
        segment_list.add(first_segment);
    }

    public boolean getAlive()
    {
        return alive;
    }

    public void extendBranch()
    {
        if (!alive || segment_list.isEmpty())
        {
            return;
        }
        
        final DirectedSegment new_segment = branch_propogator.extendBranch(segment_list.get(segment_list.size() - 1));
        segment_list.add(new_segment);
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> bound_segments = new ArrayList<>();

        while (alive && !segment_list.isEmpty() && bound.checkIsInBound(segment_list.getLast()))
        {
            extendBranch();
            bound_segments.add(segment_list.getLast());
        }

        // if we've grown beyond the bound, mark as dead
        if (!segment_list.isEmpty() && !bound.checkIsInBound(segment_list.getLast()))
        {
            alive = false;
        }

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