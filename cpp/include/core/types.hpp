#pragma once
#include <algorithm>
#include <cstddef>
#include <cstdint>

namespace types {

struct RGB {
    uint8_t r, g, b;

    bool operator==(const RGB& other) const = default;
};

struct Shade {
    // rgb is stored premultiplied by alpha for quick blends
    Shade(RGB rgb, uint8_t alpha) {
        rgbPremultiplied.r = rgb.r * alpha;
        rgbPremultiplied.g = rgb.g * alpha;
        rgbPremultiplied.b = rgb.b * alpha;
    }

    // default construct everything to 0
    Shade() : rgbPremultiplied{}, alpha{} {}

    void blend(Shade other) {
        // Simple saturated addition
        rgbPremultiplied.r =
            static_cast<uint8_t>(std::min(255, int(rgbPremultiplied.r) + other.rgbPremultiplied.r));
        rgbPremultiplied.g =
            static_cast<uint8_t>(std::min(255, int(rgbPremultiplied.g) + other.rgbPremultiplied.g));
        rgbPremultiplied.b =
            static_cast<uint8_t>(std::min(255, int(rgbPremultiplied.b) + other.rgbPremultiplied.b));
        alpha = static_cast<uint8_t>(std::min(255, int(alpha) + other.alpha));
    }

   private:
    RGB rgbPremultiplied{};
    uint8_t alpha{};
};

struct Style {
    RGB fg;
    RGB bg;
    uint8_t fg_alpha;

    bool operator==(const Style& other) const = default;
};

struct Cell {
    Style style;
    char character = ' ';

    bool operator==(const Cell& other) const = default;
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
