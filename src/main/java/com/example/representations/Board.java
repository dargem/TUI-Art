package com.example.representations;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Board {
    final Map<Integer, ArrayList<Tile>> tileMapArrayList = new HashMap<>();
    private final int ARRAY_BASE_SIZE = 100;

    public ArrayList<Tile> getRow(int y)
    {
        return tileMapArrayList.get(y);
    }

    public void addTile(int x, int y, Tile tile)
    {
        if (!tileMapArrayList.containsKey(y))
        {
            tileMapArrayList.put(y, new ArrayList<>(ARRAY_BASE_SIZE)); //arbitary number can increase later
        }

        ArrayList<Tile> array_list = tileMapArrayList.get(y);

        while (array_list.size() <= x) 
        {
            array_list.add(null);
        }
        
        // wannabe walrus operator
        final Tile existing_tile;
        if ((existing_tile = array_list.get(x)) == null || existing_tile.checkOverwritable(tile.getZScore()))
        {
            array_list.add(x, tile);
        }
    }
}
