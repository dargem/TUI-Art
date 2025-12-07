package com.example;

import java.util.ArrayList;

import com.example.models.ModelFactory;
import com.example.rendering.FloatingFillRasterizerStrategy;
import com.example.rendering.RasterizerContext;
import com.example.rendering.World;
import com.example.rendering.polygon_decomposer.EarClippingStrategy;
import com.example.representations.Board;
import com.example.representations.shapes.Shape;
import com.example.terminal.Printer;
import com.example.terminal.TerminalPublisher;
import com.example.utils.BoundFactory;

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
    private final TerminalPublisher terminal_publisher;
    private final ArrayList<ModelFactory> model_factory_list = new ArrayList<>();
    private final BoundFactory bound_factory = new BoundFactory();
    private final RasterizerContext rasterizer  = new RasterizerContext(
        new FloatingFillRasterizerStrategy(
            new EarClippingStrategy()
        )
    );
    private int rounds = 0;

    private final int SLEEP_DURATION = 100; // sleep duration of loops in ms
    
    public Controller(TerminalPublisher terminal_publisher)
    {
        this.terminal_publisher = terminal_publisher;

        // adds all the subscribers to the terminal publisher
        terminal_publisher.addTerminalSubscriber(printer);
        terminal_publisher.addTerminalSubscriber(bound_factory);
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
        // check for changes in the terminal
        terminal_publisher.checkEmitTerminalSizeNews();

        for (ModelFactory model_factory : model_factory_list)
        {
            if (model_factory.checkShouldRender())
            {
                world.addModel(model_factory.getRenderable());
            }
        }

        ArrayList<Shape> shapes = world.growAndFetchRenderable(bound_factory.createBound(0, rounds + 30));
        //System.out.println(directed_segments.size());
        //System.out.println("starting rasterisation");
        rasterizer.rasterizeShapes(shapes, board);
        //System.out.println("rasterisation done");
        //System.out.println(board.getRow(rounds).size());
        printer.printLine(rounds, board);
        ++rounds;

        try
        {
            Thread.sleep(SLEEP_DURATION);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException("Interrupted exception with sleep " + e);
        }
    }
}
