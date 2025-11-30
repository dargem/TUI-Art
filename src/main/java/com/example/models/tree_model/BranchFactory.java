package com.example.models.tree_model;

import com.example.representations.DirectedSegment;

public class BranchFactory {
    
    private final BranchPropogator branch_propogator;

    public BranchFactory(BranchPropogator branch_propogator)
    {
        this.branch_propogator = branch_propogator;
    }

    public Branch getBranch(DirectedSegment trunk_segment)
    {
        // option for adding later branch types
        return new Branch(branch_propogator, trunk_segment);
    }
}
