package com.example.rendering.tile_providing;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.example.utils.NumberGenerator;
/**
 * A class containg a probabililty distribution for characters
 * Outputs characters based on this probability distribution
 * Characters with less angle variation are more likely to be picked
 */
public class CharacterRetriever 
{
    private final TreeMap<Double, ArrayList<Character>> character_probabilities = new TreeMap<>();
    // Increase to pronounciate the directional strength
    // Decrease to make direction more generalised, bugs out with too high a number
    private final double CLOSENESS_SCALAR = 1.2;

    public CharacterRetriever(TreeMap<Double, ArrayList<Character>> tree_char_map, double search_angle)
    {
        if (tree_char_map.isEmpty())
        {
            throw new RuntimeException("Character retriever initialised with empty map");
        }

        // Store. closeness stores in a treemap where keys are OG angles
        TreeMap<Double, Double> closeness_scores = new TreeMap<>();
        double total_closeness_score = 0;

        // Calculate the closeness and total score
        for (Double angle : tree_char_map.keySet())
        {
            double offset = Math.abs(angle - search_angle);
            // guard against a miraculous division by 0 I'm sure will happen
            // Also guard against extremely small offsets that cause underflow when powered
            offset = Math.max(offset, 1e-9);
            offset = Math.pow(offset, CLOSENESS_SCALAR);

            final double score = 1.0 / offset;
            closeness_scores.put(angle, score);
            total_closeness_score += score;
        }

        // Build effectively a CDF but its a treemap
        // where indexes are doubles starting at 0 to 1
        double accumulated_probability = 0;
        for (Entry<Double, Double> entry : closeness_scores.entrySet())
        {
            final double original_angle = entry.getKey();
            final double score = entry.getValue();

            // then normalise the score to get the actual ptob
            final double probabililty = score / total_closeness_score;
            accumulated_probability += probabililty;

            character_probabilities.put(accumulated_probability, tree_char_map.get(original_angle));
        }

        // Make sure it sums to exactly 1 as floating point accuracies exist
        // Without it getting the ceil entry could miss with bad luck
        if (character_probabilities.lastKey() < 1.0)
        {
            ArrayList<Character> last_chars = character_probabilities.lastEntry().getValue();
            character_probabilities.put(1.0, last_chars);
        }
    }

    /**
     * Gets a random character according to its initialised angle
     * "CDF" is weighted so that close characters are preferred
     * @return a random character from its initialised distribution
     */
    public Character getCharacter()
    {
        final double percent = NumberGenerator.getRandomNumber();
        final ArrayList<Character> characters = character_probabilities.ceilingEntry(percent).getValue();
        return characters.get(NumberGenerator.getIntNumber(0, characters.size()));
    }
}
