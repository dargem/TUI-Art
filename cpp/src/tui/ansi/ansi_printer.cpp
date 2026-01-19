#include "tui/ansi/ansi_printer.hpp"
#include "tui/ansi/ansi_constants.hpp"

#include <iostream>
#include <string_view>
#include <cstddef>

namespace tui::ansi {

Printer& Printer::getInstance() {
    static Printer printer;
    return printer;
}

void Printer::printCell(const Cell& cell) {
    // Print new escape string colours only when required
    // If the last printed is the same assume no change is needed

    // foreground string
    if (lastForegroundColour != cell.style.fg || !lastForegroundColour.has_value()) {
        std::cout << SET_FOREGROUND_RGB 
                  << (int)cell.style.fg.r << ";"
                  << (int)cell.style.fg.g << ";"
                  << (int)cell.style.fg.b << "m";

        lastForegroundColour = cell.style.fg;
    }

    // background string
    if (lastBackgroundColour != cell.style.bg || !lastBackgroundColour.has_value()) {
        std::cout << SET_BACKGROUND_RGB 
                  << (int)cell.style.bg.r << ";" 
                  << (int)cell.style.bg.g << ";" 
                  << (int)cell.style.bg.b << "m";

        lastBackgroundColour = cell.style.bg;
    }

    // actual character
    std::cout << cell.character;
}

void Printer::insertCellRightShift(const Cell& cell) {
    std::cout << INSERT_SPACE;
    printCell(cell);
}

// Delete 1 character at cursor (shifts same left)
void Printer::removeCellLeftShift() {
    std::cout << REMOVE_CELL;
}

void Printer::moveTo(const size_t x, const size_t y) {
    std::cout << "\033[" << y << ";" << x << "H";
}

void Printer::columnShiftDown(size_t shifts) {
    for (size_t i{}; i < shifts; ++i) {
        std::cout << INSERT_LINE;
    }
}

void Printer::resetColour() {
    std::cout << RESET_COLOUR;
}

}