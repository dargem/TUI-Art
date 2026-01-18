#include "tui/ansi/ansi_printer.hpp"
#include "tui/ansi/ansi_constants.hpp"

#include <iostream>
#include <string_view>

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

}