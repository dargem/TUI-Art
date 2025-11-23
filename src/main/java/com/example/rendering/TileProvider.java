package com.example.rendering;
import com.example.utils.Colour;
import com.example.representations.AngleMap;
import com.example.representations.Tile;
import com.example.utils.NumberGenerator;

public class TileProvider 
{
    /**
     * A class for providing tiles to the renderer
     */
    final int minimum_z_score;
    final int maximum_z_score;
    final Colour primary_colour;
    Colour background_colour;
    // to allow some variation in outputted colours
    double colour_blend = 0; 

    final AngleMap angle_char_map;

    // Ideally want some complex angle based 

    public TileProvider(int minimum_z_score, int maximum_z_score, Colour primary_colour, AngleMap angle_char_map)
    {
        this.minimum_z_score = minimum_z_score;
        this.maximum_z_score = maximum_z_score;
        this.primary_colour = primary_colour;
        this.angle_char_map = angle_char_map;
    }

    public Tile getTile(double angle)
    {
        char character = angle_char_map.getCharacter(angle);

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
