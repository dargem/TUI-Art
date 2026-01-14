package com.example.models.tree_model;

import com.example.representations.shapes.Beam;
import com.example.utils.Direction;

public class BranchFactory 
{

    private final BranchPropagator branch_propagator;

    public BranchFactory(BranchPropagator branch_propagator)
    {
        this.branch_propagator = branch_propagator;
    }

    public Branch getBranch(Beam trunk_segment, Direction direction)
    {
        // option for adding later branch types
        return new Branch(branch_propagator, trunk_segment, direction);
    }
}
