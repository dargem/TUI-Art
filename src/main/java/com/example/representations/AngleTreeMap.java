package com.example.representations;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * A custom modification of a tree map
 * Retrieves by closest bearing (w. potential rotation)
 * Inputted by bearing
 */
public class AngleTreeMap 
{
    final TreeMap<Double, ArrayList<Character>> tree_char_map = new TreeMap<>();

    /**
     * Puts an array list of characters into the map, keyed by value
     * Requires bearing to be between -Math.PI/2 rad and Math.PI/2 rad
     * This is as retrieval will automatically wrap around
     * @param bearing the bearing angle to correlate with the character
     * @param characters An array list of characters paired with this bearing
     */
    public void put(double bearing, ArrayList<Character> characters)
    {
        if (bearing < -Math.PI/2 || bearing > Math.PI/2)
        {
            throw new IllegalArgumentException("Angle bearing should be between -pi/2 and pi/2 radians to maintain search angle logic");
        }
        tree_char_map.put(bearing, characters);
    }

    /**
     * Adds a generic character
     * These are not angle specified
     * And have a chance of always occurring
     * @param character
     */
    public void putGeneric(Character character)
    {

    }

    /**
     * Searches up a character using nearest neighbour search of a bearing
     * Will wrap around for searches as e.g. -135 degrees equivalent to 45 degrees
     * Returns a random character from the angle matched with the entry
     * @param bearing The bearing inputted for search
     * @return A character from the list
     */
    public CharacterRetriever getCharacterRetriever(double bearing)
    {

        if (bearing > Math.PI/2 && bearing <= Math.PI)
        {
            return new CharacterRetriever(tree_char_map, bearing - Math.PI);
        }
        
        if (bearing < -Math.PI/2 && bearing >= -Math.PI)
        {
            return new CharacterRetriever(tree_char_map, bearing + Math.PI);
        }

        return new CharacterRetriever(tree_char_map, bearing);
    }
}
