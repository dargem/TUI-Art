#include <atomic>
#include <chrono>
#include <csignal>
#include <format>
#include <thread>

#include "core/types.hpp"
#include "setup/app_context.hpp"
#include "setup/configs.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "tui/ansi/ansi_printer.hpp"
#include "tui/backend.hpp"

using setup::AppLogger;
using tui::Camera;
using tui::Cell;
using tui::Surface;
using tui::TerminalBackend;
using tui::TerminalDimension;
using tui::ansi::CLEAR_SCREEN;
using tui::ansi::HIDE_CURSOR;
using tui::ansi::Printer;
using tui::ansi::RESET_COLOUR;
using tui::ansi::SHOW_CURSOR;
using utils::LogLevel;

std::atomic<bool> running{true};

void signalHandler(int) {
    // has received a signal interrupt (ctrl C)
    running = false;
}

int main() {
    // 1. Setup
    std::signal(SIGINT, signalHandler);

    AppContext appContext;
    TerminalBackend backend(appContext);
    AppLogger& logger = appContext.getLogger();
    logger.log<LogLevel::INFO>("Program starting up");

    // 2. Clear screen initially
    std::cout << CLEAR_SCREEN;
    std::cout << HIDE_CURSOR;

    // 3. Game Loop
    int frameCount{};

    // int yCount;

    logger.log<LogLevel::INFO>("Starting rendering loop");
    while (running) {
        TerminalDimension dimension = appContext.getTerminalStatus().publishTerminalSize();
        logger.log<LogLevel::DEBUG>(
            std::format("Dimension queried with width and height in char of {},{}",
                        dimension.charWidth, dimension.charHeight));
        Surface& surface = backend.getDrawSurface();
        logger.log<LogLevel::DEBUG>(std::format("Draw surface has width and height of {},{}",
                                                surface.width, surface.height));

        // --- Update & Render ---

        // Example: Draw a moving box
        int boxX = frameCount % (dimension.charWidth - 5);
        int boxY = (frameCount / 2) % (dimension.charHeight - 3);

        /*
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                Cell c;
                c.style.bg = {0, 0, 1};
                // kitty defaults 0, 0, 0 black to the default terminal for some reason...
                surface.setCell(x, y, c);
            }
        }
        */

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 5; ++x) {
                Cell c;
                c.character = '#';
                c.style.fg = {255, 0, 0};  // Red
                c.style.bg = {0, 0, 0};
                surface.setCell(boxX + x, boxY + y, c);
            }
        }

        // --- Present ---

        backend.present(Camera{0, 0});
        // Timing
        std::this_thread::sleep_for(std::chrono::milliseconds(30));  // ~30 FPS
        frameCount++;

        appContext.getPrinter().resetColour();
    }

    // user has done a signal interrupt so main loop exited gracefully
    std::cout << SHOW_CURSOR << CLEAR_SCREEN << RESET_COLOUR << std::endl;
    return 0;
}
