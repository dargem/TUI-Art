#include "tui/backend.hpp"
#include "tui/print_constants.hpp"

#include <iostream>
namespace tui {

TerminalBackend::TerminalBackend(const int w, const int h, const int frameShift)
    : frontBuffer{ w, h }, backBuffer { w, h }, frameShift{ frameShift } 
{}

void TerminalBackend::present() {
    // Standard ANSI escape to move cursor home
    std::cout << tui::GO_HOME;

    for (int y = 0; y < backBuffer.height; ++y) {
        for (int x = 0; x < backBuffer.width; ++x) {
            
            const Cell& newPixel = backBuffer.getCell(x, y);
            const Cell& oldPixel = frontBuffer.getCell(x, y);

            if (newPixel != oldPixel) {
                // Determine movement strategy (simple optimization)
                // In a real TUI engine, we'd batch cursor moves
                
                // Move cursor to x,y if needed (simplest for now is just linear print, 
                // but for diffing we assume cursor is linear unless we skip)
                // For proper diffing, we must position cursor if we skipped pixels.
                // For this skeleton, we will validly reprint the line if changed, 
                // or ideally use a library like ncurses. 
                // Implementing raw ANSI diffing from scratch:
                
                std::cout << "\033[" << (y + 1) << ";" << (x + 1) << "H"; // Move to y,x (1-based)
                
                // Set color
                std::cout << SET_FOREGROUND_RGB 
                          << (int)newPixel.style.fg.r << ";"
                          << (int)newPixel.style.fg.g << ";"
                          << (int)newPixel.style.fg.b << "m"; // FG RGB
                std::cout << SET_BACKGROUND_RGB 
                          << (int)newPixel.style.bg.r << ";" 
                          << (int)newPixel.style.bg.g << ";" 
                          << (int)newPixel.style.bg.b << "m"; // BG RGB

                std::cout << newPixel.character;
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
