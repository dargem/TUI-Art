package com.example;

import java.util.ArrayList;

import com.example.game_board.Board;
import com.example.utils.Bound;
import com.example.models.ModelFactory;
import com.example.rendering.BasicRasterizerStrategy;
import com.example.rendering.OutlineRasterizerStrategy;
import com.example.rendering.SegmentRasterizerContext;
import com.example.rendering.World;
import com.example.representations.DirectedSegment;

/**
 * Controls a world
 * Responsible for adding models to it
 * and issuing controls to it
 * rendering it
 */
public class Controller 
{
    private final Board board = new Board();
    private final World world = new World();
    private final Printer printer = new Printer();
    private final ArrayList<ModelFactory> model_factory_list = new ArrayList<>();
    private final SegmentRasterizerContext rasterizer;
    private int rounds = 0;

    private final int SLEEP_DURATION = 100; // sleep duration of loops in ms
    
    public Controller()
    {
        rasterizer = new SegmentRasterizerContext(new OutlineRasterizerStrategy());
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

        ArrayList<DirectedSegment> directed_segments = world.growAndFetchRenderable(new Bound(0, rounds + 30));
        //System.out.println(directed_segments.size());
        //System.out.println("starting rasterisation");
        rasterizer.rasterizeSegments(directed_segments, board);
        //System.out.println("rasterisation done");
        //System.out.println(board.getRow(rounds).size());
        printer.printLine(rounds, board);
        rounds += 1;

        try
        {
            Thread.sleep(SLEEP_DURATION);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Issue with sleep " + e);
        }
    }
}
