package  com.example.models.tree_model;

import java.util.ArrayList;

import com.example.rendering.ShapeBasedRenderable;
import com.example.representations.shapes.Shape;
import com.example.utils.Bound;

public class Tree implements ShapeBasedRenderable
{

    private final Trunk trunk;

    public Tree(Trunk trunk)
    {
        this.trunk = trunk;
    }

    @Override
    public ArrayList<Shape> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<Shape> segments = new ArrayList<>();
        segments.addAll(trunk.growAndFetchRenderable(bound));
        
        return segments;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        trunk.trimSegments(bound);
    }
}