#include <thread>
#include <chrono>

#include "tui/backend.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "core/types.hpp"

using tui::ansi::CLEAR_SCREEN;
using tui::Camera;

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
    int frameCount{ 0 };

    while (running) {
        tui::Surface& surface = backend.getDrawSurface();
        
        // --- Update & Render ---
        
        // Example: Draw a moving box
        int boxX = frameCount % (WIDTH - 5);
        int boxY = (frameCount / 2) % (HEIGHT - 2);

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 5; ++x) {
                tui::Cell c;
                c.character = '#';
                c.style.fg = {255, 0, 0}; // Red
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

    }

    return 0;
}
