package com.example.utils;
import java.util.Random;

public final class NumberGenerator 
{
    
    private static final Random random = new Random();

    private NumberGenerator() {}

    /**
     * @return a double between 0 and 1
     */
    public static double getRandomNumber()
    {
        return random.nextDouble();
    }

    /**
     * Samples from a uniform distribution between two points
     * @return a double between the min and max
     */
    public static double getRandomNumber(double min, double max)
    {
        double random_number = random.nextDouble();
        // rescale the number for our range
        final double range = max - min;
        random_number *= range;
        random_number += min;
        return random_number;
    }

    /**
     * Generates an int value between the specified min (inclusive)
     * And the specified max (exclusive)
     * @param min the inclusive minimum value
     * @param max the exclusive maximum value
     * @return a random int between min and max
     */
    public static int getIntNumber(int min, int max)
    {
        return random.nextInt(min, max);
    }
}
