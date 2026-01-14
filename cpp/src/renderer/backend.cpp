#include "renderer/backend.hpp"
#include <iostream>

namespace tui {

void TerminalBackend::present() {
    // Standard ANSI escape to move cursor home
    std::cout << "\033[H"; 

    for (int y = 0; y < backBuffer.height; ++y) {
        for (int x = 0; x < backBuffer.width; ++x) {
            
            Cell& newPixel = backBuffer.get(x, y);
            Cell& oldPixel = frontBuffer.get(x, y);

            if (newPixel != oldPixel) {
                // Determine movement strategy (simple optimization)
                // In a real TUI engine, we'd batch cursor moves
                
                // Move cursor to x,y if needed (simplest for now is just linear print, 
                // but for diffing we assume cursor is linear unless we skip)
                // For proper diffing, we must position cursor if we skipped pixels.
                // For this skeleton, we will validly reprint the line if changed, 
                // or ideally use a library like ncurses. 
                // Implemening raw ANSI diffing from scratch:
                
                std::cout << "\033[" << (y + 1) << ";" << (x + 1) << "H"; // Move to y,x (1-based)
                
                // Set color
                std::cout << "\033[38;2;" 
                          << (int)newPixel.style.fg.r << ";" 
                          << (int)newPixel.style.fg.g << ";" 
                          << (int)newPixel.style.fg.b << "m"; // FG
                std::cout << "\033[48;2;" 
                          << (int)newPixel.style.bg.r << ";" 
                          << (int)newPixel.style.bg.g << ";" 
                          << (int)newPixel.style.bg.b << "m"; // BG

                std::cout << newPixel.character;
            }
        }
    }
    
    // Reset colors
    std::cout << "\033[0m";
    std::cout.flush();

    // Swap buffers
    frontBuffer = backBuffer;
    // Clearing backbuffer might be optional if we overwrite everything, 
    // but good for safety
    backBuffer.clear();
}

}
