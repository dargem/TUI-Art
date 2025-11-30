package  com.example.models.tree_model;

import java.util.ArrayList;

import com.example.rendering.LineBasedRenderable;
import com.example.representations.DirectedSegment;
import com.example.utils.Bound;

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
        
        return segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        trunk.trimSegments(bound);
    }
}