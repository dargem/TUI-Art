#include "tui/ansi/ansi_printer.hpp"
#include "tui/ansi/ansi_constants.hpp"

#include <iostream>
#include <string_view>
#include <cstddef>

namespace tui::ansi {

void Printer::printCell(const Cell& cell) {
    // foreground string
    std::cout << SET_FOREGROUND_RGB 
              << (int)cell.style.fg.r << ";"
              << (int)cell.style.fg.g << ";"
              << (int)cell.style.fg.b << "m";
    // background string
    std::cout << SET_BACKGROUND_RGB 
              << (int)cell.style.bg.r << ";" 
              << (int)cell.style.bg.g << ";" 
              << (int)cell.style.bg.b << "m";
    // actual character
    std::cout << cell.character;
}

void Printer::moveTo(const size_t x, const size_t y) {
    std::cout << "\033[" << (y + 1) << ";" << (x + 1) << "H";
}

}