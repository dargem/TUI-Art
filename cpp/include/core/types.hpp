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

// A shadow is used for negative lighting
struct Shadow {};

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
