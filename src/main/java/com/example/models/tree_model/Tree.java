package  com.example.models.tree_model;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.rendering.Renderable;
import com.example.representations.DirectedSegment;
import com.example.utils.Bound;

public class Tree implements Renderable
{
    final private int MAX_RANCHES = 5;
    final private int MIN_BRANCHES = 5;
    private final LinkedList<Branch> alive_branch_list;
    private final LinkedList<Branch> dead_branch_list;

    private final Trunk trunk;
    private final BranchFactory branch_factory;

    public Tree(Trunk trunk, BranchFactory branch_factory)
    {
        this.trunk = trunk;
        this.branch_factory = branch_factory;
        this.alive_branch_list = new LinkedList<>();
        this.dead_branch_list = new LinkedList<>();
    }

    @Override
    public ArrayList<DirectedSegment> returnBoundSegments(Bound bound)
    {
        final ArrayList<DirectedSegment> segments = new ArrayList<>();

        for (Branch alive_branch: alive_branch_list)
        {
            segments.addAll(alive_branch.returnBoundSegments(bound));
        }

        for (Branch dead_branch: dead_branch_list)
        {
            segments.addAll(dead_branch.returnBoundSegments(bound));
        }

        segments.addAll(trunk.returnBoundSegments(bound));
        return segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        for (Branch alive_branch: alive_branch_list)
        {
            alive_branch.trimSegments(bound);
        }

        for (Branch dead_branch: dead_branch_list)
        {
            dead_branch.trimSegments(bound);
        }

        trunk.trimSegments(bound);
    }
}