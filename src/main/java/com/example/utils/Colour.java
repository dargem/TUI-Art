package com.example.utils;

/**
 * An enum for output colours
 * Supports both standard 16-color ANSI and 256-color extended palette
 */

public enum Colour {
    // GENERICS (16-color)
    BLACK("30", false),
    RED("31", false),
    GREEN("32", false),
    YELLOW("33", false),
    BLUE("34", false),
    MAGENTA("35", false),
    CYAN("36", false),
    WHITE("37", false),

    // BRIGHTS (16-color)
    BRIGHT_BLACK("90", false),
    BRIGHT_RED("91", false),
    BRIGHT_GREEN("92", false),
    BRIGHT_YELLOW("93", false),
    BRIGHT_BLUE("94", false),
    BRIGHT_MAGENTA("95", false),
    BRIGHT_CYAN("96", false),
    BRIGHT_WHITE("97", false),

    // 256-COLOR BROWNS
    BROWN("94", true),           // Dark brown
    BROWN_LIGHT("130", true),    // Medium brown
    BROWN_ORANGE("136", true),   // Orange-brown
    BROWN_TAN("137", true),      // Tan/light brown
    
    // 256-COLOR GREENS (useful for foliage)
    GREEN_DARK("22", true),      // Dark green
    GREEN_FOREST("28", true),    // Forest green
    GREEN_LIME("154", true),     // Lime green
    
    // 256-COLOR EARTH TONES
    ORANGE("208", true),         // Bright orange
    ORANGE_DARK("166", true);    // Dark orange

    // The ANSI code number or 256-color index
    private final String code;
    // Whether this uses 256-color mode
    private final boolean is256Color;

    Colour(String code, boolean is256Color) {
        this.code = code;
        this.is256Color = is256Color;
    }

    /**
     * Used to get the ANSI escape sequence for foreground variation
     * @return The escape sequence for the foreground colour
     */
    public String foreground() {
        if (is256Color) {
            return "\u001b[38;5;" + this.code + "m";
        }
        return "\u001b[" + this.code + "m";
    }

    /**
     * Returns the full ANSI escape sequence for background variation
     * @return The background color escape sequence.
     */
    public String background() {
        if (is256Color) {
            return "\u001b[48;5;" + this.code + "m";
        }
        
        // For 16-color mode, add 10 to convert foreground to background
        int bgCode = Integer.parseInt(this.code);
        if (bgCode >= 30 && bgCode <= 37) {
            bgCode += 10; // Standard colors: 3x -> 4x
        } else if (bgCode >= 90 && bgCode <= 97) {
            bgCode += 10; // Bright colors: 9x -> 10x
        }
        return "\u001b[" + bgCode + "m";
    }
}