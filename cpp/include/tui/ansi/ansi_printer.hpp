#pragma once

#include <string_view>
#include <cstddef>
#include "core/types.hpp"

namespace tui::ansi {
    
// A utility class used to print objects to screen
class Printer {
public:
    // Prints a cell at the cursor
    // This print outputs the:
    //     - Character in its foreground colour
    //     - Background in the background colour
    static void printCell(const Cell& cell);

    // Insert 1 cell at cursor
    // Shifts same line cells right by one
    static void insertCellRightShift(const Cell& cell);

    // Delete 1 cell at cursor 
    // Shifts same line cells left by one
    static void removeCellLeftShift();

    // Moves the cursor to the x,y coordinate
    static void moveTo(size_t x, size_t y);
};

};