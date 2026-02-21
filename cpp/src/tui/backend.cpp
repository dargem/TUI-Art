#include "tui/backend.hpp"

#include <cstddef>
#include <format>
#include <iostream>

#include "tui/ansi/ansi_printer.hpp"
#include "utils/logger.hpp"

namespace tui {

using ansi::Printer;
using utils::LogLevel;

TerminalBackend::TerminalBackend(AppContext& context) :
        logger{context.getLogger()},
        dimensionSubscriptionToken{context.getTerminalStatus().addDimensionListener(this)},
        currentDimension{context.getTerminalStatus().queryTerminalSize()},
        frontBuffer{currentDimension.charWidth, currentDimension.charHeight},
        backBuffer{currentDimension.charWidth, currentDimension.charHeight},
        frontBufferCamera{0, 0},
        printer{context.getPrinter()} {}

// Presents the back buffer through overwriting the front buffer
// Swaps the back buffer to the front, clearing the old buffer.
// The backBufferCamera allows for optimisation as it indicates
// the new back buffer is a partial translation of the front buffer.
// The front buffer can then be translated that amount before diffing,
// this minimizes the amount of rewriting required improving speeds.
// Back buffer and front buffer are not necessarily the same size if the users
// terminal has resized, so checking for this is required as a special print case
void TerminalBackend::present(const Camera backBufferCamera) {
    // abort if the new camera is not equal or above the last in height
    assert(backBufferCamera.y >= frontBufferCamera.y &&
           "Downwards camera movement not supported currently");

    logger.log<LogLevel::INFO>("Started rendering back buffer");

    // if the terminal gets resized it should just reprint the whole screen without dif checks as
    // current state can get messed up by resizes so there's no point in diffing
    if (!backBuffer.sameSize(frontBuffer)) {
        logger.log<LogLevel::INFO>("Screen resize detected, doing a diffless screen reprint");

        for (size_t y{}; y < backBuffer.height; ++y) {
            for (size_t x{}; x < backBuffer.width; ++x) {
                // Cell is lightweight better to copy by val when not modifying
                Cell cell = backBuffer.getCell(x, y);
                printer.printCell(cell, {x, y});
            }
        }
        cleanupPresentation(backBufferCamera);
        return;
    }

    // This can always be converted to a size t because of the assertion above
    size_t y_increase{static_cast<size_t>(backBufferCamera.y - frontBufferCamera.y)};

    if (y_increase != 0) {
        logger.log<LogLevel::INFO>(std::format(
            "Different camera locs, back buffer cam at {},{} and front buffer cam at {},{}",
            backBufferCamera.x, backBufferCamera.y, frontBufferCamera.x, frontBufferCamera.y));
    }

    // An initial loop is used to shift down the front buffer with empty lines
    // It then prints in these empty lines the new back buffer
    // As the empty lines are empty no diffing is needed for this
    // Note the number of iterations is not related to cursor y position
    // Because the cursor will always be on the top row
    for (size_t i{}; i < y_increase && i < backBuffer.height; ++i) {
        const size_t y{backBuffer.height - 1};

        printer.rowShiftDown(1);

        for (size_t x{}; x < backBuffer.width; ++x) {
            const Cell& newCell = backBuffer.getCell(x, y);
            printer.printCell(newCell, {x, y});
        }
    }

    // After the shift has been noted and overwritten, diffing behaviour can be implemented
    // It starts iterating after what has already been printed from the back buffer (top y_increase
    // rows) to avoid needless diffing logic
    const size_t diffHeight{backBuffer.height - y_increase};
    for (size_t y{}; y < diffHeight; ++y) {
        for (size_t x{}; x < backBuffer.width; ++x) {
            const Cell& newCell = backBuffer.getCell(x, y);

            // Includes the translation from the front buffer as its been shifted down by y_increase
            // So the old front buffer at y = 1 with a shift of 1 goes down to y = 0,
            // so compare backBuffer with frontBuffer + y_increase
            const Cell& oldCell = frontBuffer.getCell(x, y + y_increase);

            if (newCell != oldCell) {
                // Print the cell, noting the cursor will advance by one automatically
                printer.printCell(newCell, {x, y});
            }
        }
    }

    cleanupPresentation(backBufferCamera);
}

void TerminalBackend::cleanupPresentation(const Camera backBufferCamera) {
    std::cout.flush();

    // Swap buffers then clear the back buffer
    frontBuffer = std::move(backBuffer);
    frontBufferCamera = backBufferCamera;
    backBuffer.reset();
    backBuffer.drawnOn = false;  // wiped so now empty
}

void TerminalBackend::receiveTerminalSize(TerminalDimension dimension) {
    // kindof sketchy, back buffer if drawn on before receiving terminal size, the new terminal size
    // will throw out what is drawn this leads to logical errors so should just assert as false
    assert(
        !backBuffer.drawnOn &&
        "Back buffer must not be drawn on before receiving a new terminal size for the backbuffer");
    backBuffer = Surface(dimension.charWidth, dimension.charHeight);
}

Surface& TerminalBackend::getDrawSurface() {
    // returns a non const reference so it is states as drawn on
    backBuffer.drawnOn = true;
    return backBuffer;
}

}  // namespace tui
