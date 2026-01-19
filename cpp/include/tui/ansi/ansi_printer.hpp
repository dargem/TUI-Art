#pragma once

#include <string_view>
#include <cstddef>
#include "core/types.hpp"
#include <optional>

namespace tui::ansi {
    
// A printer used to print objects to screen
// This is a singleton as only one terminal will be in use
// and to prevent misuse with multiple printers
class Printer {
public:
    static Printer& getInstance();
    // private
    Printer(const Printer& printer) = delete; // don't implement copy constructor
    void operator=(const Printer& printer) = delete; // don't implement assignment operator

    // Prints a cell at the cursor
    // This print outputs the:
    //     - Character in its foreground colour
    //     - Background in the background colour
    void printCell(const Cell& cell);

    // Insert 1 cell at cursor
    // Shifts same line cells right by one
    void insertCellRightShift(const Cell& cell);

    // Delete 1 cell at cursor 
    // Shifts same line cells left by one
    void removeCellLeftShift();

    // Moves the cursor to the x,y coordinate
    void moveTo(size_t x, size_t y);

    // Shift down cells by shift columns
    void columnShiftDown(size_t shifts);

    void resetColour();
private:
    // Private constructor to prevent creation
    Printer() {};

    // Colour state of the last thing the printers printed 
    // and only one printer can exist so it is safe to reuse 
    // RGB colours for optimisation when possible rather than 
    // senselessly overwriting an identical past colour state
    std::optional<Colour> lastForegroundColour;
    std::optional<Colour> lastBackgroundColour;
};

};