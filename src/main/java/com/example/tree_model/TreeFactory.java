package com.example.tree_model;

import com.example.config.BranchParams;
import com.example.config.TrunkParams;
import com.example.representations.Point;
import com.example.utils.NumberGenerator;

public class TreeFactory {

    private final BranchParams branch_params;
    private final TrunkParams trunk_params;
    private static final double LEFT_MIN = 0.2;
    private static final double RIGHT_MAX = 0.8;
    private static final double GROUND_LEVEL = 0;

    public TreeFactory(BranchParams branch_params, TrunkParams trunk_params)
    {
        this.branch_params = branch_params;
        this.trunk_params = trunk_params;
    }

    public Tree createTree()
    {
        // Potential for a treetype in the future
        final Point start_location = new Point(NumberGenerator.getRandomNumber(LEFT_MIN, RIGHT_MAX), GROUND_LEVEL);
        final Trunk trunk = new Trunk(trunk_params, start_location);
        final BranchPropogator propogator = new BranchPropogator(branch_params);
        final BranchFactory branch_factory = new BranchFactory(propogator);
        return new Tree(trunk, branch_factory);
    }
}
