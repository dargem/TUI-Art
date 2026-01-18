#include "tui/backend.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include <iostream>

namespace tui {

using ansi::Printer;
using ansi::GO_HOME;
using ansi::SET_BACKGROUND_RGB;
using ansi::SET_FOREGROUND_RGB;
using ansi::RESET_COLOUR;

TerminalBackend::TerminalBackend(const int w, const int h, const int frameShift)
    : frontBuffer{ w, h }, backBuffer { w, h }, frameShift{ frameShift } 
{}

void TerminalBackend::present() {
    // Moves cursor home
    std::cout << tui::ansi::GO_HOME;

    // Tracks where the terminal cursor is
    int terminalCursorX{ -1 };
    int terminalCursorY{ -1 };

    for (int y = 0; y < backBuffer.height; ++y) {
        for (int x = 0; x < backBuffer.width; ++x) {
            
            const Cell& newCell = backBuffer.getCell(x, y);
            const Cell& oldCell = frontBuffer.getCell(x, y);

            if (newCell != oldCell) {
                // only move the cursor when in incorrect position
                if (terminalCursorX != x || terminalCursorY != x) {
                    std::cout << "\033[" << (y + 1) << ";" << (x + 1) << "H"; // Move to y, x (1-based)
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
