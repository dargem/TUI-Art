package com.example.rendering;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.representations.DirectedSegment;
import com.example.utils.Bound;

/**
 * A world represents a collection of renderable models
 * These models can be decomposed into lines (DirectedSegment)
 * This world of lines then can be rasterized into a 2d grid
 */
public class World implements Renderable
{
    final LinkedList<Renderable> model_list = new LinkedList<>();

    public void addModel(Renderable model)
    {
        model_list.add(model);
    }

    @Override
    public ArrayList<DirectedSegment> returnBoundSegments(Bound bound)
    {
        final ArrayList<DirectedSegment> segment_list = new ArrayList<>();
        for (Renderable model : model_list)
        {
            segment_list.addAll(model.returnBoundSegments(bound));
        }
        return segment_list;
    }

    @Override
    public void trimSegments(Bound bound)
    {
        for (Renderable model : model_list)
        {
            model.trimSegments(bound);
        }
    }
}
