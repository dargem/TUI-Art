#pragma once
#include <algorithm>
#include <cstddef>
#include <cstdint>

namespace types {

struct RGB {
    uint8_t r, g, b;

    bool operator==(const RGB& other) const = default;
};

struct Light {
    RGB rgb{};
    uint8_t alpha{};

    void blend(Light other) {
        // use an additive blend of both lights
        int my_r = rgb.r * alpha / 255;
        int my_g = rgb.g * alpha / 255;
        int my_b = rgb.b * alpha / 255;

        int other_r = other.rgb.r * other.alpha / 255;
        int other_g = other.rgb.g * other.alpha / 255;
        int other_b = other.rgb.b * other.alpha / 255;

        // Add together RGB's capping at 255
        rgb.r = static_cast<uint8_t>(std::min(255, my_r + other_r));
        rgb.g = static_cast<uint8_t>(std::min(255, my_g + other_g));
        rgb.b = static_cast<uint8_t>(std::min(255, my_b + other_b));

        // combined alpha
        alpha = static_cast<uint8_t>(std::min(255, alpha + other.alpha));
    }
};

struct Style {
    RGB fg;
    RGB bg;
    uint8_t alpha;

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
