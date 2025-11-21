package com.example;

import java.util.ArrayList;

import com.example.models.ModelFactory;
import com.example.rendering.BasicRasterizerStrategy;
import com.example.rendering.SegmentRasterizerContext;
import com.example.rendering.World;

/**
 * Controls a world
 * Responsible for adding models to it
 * and issuing controls to it
 * rendering it
 */
public class Controller 
{
    private final World world = new World();
    private final ArrayList<ModelFactory> model_factory_list = new ArrayList<>();
    private final SegmentRasterizerContext rasterizer;


    private final double SLEEP_DURATION = 300; // sleep duration of loops in ms
    
    public Controller()
    {
        rasterizer = new SegmentRasterizerContext(new BasicRasterizerStrategy());
    }

    public void addModelFactory(ModelFactory model_factory)
    {
        this.model_factory_list.add(model_factory);
    }

    public void startUp()
    {
        for (ModelFactory model_factory : model_factory_list)
        {
            int num_renders_needed = model_factory.checkNumStartupRenders();
            for (; num_renders_needed >= 0; --num_renders_needed)
            {
                world.addModel(model_factory.getRenderable());
            }
        }
    }

    public void runRound()
    {
        for (ModelFactory model_factory : model_factory_list)
        {
            if (model_factory.checkShouldRender())
            {
                world.addModel(model_factory.getRenderable());
            }
        }
    }
}
