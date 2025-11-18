package com.example.utils;
import java.util.Random;

public final class NumberGenerator {
    
    private static final Random random = new Random();

    private NumberGenerator() {}

    public static double getRandomNumber()
    {
        return random.nextDouble();
    }

    public static double getRandomNumber(double min, double max)
    {
        double random_number = random.nextDouble();
        // rescale the number for our range
        final double range = max - min;
        random_number *= range;
        random_number += min;
        return random_number;
    }
}
