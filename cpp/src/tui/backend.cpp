#include "tui/backend.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include <iostream>
#include <cstddef>

namespace tui {

using ansi::Printer;
using ansi::GO_HOME;
using ansi::SET_BACKGROUND_RGB;
using ansi::SET_FOREGROUND_RGB;
using ansi::RESET_COLOUR;

TerminalBackend::TerminalBackend(const size_t w, const size_t h, const int frameShift)
    : frontBuffer{ w, h }, backBuffer { w, h }, frameShift{ frameShift } 
{}

// Presents the back buffer through overwriting the front buffer
// Swaps the back buffer to the front, clearing the old buffer
// The y shift is a hint from the caller that the back buffer is a y shifted
// version of the current front buffer, this allows optimisation as a new line
// escape can be used to minimize the amount of rewriting required
// Back buffer and front buffer are not necessarily the same size if the users 
// terminal has resized, so checking for this is required as a special print case
void TerminalBackend::present(int y_shift) {
    // Moves cursor home
    std::cout << tui::ansi::GO_HOME;

    // Tracks where the terminal cursor is
    size_t terminalCursorX{ 0 };
    size_t terminalCursorY{ 0 };
    Printer::moveTo(0, 0);

    // Expansion won't hurt layout, but compression will immediately break it
    if (frontBuffer.height == backBuffer.height && frontBuffer.width == backBuffer.width) {

    }
    


    for (size_t y{ 0 }; y < backBuffer.height; ++y) {
        for (size_t x{ 0 }; x < backBuffer.width; ++x) {
            
            const Cell& newCell = backBuffer.getCell(x, y);
            const Cell& oldCell = frontBuffer.getCell(x, y);

            if (newCell != oldCell) {
                // only move the cursor when in incorrect position
                if (terminalCursorX != x || terminalCursorY != x) {
                    Printer::moveTo(x, y);
                    // update x and y with reset positions
                    terminalCursorX = x;
                    terminalCursorY = y;
                }
                
                // Print the cell, noting the cursor will advance by one automatically
                Printer::printCell(newCell);
                ++terminalCursorX;
            }
        }
    }
    
    // Reset colors
    std::cout << RESET_COLOUR;
    std::cout.flush();

    // Swap buffers then clear the back buffer
    frontBuffer = backBuffer;
    backBuffer.clear();
}

Surface& TerminalBackend::getDrawSurface() { return backBuffer; }

}
