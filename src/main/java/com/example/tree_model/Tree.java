package  com.example.tree_model;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.rendering.Renderable;
import com.example.representations.DirectedSegment;

public class Tree implements Renderable
{
    final private int MAX_RANCHES = 5;
    final private int MIN_BRANCHES = 5;
    private final LinkedList<Branch> alive_branch;
    private final LinkedList<Branch> dead_branch;

    private final Trunk trunk;
    private final BranchFactory branch_factory;

    public Tree(Trunk trunk, BranchFactory branch_factory)
    {
        this.trunk = trunk;
        this.branch_factory = branch_factory;
        this.alive_branch = new LinkedList<>();
        this.dead_branch = new LinkedList<>();
    }

    @Override
    public ArrayList<DirectedSegment> returnSegments(double min_y, double max_y)
    {
        
    }
    

}