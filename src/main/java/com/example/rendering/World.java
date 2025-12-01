package com.example.rendering;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.representations.shapes.Beam;
import com.example.utils.Bound;

/**
 * A world represents a collection of renderable models
 * These models can be decomposed into lines (DirectedSegment)
 * This world of lines then can be rasterized into a 2d grid
 */
public class World implements LineBasedRenderable
{
    final LinkedList<LineBasedRenderable> model_list = new LinkedList<>();

    public void addModel(LineBasedRenderable model)
    {
        model_list.add(model);
    }

    @Override
    public ArrayList<Beam> growAndFetchRenderable(Bound bound)
    {
        final ArrayList<Beam> segment_list = new ArrayList<>();
        for (LineBasedRenderable model : model_list)
        {
            segment_list.addAll(model.growAndFetchRenderable(bound));
        }
        return segment_list;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        for (LineBasedRenderable model : model_list)
        {
            model.trimSegments(bound);
        }
    }
}
