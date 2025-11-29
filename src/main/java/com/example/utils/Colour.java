package com.example.utils;

/**
 * Colour utility class supporting:
 * - legacy 16/256 ANSI colours (kept as static constants for compatibility)
 * - truecolour (24-bit RGB) via factory methods
 * - blending between two colours (returns a new truecolour Colour)
 *
 * The previous implementation was an enum of ANSI indices. That made it
 * impossible to create dynamic RGB colours or blended variants. This class
 * keeps the same named constants (e.g. Colour.BLACK) while adding dynamic
 * truecolour support and a blending helper.
 */
public final class Colour {

    // --- Predefined constants (keeps previous API names) ---
    public static final Colour BLACK = new Colour("30", false, 0,0,0);
    public static final Colour RED = new Colour("31", false, 128,0,0);
    public static final Colour GREEN = new Colour("32", false, 0,128,0);
    public static final Colour YELLOW = new Colour("33", false, 128,128,0);
    public static final Colour BLUE = new Colour("34", false, 0,0,128);
    public static final Colour MAGENTA = new Colour("35", false, 128,0,128);
    public static final Colour CYAN = new Colour("36", false, 0,128,128);
    public static final Colour WHITE = new Colour("37", false, 192,192,192);

    public static final Colour BRIGHT_BLACK = new Colour("90", false, 128,128,128);
    public static final Colour BRIGHT_RED = new Colour("91", false, 255,0,0);
    public static final Colour BRIGHT_GREEN = new Colour("92", false, 0,255,0);
    public static final Colour BRIGHT_YELLOW = new Colour("93", false, 255,255,0);
    public static final Colour BRIGHT_BLUE = new Colour("94", false, 0,0,255);
    public static final Colour BRIGHT_MAGENTA = new Colour("95", false, 255,0,255);
    public static final Colour BRIGHT_CYAN = new Colour("96", false, 0,255,255);
    public static final Colour BRIGHT_WHITE = new Colour("97", false, 255,255,255);

    // Some previously defined 256-color named entries (kept for compatibility)
    public static final Colour BROWN = new Colour("94", true, 101,67,33);
    public static final Colour BROWN_LIGHT = new Colour("130", true, 150,75,0);
    public static final Colour BROWN_ORANGE = new Colour("136", true, 205,102,0);
    public static final Colour BROWN_TAN = new Colour("137", true, 210,180,140);

    public static final Colour GREEN_DARK = new Colour("22", true, 0,100,0);
    public static final Colour GREEN_FOREST = new Colour("28", true, 34,139,34);
    public static final Colour GREEN_LIME = new Colour("154", true, 173,255,47);

    public static final Colour ORANGE = new Colour("208", true, 255,140,0);
    public static final Colour ORANGE_DARK = new Colour("166", true, 255,120,0);

    // Internal: either this uses an ANSI/256 code OR an explicit RGB value
    private final String code; // numeric string for legacy 16/256 codes
    private final boolean is256Color;
    private final Integer r, g, b; // null when not defined as truecolour

