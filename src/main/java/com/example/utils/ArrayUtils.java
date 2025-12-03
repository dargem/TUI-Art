package com.example.utils;

/**
 * Utility class for some 
 */
public class ArrayUtils 
{
    // private class to stop instantiation
    private ArrayUtils() {}

    /**
     * Get an element from an array with additional wrapping features
     * Negatives wrap to positives, e.g. -1 getting the last element
     * Exceeding positives wrap to normals, e.g. index = 8 mod (length_array)
     * @param <T> type the array contains and naturally returns
     * @param items an array of items to search
     * @param index the index to retrieve an item
     * @return an element from the array
     */
    public static <T> T getItem(T[] items, int index)
    {
        if (index >= items.length)
        {
            return items[index % items.length];
        }
        else if (index < 0)
        {
            return items[index % items.length + items.length];
        }
        return items[index];
    }
}
