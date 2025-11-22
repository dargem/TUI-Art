package com.example.rendering;
import com.example.utils.Colour;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.example.representations.Tile;
import com.example.utils.NumberGenerator;

public class TileProvider {
    /**
     * A class for providing tiles to the renderer
     */

    final int minimum_z_score;
    final int maximum_z_score;
    final Colour primary_colour;
    Colour background_colour;
    // to allow some variation in outputted colours
    double colour_blend = 0; 
    final ArrayList<Character> possible_characters;

    final TreeMap<Double, ArrayList<Character>> tree_char_map = new TreeMap<>();

    // Ideally want some complex angle based 

    public TileProvider(int minimum_z_score, int maximum_z_score, Colour primary_colour, ArrayList<Character> possible_characters)
    {
        this.minimum_z_score = minimum_z_score;
        this.maximum_z_score = maximum_z_score;
        this.primary_colour = primary_colour;
        this.possible_characters = possible_characters;
    }

    public void addCharacter(ArrayList<Character> output_char, double angle)
    {
        tree_char_map.put(angle, output_char);
    }

    public Tile getTile(double angle)
    {
        final Entry<Double, ArrayList<Character>> floor_entry = tree_char_map.floorEntry(angle);
        final Entry<Double, ArrayList<Character>> ceil_entry = tree_char_map.ceilingEntry(angle);
        
        final double displacement_floor = Math.abs(floor_entry.getKey() - angle);
        final double displacement_ceil = Math.abs(ceil_entry.getKey() - angle);

        // Choose the character arraylist with least angle discrepancy
        // Then randomly choose a character from the array list!
        char character;
        if (displacement_ceil < displacement_floor)
        {
            int chosen_index = NumberGenerator.getIntNumber(0, ceil_entry.getValue().size()-1);
            character = ceil_entry.getValue().get(chosen_index);
        }
        else
        {
            int chosen_index = NumberGenerator.getIntNumber(0, floor_entry.getValue().size()-1);
            character = floor_entry.getValue().get(chosen_index);
        }

        if (background_colour == null)
        {
            background_colour = Colour.BLACK;
        }

        // think about how to implement colour blend later, breaks my enum rn
        double z_score = NumberGenerator.getRandomNumber(minimum_z_score, maximum_z_score);
        return new Tile(primary_colour, background_colour, character, z_score);
    }

    public void setColourBlend(double blend_amount)
    {
        this.colour_blend = blend_amount;
    }

    public void setBackgroundColour(Colour background_colour)
    {
        this.background_colour = background_colour;
    }
}
