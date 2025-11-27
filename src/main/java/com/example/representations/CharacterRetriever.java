package com.example.representations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;

import com.example.utils.DoublePair;
/**
 * A class containg a probabililty distribution for characters
 * Outputs characters based on this probability distribution
 * Characters with less angle variation are more likely to be picked
 */
public class CharacterRetriever 
{
    private final TreeMap<Double, ArrayList<Character>> character_probabilities = new TreeMap<>();

    public CharacterRetriever(TreeMap<Double, ArrayList<Character>> tree_char_map, double search_angle)
    {
        Set<Double> angles = tree_char_map.keySet();

        // pairs of the offset angle with the original angle
        final DoublePair[] adjusted_angles = new DoublePair[angles.size()];

        int i = 0;
        double total_offset = 0;
        for (Double angle : angles)
        {
            // option of adding a scalar like ^1.2 to make it prioritise closer angles more-so
            final double offset = Math.abs(angle - search_angle);
            total_offset += offset;
            adjusted_angles[i] = new DoublePair(offset, angle);
            ++i;
        }

        // sort it by offset magnitude
        Arrays.sort(adjusted_angles, Comparator.comparingDouble(p -> p.offset_magnitude()));

        double accumulated_percent = 0;
        for (DoublePair double_pair : adjusted_angles)
        {
            // To construct a discrete CDF effectively
            // as accessing will be done through finding closest ceiling key of input
            final double closeness_percent = 1 - double_pair.offset_magnitude() / total_offset;
            accumulated_percent += closeness_percent;
            character_probabilities.put(
                accumulated_percent, 
                tree_char_map.get(double_pair.original_angle()
            ));
        }

        // because floating point errors could lead to a key slightly less than 1
        // manually put in a key of 1.0 using the last element as CDF sums to 1
        character_probabilities.put(
            1.0, 
            tree_char_map.get(adjusted_angles[adjusted_angles.length - 1].original_angle())
        );
    }
}
