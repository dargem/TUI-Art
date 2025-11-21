package com.example.game_board;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Board {
    final Map<Integer, ArrayList<Tile>> tileMapArrayList = new HashMap<>();
    private final int ARRAY_BASE_SIZE = 100;
    private final double ARRAY_RESIZE_FACTOR = 1.5;

    public void addTile(int x, int y, Tile tile)
    {
        if (!tileMapArrayList.containsKey(y))
        {
            tileMapArrayList.put(y, new ArrayList<>(ARRAY_BASE_SIZE)); //arbitary number can increase later
        }
        ArrayList<Tile> array_list = tileMapArrayList.get(y);
        while (x > array_list.size()-1)
        {
            // An incredibly scientific array resizer
            double wanted_size = array_list.size() * ARRAY_RESIZE_FACTOR;

            if (x > wanted_size)
                wanted_size = array_list.size() * ARRAY_RESIZE_FACTOR;
            
            array_list.ensureCapacity( (int) Math.round(wanted_size));
        }
    }
}
