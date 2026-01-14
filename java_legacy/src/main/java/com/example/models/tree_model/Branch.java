package com.example.models.tree_model;
import java.util.ArrayList;
import java.util.Iterator;

import com.example.rendering.RenderableShape;
import com.example.rendering.ShapeBasedRenderable;
import com.example.representations.Coord;
import com.example.representations.shapes.Beam;
import com.example.utils.Bound;
import com.example.utils.Direction;

public class Branch implements ShapeBasedRenderable{
    private boolean alive;
    private final ArrayList<Beam> segment_list;
    private final BranchPropagator branch_propagator;

    public Branch(BranchPropagator branch_propagator, Beam trunk_segment, Direction direction)
    {
        this.alive = true;
        this.branch_propagator = branch_propagator;
        this.segment_list = new ArrayList<>();
        
        // initialize the branch with its first segment
        final Beam first_segment = branch_propagator.createFirstBranch(trunk_segment, direction);
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
        if (branch_propagator.checkDies(segment_list.getLast()))
        {
            alive = false;
            return;
        }
        
        final Beam new_segment = branch_propagator.extendBranch(segment_list.getLast());
        segment_list.add(new_segment);
    }

    @Override
    public ArrayList<RenderableShape> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<RenderableShape> bound_shapes = new ArrayList<>();

        do
        {
            extendBranch();
            bound_shapes.add(new RenderableShape(segment_list.getLast(), branch_propagator.getTileProvider()));
        }
        while (alive && bound.checkIsInXBound(segment_list.getLast()));

        //System.out.print(bound_segments.size());
        return bound_shapes;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        final Iterator<Beam> segment = segment_list.iterator();
        while (segment.hasNext())
        {
            if (bound.checkLowerTrimmable(segment.next()))
            {
                segment.remove();
            }
        }
    }
}