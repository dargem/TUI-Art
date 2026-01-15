#pragma once
#include <cstdint>

namespace tui {

struct Color {
    uint8_t r, g, b;
};

struct Style {
    Color fg{255, 255, 255};
    Color bg{0, 0, 0};
    bool bold = false;
};

struct Cell {
    char character = ' ';
    Style style;

    bool operator!=(const Cell& other) const {
        return character != other.character || 
               style.fg.r != other.style.fg.r || 
               style.fg.g != other.style.fg.g || 
               style.fg.b != other.style.fg.b ||
               style.bg.r != other.style.bg.r ||
               style.bg.g != other.style.bg.g ||
               style.bg.b != other.style.bg.b;
    }
};

struct Camera {
    float x = 0;
    float y = 0;
};

}
