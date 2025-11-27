package com.example.models.tree_model;

import java.util.ArrayList;
import java.util.List;

import com.example.config.BranchParams;
import com.example.config.TrunkParams;
import com.example.models.ModelFactory;
import com.example.rendering.LineBasedRenderable;
import com.example.rendering.TileProvider;
import com.example.representations.AngleMap;
import com.example.representations.Coord;
import com.example.utils.Colour;
import com.example.utils.NumberGenerator;

public class TreeFactory implements ModelFactory{

    private final BranchParams branch_params;
    private final TrunkParams trunk_params;
    private static final double LEFT_MIN = 40;
    private static final double RIGHT_MAX = 50;
    private static final double GROUND_LEVEL = 0;
    // Potentially have it load startup renders from a config file later
    private static final int NUM_STARTUP_RENDERS = 0;

    private final TileProvider trunk_tile_provider;
    private final TileProvider branch_tile_provider;

    public TreeFactory(BranchParams branch_params, TrunkParams trunk_params)
    {
        this.branch_params = branch_params;
        this.trunk_params = trunk_params;
        final AngleMap trunk_char_map = new AngleMap();

        // --- VERTICAL (0 degrees) ---
        // For the Trunk
        // Added '!' and '1' for variation, ':' for texture
        trunk_char_map.put(
            0.0,
            new ArrayList<>(List.of('|', '[', ']', 'I', '!', '1', ':'))
        );

        // --- VERY STEEP RIGHT (~11 to 15 degrees) ---
        // Parentheses and curly braces look like a vertical line bending slightly
        trunk_char_map.put(
            Math.PI/16, // or Math.PI/12
            new ArrayList<>(List.of(')', '}', 'l', 'f'))
        );

        // --- VERY STEEP LEFT (~-11 to -15 degrees) ---
        trunk_char_map.put(
            -Math.PI/16, 
            new ArrayList<>(List.of('(', '{', '1', 'S'))
        );

        // --- DIAGONAL RIGHT (22.5 to 45 degrees) ---
        trunk_char_map.put(
            Math.PI/8, // 22.5 deg
            new ArrayList<>(List.of('/', '7', 'd', 'P'))
        );

        trunk_char_map.put(
            Math.PI/4, // 45 deg
            new ArrayList<>(List.of('/', 'F', '7'))
        );

        // --- DIAGONAL LEFT (-22.5 to -45 degrees) ---
        trunk_char_map.put(
            -Math.PI/8, // -22.5 deg
            new ArrayList<>(List.of('\\', 'J', 'L')) // Escaped backslash
        );

        trunk_char_map.put(
            -Math.PI/4, // -45 deg
            new ArrayList<>(List.of('\\', 'v', 'Y'))
        );

        // --- LOW ANGLE / SIDEWAYS RIGHT (~60 to 90 degrees) ---
        // Tilde ~ is great for gnarly horizontal branches
        trunk_char_map.put(
            Math.PI/3, // 60 deg (Steep horizontal)
            new ArrayList<>(List.of('/', ',', '>', 'r'))
        );

        trunk_char_map.put(
            Math.PI/2, // 90 deg (Pure East)
            new ArrayList<>(List.of('-', '_', '~', '=', '>'))
        );

        // --- LOW ANGLE / SIDEWAYS LEFT (~-60 to -90 degrees) ---
        trunk_char_map.put(
            -Math.PI/3, // -60 deg
            new ArrayList<>(List.of('\\', '`', '<'))
        );

        trunk_char_map.put(
            -Math.PI/2, // -90 deg (Pure West)
            new ArrayList<>(List.of('-', '_', '~', '=', '<'))
        );
        
        this.trunk_tile_provider = new TileProvider(
            4,
            5,
            Colour.BROWN_LIGHT,
            trunk_char_map
        );

        this.branch_tile_provider = new TileProvider(
            0, 
            1,
            Colour.BROWN_LIGHT,
            trunk_char_map
        );

        

        //final AngleMap branch_char_map = new AngleMap();
        
    }

    @Override
    public LineBasedRenderable getRenderable()
    {
        // Potential for a treetype in the future
        final Coord start_location = new Coord(NumberGenerator.getRandomNumber(LEFT_MIN, RIGHT_MAX), GROUND_LEVEL);
        final BranchPropogator propogator = new BranchPropogator(branch_params, branch_tile_provider);
        final BranchFactory branch_factory = new BranchFactory(propogator);
        final Trunk trunk = new Trunk(trunk_params, start_location, branch_factory, trunk_tile_provider);
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
