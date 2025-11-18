package com.example.game_board;

public class Board {
    private Tile[][] tile_array;
    final private int array_width;
    private int access_width;

    public Board(final int needed_width)
    {
        this.array_width = needed_width;
        this.tile_array = new Tile[200][needed_width];
        // 200 rows, current width wide
    }

    public boolean checkWidthSame(final int needed_width)
    {
        return this.access_width == needed_width;
    }

    public void resizeArray(final int needed_width)
    {
        this.access_width = Math.min(this.array_width, needed_width);
    }



}
