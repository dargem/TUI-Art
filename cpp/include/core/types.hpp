#pragma once
#include <cstdint>

namespace tui {

struct Colour {
    uint8_t r, g, b;

    bool operator==(const Colour& other) const = default;
};

struct Style {
    Colour fg;
    Colour bg;
    bool bold = false;
 
    bool operator==(const Style& other) const = default;
};

struct Cell {
    char character = ' ';
    Style style;

    bool operator==(const Cell& other) const = default;
};

// Viewer of the screen
struct Camera {
    int x;
    int y;
};

}
