#pragma once

#include <string_view>
#include <cstddef>
#include "core/types.hpp"

namespace tui::ansi {
    
// A utility class used to print objects to screen
class Printer {
public:
    // Converts a cell to a string view object
    // Printing this string view prints the cell's
    //     - Character in foreground colour
    //     - The background colour
    static void printCell(const Cell& cell);

    // Moves the cursor to the x,y coordinate
    static void moveTo(size_t x, size_t y);
};

};