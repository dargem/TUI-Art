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

/*
struct RGB {
    uint8_t r, g, b;

    bool operator==(const RGB& other) const = default;
};
*/

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
        rgbPremultiplied.r = rgb.r * alpha;
        rgbPremultiplied.g = rgb.g * alpha;
        rgbPremultiplied.b = rgb.b * alpha;
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

   private:
    RGB rgbPremultiplied{};
    uint8_t alpha{};
};

// A shadow is used for negative lighting, basically a brightness multiplier
struct Shadow {
    uint8_t brightness{255};  // brightness, 255 is no change, 0 is absolute black
};

// A tone shift is used to push a colour towards another one
// E.g. Make everything more dark or give a light orange shift
// Works not additively but through multiplication (lerp)
struct ToneShift {
    RGB tone{};               // The tone for moving towards
    uint8_t shiftStrength{};  // 255 strength would set them as the same, 0 would do nothing

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
    };
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
