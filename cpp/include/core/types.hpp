#pragma once
#include <cstdint>

namespace tui {

struct Colour {
    uint8_t r, g, b;

    inline bool operator!=(const Colour& other) const {
        return r != other.r ||
               g != other.g ||
               b != other.b;
    }
};

struct Style {
    Colour fg{255, 255, 255};
    Colour bg{0, 0, 0};
    bool bold = false;
};

struct Cell {
    char character = ' ';
    Style style;

    bool operator!=(const Cell& other) const {
        return character != other.character || 
               style.fg != other.style.fg ||
               style.bg != other.style.bg;
    }
};

// Viewer of the screen
struct Camera {
    int x;
    int y;
};

}
