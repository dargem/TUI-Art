package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;
import com.example.rendering.LineBasedRenderable;

public class Branch implements LineBasedRenderable{
    private boolean alive;
    private ArrayList<DirectedSegment> segment_list;
    private final BranchPropogator branch_propogator;

    public Branch(BranchPropogator branch_propogator)
    {
        this.alive = true;
        this.branch_propogator = branch_propogator;
        this.segment_list = new ArrayList<>();
    }

    public boolean getAlive()
    {
        return alive;
    }

    public void extendBranch()
    {
        if (segment_list.isEmpty())
        {
            
            return;
        }
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> filtered_segment_list = new ArrayList<>();
        for (DirectedSegment segment: segment_list)
        {
            if (bound.checkIsInBound(segment))
            {
                filtered_segment_list.add(segment);
            }
        }
        return filtered_segment_list;
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
