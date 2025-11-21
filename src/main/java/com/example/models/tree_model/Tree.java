package  com.example.models.tree_model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;
import com.example.rendering.LineBasedRenderable;

public class Tree implements LineBasedRenderable
{
    private final LinkedList<Branch> alive_branch_list;
    private final LinkedList<Branch> dead_branch_list;

    private final Trunk trunk;

    public Tree(Trunk trunk)
    {
        this.trunk = trunk;
        this.alive_branch_list = new LinkedList<>();
        this.dead_branch_list = new LinkedList<>();
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> segments = new ArrayList<>();

        for (Branch alive_branch: alive_branch_list)
        {
            segments.addAll(alive_branch.growAndFetchRenderable(bound));
        }

        segments.addAll(trunk.growAndFetchRenderable(bound));
        
        // collect any new branches created during trunk growth
        final LinkedList<Branch> new_branches = trunk.fetchAndClearPendingBranches();
        alive_branch_list.addAll(new_branches);
        
        // move dead branches from alive to dead list
        final Iterator<Branch> branch_iterator = alive_branch_list.iterator();
        while (branch_iterator.hasNext())
        {
            final Branch branch = branch_iterator.next();
            if (!branch.getAlive())
            {
                branch_iterator.remove();
                dead_branch_list.add(branch);
            }
        }
        
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