package  com.example.models.tree_model;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;
import com.example.rendering.LineBasedRenderable;

public class Tree implements LineBasedRenderable
{

    private final Trunk trunk;

    public Tree(Trunk trunk)
    {
        this.trunk = trunk;
    }

    @Override
    public ArrayList<DirectedSegment> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<DirectedSegment> segments = new ArrayList<>();

        segments.addAll(trunk.growAndFetchRenderable(bound));
        
        // collect any new branches created during trunk growth
        final LinkedList<Branch> new_branches = trunk.fetchAndClearPendingBranches();
        //System.out.println("fetched branches");
        for (Branch branch : new_branches)
        {
            //System.out.println("growing a branch");
            segments.addAll(branch.growAndFetchRenderable(bound));
        }
        
        return segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        trunk.trimSegments(bound);
    }
}