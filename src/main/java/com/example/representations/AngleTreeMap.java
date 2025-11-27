package com.example.representations;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.utils.NumberGenerator;

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
    public Character getCharacter(double bearing)
    {
        if (bearing > Math.PI/2 && bearing <= Math.PI)
        {
            return searchAngle(bearing - Math.PI);
        }
        
        if (bearing < -Math.PI/2 && bearing >= -Math.PI)
        {
            return searchAngle(bearing + Math.PI);
        }

        return searchAngle(bearing);
    }

    /**
     * Helper function for searching angles
     * Finds angle with lowest absolute angle discrepancy
     * Returns a random character from the arraylist matched with it
     * @param bearing angle to search with
     * @return A random character
     */
    private Character searchAngle(double bearing)
    {
        final Entry<Double, ArrayList<Character>> floor_entry = tree_char_map.floorEntry(bearing);
        final Entry<Double, ArrayList<Character>> ceil_entry = tree_char_map.ceilingEntry(bearing);

        if (floor_entry == null)
        {
            return getRandomChar(ceil_entry.getValue());
        }
        else if (ceil_entry == null)
        {
            return getRandomChar(floor_entry.getValue());
        }

        final double floor_dif = Math.abs(floor_entry.getKey() - bearing);
        final double ceil_dif = Math.abs(ceil_entry.getKey() - bearing);

        final ArrayList<Character> char_list;

        if (floor_dif < ceil_dif)
        {
            char_list = floor_entry.getValue();
        }
        else
        {
            char_list = ceil_entry.getValue();
        }

        return getRandomChar(char_list);
    }

    /**
     * Helper function for getting random member of array
     * @param characters array of characters
     * @return a character randomly chosen
     */
    private char getRandomChar(ArrayList<Character> characters)
    {
        return characters.get(NumberGenerator.getIntNumber(0, characters.size()-1));
    }  
}
