package com.example.rendering;
import java.util.AbstractMap;
import java.util.Map.Entry;

import com.example.representations.AngleTreeMap;
import com.example.representations.CharacterRetriever;
import com.example.representations.Tile;
import com.example.utils.Colour;
import com.example.utils.NumberGenerator;

public class TileProvider 
{
    /**
     * A class for providing tiles to the renderer
     */
    private final int minimum_z_score;
    private final int maximum_z_score;
    private final Colour primary_colour;
    private Colour background_colour;
    // to allow some variation in outputted colours
    private double colour_blend = 0; 

    private final AngleTreeMap angle_char_map;
    private Entry<Double, CharacterRetriever> retriever_cache =  new AbstractMap.SimpleEntry<>(Double.NEGATIVE_INFINITY, null);

    // Ideally want some complex angle based 

    public TileProvider(int minimum_z_score, int maximum_z_score, Colour primary_colour, AngleTreeMap angle_char_map)
    {
        this.minimum_z_score = minimum_z_score;
        this.maximum_z_score = maximum_z_score;
        this.primary_colour = primary_colour;
        this.angle_char_map = angle_char_map;
    }

    public Tile getTile(double angle)
    {
        if (retriever_cache.getKey() != angle)
        {
            retriever_cache = new AbstractMap.SimpleEntry<>(angle, angle_char_map.getCharacterRetriever(angle));
        }

        char character = retriever_cache.getValue().getCharacter();

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
