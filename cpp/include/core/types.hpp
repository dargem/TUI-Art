#pragma once
#include <algorithm>
#include <cstddef>
#include <cstdint>
#include <execution>
#include <ranges>

namespace types {

union RGB {
    struct {
        uint8_t r, g, b;
    };
    std::array<uint8_t, 3> colours;

    // bool operator==(const RGB& other) const = default;
    bool operator==(const RGB& other) const {
        return (r == other.r) && (g == other.g) && (b == other.b);
    }
};

struct Style {
    RGB fg;
    RGB bg;
    bool bold{false};

    bool operator==(const Style& other) const = default;
};

struct Cell {
    Style style;
    char character = ' ';

    bool operator==(const Cell& other) const = default;
};

// A shade is used for positive lighting, think adding light sources on top of each other.
struct Shade {
    // rgb is stored premultiplied by alpha
    Shade(RGB rgb, uint8_t alpha) {
        rgbPremultiplied.r = rgb.r * double(alpha) / 255 + 0.5;
        rgbPremultiplied.g = rgb.g * double(alpha) / 255 + 0.5;
        rgbPremultiplied.b = rgb.b * double(alpha) / 255 + 0.5;
    }

    // default construct everything to 0
    Shade() : rgbPremultiplied{}, alpha{} {}

    // blend another shade together with this shade
    // this is a process where order of addition doesn't matter
    void blend(Shade other) {
        // Simple saturated addition
        for (size_t i{}; i < rgbPremultiplied.colours.size(); ++i) {
            rgbPremultiplied.colours[i] = static_cast<uint8_t>(std::min(
                255, int(rgbPremultiplied.colours[i]) + other.rgbPremultiplied.colours[i]));
        }
        alpha = static_cast<uint8_t>(std::min(255, int(alpha) + other.alpha));
    }

    // apply this shade onto a cell
    void applyOn(Cell& cell) const {
        // Simple saturated addition
        for (size_t i{}; i < rgbPremultiplied.colours.size(); ++i) {
            cell.style.bg.colours[i] = static_cast<uint8_t>(
                std::min(255, int(rgbPremultiplied.colours[i]) + cell.style.bg.colours[i]));
            cell.style.fg.colours[i] = static_cast<uint8_t>(
                std::min(255, int(rgbPremultiplied.colours[i]) + cell.style.fg.colours[i]));
        }
    }

    // shouldn't really be called, shade is only for blending or applying on cells
    RGB getPremultipliedRGB() const { return rgbPremultiplied; }

   private:
    RGB rgbPremultiplied{};
    uint8_t alpha{};
};

// A tone shift is used to push a colour towards another one
// E.g. Make everything more dark or give a light orange shift
// Works not additively but through multiplication (lerp)
struct ToneShift {
    RGB tone{};               // The tone for moving towards
    uint8_t shiftStrength{};  // 255 strength would set them as the same, 0 would do nothing

    // blend two tone shifts together
    void blend(ToneShift other) {
        if (other.shiftStrength == 0) [[unlikely]] {
            return;
        }

        // try and find and intermediate of the tone based on the strength of each shift
        double neededShift =
            other.shiftStrength / static_cast<double>(shiftStrength + other.shiftStrength);
        for (size_t i{}; i < tone.colours.size(); ++i) {
            tone.colours[i] +=
                neededShift * (int(other.tone.colours[i]) - int(tone.colours[i])) + 0.5;
        }
        shiftStrength = 0.5 + 255 * (1 - (1 - double(shiftStrength) / 255) *
                                             (1 - double(other.shiftStrength) / 255));
    }

    void applyOn(Cell& cell) const {
        for (size_t i{}; i < tone.colours.size(); ++i) {
            int w = tone.colours[i];  // wanted colour

            // do lerp to get the new cell colour, from the old shifted towards the wanted. Add 127
            // so that outputs are rounded to closest int effectively not just up/down.
            int c_1 = cell.style.fg.colours[i];  // current foreground colour
            cell.style.fg.colours[i] =
                (c_1 * (255 - shiftStrength) + w * shiftStrength + 127) / 255;

            int c_2 = cell.style.bg.colours[i];  // current background colour
            cell.style.bg.colours[i] =
                (c_2 * (255 - shiftStrength) + w * shiftStrength + 127) / 255;
        }
    }
};

// Viewer of the screen
struct Camera {
    int x;
    int y;
};

struct GridLocation {
    size_t x;
    size_t y;

    bool operator==(const GridLocation& other) const = default;
};

}  // namespace types
