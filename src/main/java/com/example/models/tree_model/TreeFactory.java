package com.example.models.tree_model;

import com.example.config.BranchParams;
import com.example.config.TrunkParams;
import com.example.models.ModelFactory;
import com.example.representations.Coord;
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
    private final TileProvider trunk_tile_provider = new TileProvider(10, 11, , null);

    public TreeFactory(BranchParams branch_params, TrunkParams trunk_params)
    {
        this.branch_params = branch_params;
        this.trunk_params = trunk_params;
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