    private Colour(String code, boolean is256Color, Integer r, Integer g, Integer b) {
        this.code = code;
        this.is256Color = is256Color;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    // Factory to create an explicit truecolour (24-bit) Colour
    public static Colour fromRGB(int r, int g, int b) {
        r = clamp(r, 0, 255);
        g = clamp(g, 0, 255);
        b = clamp(b, 0, 255);
        return new Colour(null, false, r, g, b);
    }

    // Helper for clamping
    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * Returns true if this Colour has a concrete RGB value (truecolour)
     */
    public boolean isTrueColour() {
        return r != null && g != null && b != null;
    }

    /**
     * Get ANSI escape sequence for foreground. If this Colour is a truecolour
     * (has RGB) the 24-bit sequence is produced. Otherwise fallback to 256/16
     * code behaviour for compatibility.
     */
    public String foreground() {
        if (isTrueColour()) {
            return "\u001b[38;2;" + r + ";" + g + ";" + b + "m";
        }
        if (is256Color) {
            return "\u001b[38;5;" + this.code + "m";
        }
        return "\u001b[" + this.code + "m";
    }

    /**
     * Background escape sequence. For truecolour uses 48;2;R;G;B
     */
    public String background() {
        if (isTrueColour()) {
            return "\u001b[48;2;" + r + ";" + g + ";" + b + "m";
        }
        if (is256Color) {
            return "\u001b[48;5;" + this.code + "m";
        }

        int bgCode = Integer.parseInt(this.code);
        if (bgCode >= 30 && bgCode <= 37) {
            bgCode += 10; // e.g. 30 -> 40
        } else if (bgCode >= 90 && bgCode <= 97) {
            bgCode += 10; // 90 -> 100
        }
        return "\u001b[" + bgCode + "m";
    }

    /**
     * Blend this colour with another colour and return a new truecolour Colour.
     * t=0 => this colour; t=1 => other colour.
     */
    public Colour blendWith(Colour other, double t) {
        t = Math.max(0.0, Math.min(1.0, t));
        int[] a = this.toRGB();
        int[] b = other.toRGB();
        int nr = (int) Math.round(a[0] * (1 - t) + b[0] * t);
        int ng = (int) Math.round(a[1] * (1 - t) + b[1] * t);
        int nb = (int) Math.round(a[2] * (1 - t) + b[2] * t);
        return Colour.fromRGB(nr, ng, nb);
    }

    /**
     * Convert this Colour to an RGB triple. If it is already truecolour, return
     * its rgb values. Otherwise attempts to map 16/256 indices to approximate
     * RGB values.
     */
    public int[] toRGB() {
        if (isTrueColour()) {
            return new int[]{r, g, b};
        }
        // Try parse numeric code
        try {
            int codeNum = Integer.parseInt(this.code);
            // 0-15: standard/bright colours - map to typical values
            if (codeNum >= 30 && codeNum <= 37) {
                // foreground 30-37 mapping to 0-7 index
                return ansiIndexToRGB(codeNum - 30);
            }
            if (codeNum >= 90 && codeNum <= 97) {
                return ansiIndexToRGB(8 + (codeNum - 90));
            }
            if (is256Color) {
                int idx = codeNum;
                if (idx >= 0 && idx <= 255) {
                    return color256ToRGB(idx);
                }
            }
        } catch (NumberFormatException e) {
            // fall through
        }
        // Fallback to black
        return new int[]{0,0,0};
    }

    // Map basic ANSI 0-15 palette to approximate RGB
    private static int[] ansiIndexToRGB(int index) {
        switch (index) {
            case 0: return new int[]{0,0,0};
            case 1: return new int[]{128,0,0};
            case 2: return new int[]{0,128,0};
            case 3: return new int[]{128,128,0};
            case 4: return new int[]{0,0,128};
            case 5: return new int[]{128,0,128};
            case 6: return new int[]{0,128,128};
            case 7: return new int[]{192,192,192};
            case 8: return new int[]{128,128,128};
            case 9: return new int[]{255,0,0};
            case 10: return new int[]{0,255,0};
            case 11: return new int[]{255,255,0};
            case 12: return new int[]{0,0,255};
            case 13: return new int[]{255,0,255};
            case 14: return new int[]{0,255,255};
            case 15: return new int[]{255,255,255};
            default: return new int[]{0,0,0};
        }
    }

    // Convert a 256-color palette index to RGB. Handles 0-15 (system), 16-231 (6x6x6), 232-255 (grayscale)
    private static int[] color256ToRGB(int idx) {
        if (idx >= 0 && idx <= 15) {
            return ansiIndexToRGB(idx);
        }
        if (idx >= 16 && idx <= 231) {
            int i = idx - 16;
            int r = i / 36;
            int g = (i % 36) / 6;
            int b = i % 6;
            return new int[]{component6To255(r), component6To255(g), component6To255(b)};
        }
        if (idx >= 232 && idx <= 255) {
            int c = (idx - 232) * 10 + 8;
            return new int[]{c,c,c};
        }
        return new int[]{0,0,0};
    }

    private static int component6To255(int v) {
        // map 0..5 -> 0,95,135,175,215,255
        switch (v) {
            case 0: return 0;
            case 1: return 95;
            case 2: return 135;
            case 3: return 175;
            case 4: return 215;
            case 5: return 255;
            default: return 0;
        }
    }

    @Override
    public String toString() {
        if (isTrueColour()) {
            return String.format("RGB(%d,%d,%d)", r, g, b);
        }
        return is256Color ? ("ANSI256(" + code + ")") : ("ANSI(" + code + ")");
    }
}