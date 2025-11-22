package com.example.representations;
import java.awt.Color;

import com.example.utils.Colour;

public class Tile 
{
    private Colour foreground;
    private Colour background;
    private char character;
    private double z_score; // higher z score overwrites lower z score


    public Tile()
    {
        // holder for now
        foreground = Colour.GREEN;
        background = Colour.BLACK;
        character = '&';
    }
    
    public Tile(Colour foreground, Colour background, char character, double z_score)
    {
        init(foreground, background, character, z_score);
    }

    public void rewriteTile(final Colour foreground, final Colour background, final char character, double z_score)
    {
        init(foreground, background, character, z_score);
    }

    private void init(final Colour foreground, final Colour background, final char character, double z_score) {
        this.foreground = foreground;
        this.background = background;
        this.character = character;
        this.z_score = z_score;
    }

    public Colour getForeground()
    {
        return foreground;
    }

    public Colour getBackground()
    {
        return background;
    }

    public char getCharacter()
    {
        return character;
    }
}
