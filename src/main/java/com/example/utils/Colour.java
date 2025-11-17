package com.example.utils;

/**
 * An enum for output colours
 */
public enum Colour {
    // GENERICS
    BLACK("30"),
    RED("31"),
    GREEN("32"),
    YELLOW("33"),
    BLUE("34"),
    MAGENTA("35"),
    CYAN("36"),
    WHITE("37"),

    // BRIGHTS
    BRIGHT_BLACK("90"),
    BRIGHT_RED("91"),
    BRIGHT_GREEN("92"),
    BRIGHT_YELLOW("93"),
    BRIGHT_BLUE("94"),
    BRIGHT_MAGENTA("95"),
    BRIGHT_CYAN("96"),
    BRIGHT_WHITE("97");

    // The ANSI code number
    private final String code;

    Colour(String code) {
        this.code = code;
    }

    /**
     * Used to get the ANSI escape sequence for foreground variation
     * @return The escape sequence for the foreground colour
     */
    public String foreground() {
        return "\u001b[" + this.code + "m";
    }

    /**
     * Returns the full ANSI escape sequence for background variation
     * Does this by adding 10 to the standard codes
     * @return The background color escape sequence.
     */
    public String background() {
        // We parse the code as an integer, add 10 (or 60 for bright codes),
        // and convert it back to a string for the background sequence.
        int bgCode = Integer.parseInt(this.code);
        if (bgCode >= 30 && bgCode <= 37) {
            bgCode += 10; // Standard colors: 3x -> 4x
        } else if (bgCode >= 90 && bgCode <= 97) {
            bgCode += 10; // Bright colors: 9x -> 10x
        }
        return "\u001b[" + bgCode + "m";
    }
}