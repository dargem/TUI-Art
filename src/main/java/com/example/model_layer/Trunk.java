package com.example.model_layer;
import java.util.LinkedList;

import com.example.config.TrunkParams;

public class Trunk 
{
    private final TrunkParams PARAMETERS;
    private LinkedList<BranchSection> trunk_list;
    private final double start_x;

    public Trunk(TrunkParams params, Point location)
    {
        this.PARAMETERS = params;
        this.trunk_list = new LinkedList<>();
        this.start_x = location.x();
    }

    




}
