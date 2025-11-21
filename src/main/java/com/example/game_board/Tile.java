package com.example.game_board;
import java.awt.Color;

import com.example.utils.Colour;

public class Tile 
{
    private Colour foreground;
    private Colour background;
    private char character;

    public Tile()
    {
        // holder for now
        foreground = Colour.BLUE;
        background = Colour.BLACK;
        character = '&';
    }
    
    public Tile(final Colour foreground, final Colour background, final char character)
    {
        init(foreground, background, character);
    }

    public void rewriteTile(final Colour foreground, final Colour background, final char character)
    {
        init(foreground, background, character);
    }

    private void init(final Colour foreground, final Colour background, final char character) {
        this.foreground = foreground;
        this.background = background;
        this.character = character;
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
