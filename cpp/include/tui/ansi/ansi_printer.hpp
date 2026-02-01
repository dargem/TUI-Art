#pragma once

#include <string_view>
#include <cstddef>
#include <optional>

#include "core/types.hpp"
#include "tui/terminal_status.hpp"

namespace tui::ansi
{

    // A printer used to print objects to screen
    // This is a singleton as only one terminal will be in use
    // and to prevent misuse with multiple printers
    class Printer : TerminalDimensionListener
    {
    public:
        static Printer &getInstance();

        Printer(const Printer &) = delete;        // delete copy constructor
        void operator=(const Printer &) = delete; // delete assignment operator
        Printer(Printer &&) = delete;             // delete move constructor
        void operator=(Printer &&) = delete;      // delete move assignment operator

        // Prints a cell at the GridLocation inputted, printing
        //     - Character in its foreground colour
        //     - Background in the background colour
        void printCell(const Cell &cell, GridLocation printLocation);

        // Inserts a cell at the GridLocation, shifts same line cells right by one
        void insertCellRightShift(const Cell &cell, GridLocation insertLocation);

        // Deletes 1 cell at the cellLocation,
        // this shifts same line cells left by one
        void removeCellLeftShift(GridLocation cellLocation);

        // Shift down the whole display by shift rows
        void rowShiftDown(size_t shifts);

        // Reset the colour of the cursor
        void resetColour();

        // Print a debug cell, which is just a white hash
        void printDebugHashCell();

        // Receive a new terminal dimension size on update
        void receiveTerminalSize(TerminalDimension) override;

    private:
        // Moves the cursor to the grid location
        template <bool checked = true>
        void moveTo(GridLocation gridLocation);

        // Private constructor to prevent creation
        Printer();
        TerminalStatus &terminalStatus;
        TerminalDimension currentTerminalDimension;

        // Not 0, 0, 0 due to kitty stuff defaulting to BG which can break diffing logic
        constexpr static Colour FG_COLOUR_DEFAULT{255, 255, 255};
        constexpr static Colour BG_COLOUR_DEFAULT{0, 0, 1};
    };

};