#include "tui/backend.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include <iostream>
#include <cstddef>

namespace tui {

using ansi::Printer;

TerminalBackend::TerminalBackend(const size_t w, const size_t h)
    : frontBuffer{ w, h }, backBuffer { w, h }, frontBufferCamera{0, 0}
{}

// Presents the back buffer through overwriting the front buffer
// Swaps the back buffer to the front, clearing the old buffer.
// The backBufferCamera allows for optimisation as it indicates
// the new back buffer is a partial translation of the front buffer.
// The front buffer can then be translated that amount before diffing,
// this minimizes the amount of rewriting required improving speeds.
// Back buffer and front buffer are not necessarily the same size if the users
// terminal has resized, so checking for this is required as a special print case
void TerminalBackend::present(const Camera backBufferCamera) {

    Printer& printer = Printer::getInstance();

    // abort if the new camera is not equal or above the last in height
    assert(backBufferCamera.y >= frontBufferCamera.y && "Downwards camera movement not supported currently");

    // Tracks where the terminal cursor is
    printer.moveTo(0, 0);
    size_t terminalCursorX{};
    size_t terminalCursorY{};

    // This can always be converted to a size t because of the assertion above
    size_t y_increase{ static_cast<size_t>(backBufferCamera.y - frontBufferCamera.y) };

    for (size_t i{}; i < y_increase; ++i) {
        printer.columnShiftDown(1);
    }

    // Expansion won't hurt layout, but compression will immediately break it
    if (frontBuffer.height == backBuffer.height && frontBuffer.width == backBuffer.width) {

    }

    for (size_t y{}; y < backBuffer.height; ++y) {
        for (size_t x{}; x < backBuffer.width; ++x) {
            
            const Cell& newCell = backBuffer.getCell(x, y);
            const Cell& oldCell = frontBuffer.getCell(x, y);

            if (newCell != oldCell) {
                // only move the cursor when in incorrect position
                if (terminalCursorX != x || terminalCursorY != x) {
                    printer.moveTo(x, y);
                    // update x and y with reset positions
                    terminalCursorX = x;
                    terminalCursorY = y;
                }
                
                // Print the cell, noting the cursor will advance by one automatically
                printer.printCell(newCell);
                ++terminalCursorX;
            }
        }
    }
    
    // Reset colors
    printer.resetColour();
    std::cout.flush();

    // Swap buffers then clear the back buffer
    frontBuffer = backBuffer;
    frontBufferCamera = backBufferCamera;
    backBuffer.clear();
}

Surface& TerminalBackend::getDrawSurface() { return backBuffer; }

}
