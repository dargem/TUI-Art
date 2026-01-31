#include "tui/ansi/ansi_printer.hpp"
#include "tui/ansi/ansi_constants.hpp"
#include "tui/terminal_status.hpp"

#include <iostream>
#include <string_view>
#include <cstddef>
#include <cassert>

namespace tui::ansi
{

    Printer &Printer::getInstance()
    {
        static Printer printer;
        return printer;
    }

    Printer::Printer()
        : terminalStatus{TerminalStatus::getInstance()}
    {
        terminalStatus.cursorLocation = {0, 0};
    }

    void Printer::printCell(const Cell &cell, GridLocation insertionLocation)
    {
        // Print new escape string colours only when required
        // If the last printed is the same assume no change is needed

        // foreground string
        if (lastForegroundColour != cell.style.fg || !lastForegroundColour.has_value())
        {
            std::cout << SET_FOREGROUND_RGB
                      << (int)cell.style.fg.r << ";"
                      << (int)cell.style.fg.g << ";"
                      << (int)cell.style.fg.b << "m";

            lastForegroundColour = cell.style.fg;
        }

        // background string
        if (lastBackgroundColour != cell.style.bg || !lastBackgroundColour.has_value())
        {
            std::cout << SET_BACKGROUND_RGB
                      << (int)cell.style.bg.r << ";"
                      << (int)cell.style.bg.g << ";"
                      << (int)cell.style.bg.b << "m";

            lastBackgroundColour = cell.style.bg;
        }

        // actual character
        std::cout << cell.character;
    }

    void Printer::insertCellRightShift(const Cell &cell)
    {
        std::cout << INSERT_SPACE;
        printCell(cell);
    }

    // Delete 1 character at cursor (shifts same left)
    void Printer::removeCellLeftShift()
    {
        std::cout << REMOVE_CELL;
    }

    // Y values are "inverted" when it comes to ANSI escape codes
    // A y value of 0 is going to be at the top not at the bottom
    // Rendering obviously expects 0, 0 to be at the bottom left though
    // So this must be inverted
    // ANSI columns are also one based but input x is 0 based, so needs an offset by one
    void Printer::moveTo(const size_t x, const size_t y, size_t surfaceHeight)
    {
        assert(surfaceHeight > y && "surface height must be larger than y pos to move to");
        std::cout << "\033[" << surfaceHeight - y << ";" << x + 1 << "H";
    }

    void Printer::rowShiftDown(size_t shifts)
    {
        for (size_t i{}; i < shifts; ++i)
        {
            std::cout << INSERT_LINE;
        }
    }

    // Resets the printers colour, rewriting last colour to null to guarantee
    // the printer will always output the change colour ansi for the next one
    void Printer::resetColour()
    {
        std::cout << RESET_COLOUR;
        lastForegroundColour = std::nullopt;
        lastBackgroundColour = std::nullopt;
    }

    void Printer::printDebugHashCell()
    {
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
        printCell(cell);
    }

}