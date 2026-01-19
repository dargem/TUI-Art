#include <thread>
#include <chrono>

#include "tui/backend.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "core/types.hpp"

using tui::ansi::CLEAR_SCREEN;
using tui::Camera;
using tui::Cell;
using tui::ansi::Printer;
/*
 * Main Entry Point for TUI-Art C++ Rewrite
 */
int main() {
    // 1. Setup
    constexpr int WIDTH{ 80 };
    constexpr int HEIGHT{ 24 };
    const Camera camera{0, 0};

    tui::TerminalBackend backend(WIDTH, HEIGHT);
    
    // 2. Clear screen initially
    std::cout << CLEAR_SCREEN; 

    // 3. Game Loop
    bool running{ true };
    int frameCount{};

    while (running) {
        tui::Surface& surface = backend.getDrawSurface();
        
        // --- Update & Render ---
        
        // Example: Draw a moving box
        int boxX = frameCount % (WIDTH - 5);
        int boxY = (frameCount / 2) % (HEIGHT - 2);
        
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                Cell c;
                c.style.bg = {255, 255, 255};
                surface.setCell(x, y, c);
            } 
        }

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 5; ++x) {
                Cell c;
                c.character = '#';
                c.style.fg = {255, 0, 0}; // Red
                c.style.bg = {0, 0, 0};
                surface.setCell(boxX + x, boxY + y, c);
            }
        }
        
        // --- Present ---
        backend.present(camera);

        // Timing
        std::this_thread::sleep_for(std::chrono::milliseconds(33)); // ~30 FPS
        frameCount++;
        
        // Simple exit condition for demo
        if (frameCount > 200) running = false;

        Printer::getInstance().resetColour();
    }

    return 0;
}
