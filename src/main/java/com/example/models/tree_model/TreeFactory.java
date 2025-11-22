package com.example.models.tree_model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.example.config.BranchParams;
import com.example.config.TrunkParams;
import com.example.models.ModelFactory;
import com.example.representations.Coord;
import com.example.utils.Colour;
import com.example.utils.NumberGenerator;
import com.example.rendering.LineBasedRenderable;
import com.example.rendering.TileProvider;

public class TreeFactory implements ModelFactory{

    private final BranchParams branch_params;
    private final TrunkParams trunk_params;
    private static final double LEFT_MIN = 40;
    private static final double RIGHT_MAX = 50;
    private static final double GROUND_LEVEL = 0;
    // Potentially have it load startup renders from a config file later
    private static final int NUM_STARTUP_RENDERS = 0;

    private final TileProvider trunk_tile_provider;

    public TreeFactory(BranchParams branch_params, TrunkParams trunk_params)
    {
        this.branch_params = branch_params;
        this.trunk_params = trunk_params;
        final TreeMap<Double, ArrayList<Character>> tree_char_map = new TreeMap<>();

        tree_char_map.put(
            0.0,
            new ArrayList<>(List.of('|', '[', ']', 'I'))
        );

        tree_char_map.put(
            Math.PI/8,
            new ArrayList<>(List.of('/', '[', 'P'))
        );

        tree_char_map.put(
            -Math.PI/8,
            new ArrayList<>(List.of('\\', ']', 'J')) // only one backslash its just escape
        );

        tree_char_map.put(
            Math.PI/2,
            new ArrayList<>(List.of('-', '_'))
        );

        trunk_tile_provider = new TileProvider(
            0,
            1,
            Colour.BROWN_LIGHT,
            tree_char_map
        );

    }

    @Override
    public LineBasedRenderable getRenderable()
    {
        // Potential for a treetype in the future
        final Coord start_location = new Coord(NumberGenerator.getRandomNumber(LEFT_MIN, RIGHT_MAX), GROUND_LEVEL);
        final BranchPropogator propogator = new BranchPropogator(branch_params);
        final BranchFactory branch_factory = new BranchFactory(propogator);
        final Trunk trunk = new Trunk(trunk_params, start_location, branch_factory);
        return new Tree(trunk);
    }

    @Override
    public boolean checkShouldRender()
    {
        // trees will never get rendered randomly
        return false;
    }

    @Override
    public int checkNumStartupRenders()
    {
        // trees will never get rendered randomly
        return NUM_STARTUP_RENDERS;
    }

}
