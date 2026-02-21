#include "tui/ansi/ansi_printer.hpp"

#include <cassert>
#include <cstddef>
#include <iostream>
#include <string_view>

#include "tui/ansi/ansi_constants.hpp"
#include "tui/terminal_status.hpp"
#include "utils/logger.hpp"

namespace tui::ansi {

Printer::Printer(TerminalStatus& terminalStatus) :
        dimensionSubscriptionToken{terminalStatus.addDimensionListener(this)},
        currentTerminalDimension{terminalStatus.queryTerminalSize()} {
    // centre the cursor to a known position as its at an
    // unknown position at the start of the application
    // do an unchecked move
    moveTo<false>(GridLocation{0, 0});

    // resets the colour to something known
    resetColour();
}

void Printer::receiveTerminalSize(TerminalDimension terminalDimension) {
    currentTerminalDimension = terminalDimension;
}

void Printer::printCell(const Cell& cell, GridLocation insertionLocation) {
    moveTo(insertionLocation);

    // Print new escape string colours only when required
    // If the last printed colour is the same no change is needed

    if (loadedFGColour != cell.style.fg) {
        std::cout << SET_FOREGROUND_RGB << (int)cell.style.fg.r << ";" << (int)cell.style.fg.g
                  << ";" << (int)cell.style.fg.b << "m";

        loadedFGColour = cell.style.fg;
    }

    // background string
    if (loadedBGColour != cell.style.bg) {
        std::cout << SET_BACKGROUND_RGB << (int)cell.style.bg.r << ";" << (int)cell.style.bg.g
                  << ";" << (int)cell.style.bg.b << "m";

        loadedBGColour = cell.style.bg;
    }

    // actual character
    std::cout << cell.character;

    // printing a character moves the cursor one to the right
    cursorLocation.x += 1;
}

void Printer::insertCellRightShift(const Cell& cell, const GridLocation insertLocation) {
    moveTo(insertLocation);
    std::cout << INSERT_SPACE;
    printCell(cell, insertLocation);
}

// Delete 1 character at cursor (shifts same left)
void Printer::removeCellLeftShift(const GridLocation deleteLocation) {
    moveTo(deleteLocation);
    std::cout << REMOVE_CELL;
}

// Y values are "inverted" when it comes to ANSI escape codes
// A y value of 0 is going to be at the top not at the bottom
// Rendering obviously expects 0, 0 to be at the bottom left though
// So this must be inverted. ANSI columns are also one based but input
// x is 0 based, so needs an offset by one
template <bool checked>
void Printer::moveTo(const GridLocation destination) {
    // option of an unchecked move for setup
    if constexpr (checked) {
        // check if a movmement is actually needed, if the cursors in
        // the right position already then it can just be skipped
        if (cursorLocation == destination) {
            return;
        }
    }

    size_t surfaceHeight{currentTerminalDimension.charHeight};

    assert(surfaceHeight > destination.y && "surface height must be larger than y pos to move to");
    std::cout << "\033[" << surfaceHeight - destination.y << ";" << destination.x + 1 << "H";
    cursorLocation = destination;
}

void Printer::rowShiftDown(size_t shifts) {
    for (size_t i{}; i < shifts; ++i) {
        std::cout << INSERT_LINE;
    }
}

// Resets the printers colour, rewriting last colour to null to guarantee
// the printer will always output the change colour ansi for the next one
void Printer::resetColour() {
    std::cout << SET_FOREGROUND_RGB << (int)FG_COLOUR_DEFAULT.r << ";" << (int)FG_COLOUR_DEFAULT.g
              << ";" << (int)FG_COLOUR_DEFAULT.b << "m";
    loadedFGColour = FG_COLOUR_DEFAULT;  // set to a known colour

    std::cout << SET_BACKGROUND_RGB << (int)BG_COLOUR_DEFAULT.r << ";" << (int)BG_COLOUR_DEFAULT.g
              << ";" << (int)BG_COLOUR_DEFAULT.b << "m";
    loadedBGColour = BG_COLOUR_DEFAULT;  // set to a known colour
}

void Printer::printDebugHashCell() {
    Cell cell;

    // Set cell's foreground to white
    cell.style.fg.r = 255;
    cell.style.fg.g = 255;
    cell.style.fg.b = 255;

    cell.character = '#';

    // Set cell's background to black
    cell.style.bg.r = 0;
    cell.style.bg.g = 0;
    cell.style.bg.b = 1;
    printCell(cell, DEBUG_LOCATION);
}

}  // namespace tui::ansi