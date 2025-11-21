package com.example.game_board;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class Board {
    Map<Integer, ArrayList<Tile>> tileMapArrayList = new HashMap<>();

    public void addTile(int x, int y, Tile tile)
    {
        if (!tileMapArrayList.containsKey(y))
        {
            tileMapArrayList.put(y, new ArrayList<Tile>(100)); //arbitary number can increase later
        }
        ArrayList<Tile> array_list = tileMapArrayList.get(y);
        while (x > array_list.size()-1)
        {
            array_list.ensureCapacity( (int) Math.round(array_list.size()*1.5));
        }
    }
}
