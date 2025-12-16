package com.example.rendering;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.utils.Bound;

/**
 * A world represents a collection of renderable models
 * These models can be decomposed into lines (DirectedSegment)
 * This world of lines then can be rasterized into a 2d grid
 */
public class World implements ShapeBasedRenderable
{
    final LinkedList<ShapeBasedRenderable> model_list = new LinkedList<>();

    public void addModel(ShapeBasedRenderable model)
    {
        model_list.add(model);
    }

    @Override
    public ArrayList<RenderableShape> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<RenderableShape> shape_list = new ArrayList<>();
        for (ShapeBasedRenderable model : model_list)
        {
            shape_list.addAll(model.growAndFetchRenderable(bound));
        }
        return shape_list;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        for (ShapeBasedRenderable model : model_list)
        {
            model.trimSegments(bound);
        }
    }
}
