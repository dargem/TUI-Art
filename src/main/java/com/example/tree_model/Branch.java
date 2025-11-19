package com.example.tree_model;
import java.util.ArrayList;

import com.example.representations.DirectedSegment;

public class Branch {
    private boolean alive;
    private ArrayList<DirectedSegment> length_list;
    private final BranchPropogator branch_propogator;

    public Branch(BranchPropogator branch_propogator)
    {
        this.alive = true;
        this.branch_propogator = branch_propogator;
        this.length_list = new ArrayList<>();
    }

    public boolean getAlive()
    {
        return alive;
    }

    public void extendBranch()
    {
        if (length_list.isEmpty())
        {

            return;
        }
    }
}
