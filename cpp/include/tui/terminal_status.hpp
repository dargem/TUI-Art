#pragma once

#include <cstddef>
#include "core/types.hpp"

namespace tui {

struct TerminalDimension {

    TerminalDimension(size_t x, size_t y)
        : x{ x }, y{ y }
    {}

    size_t x;
    size_t y;
};

struct CursorLocation {

    CursorLocation(size_t x, size_t y)
        : x{ x }, y{ y }
    {}

    size_t x;
    size_t y;
};

struct LoadedColour {

    LoadedColour(Colour fg, Colour bg)
        : fg{ fg }, bg { bg }
    {}

    Colour fg;
    Colour bg;
};

class TerminalStatus {
public:
    static TerminalStatus& getInstance();

    void operator=(const TerminalStatus& terminalStatus) = delete; // remove assignment
    void operator=(const TerminalStatus&& terminalStatus) = delete; // remove move assignment
    TerminalStatus(const TerminalStatus& terminalStatus) = delete; // remove copy constructor
    TerminalStatus(const TerminalStatus&& terminalStatus) = delete; // remove move constructor

    // Get a terminal dimension object holding 
    // width/height in single width ASCII characters
    TerminalDimension getTerminalDimension();

    CursorLocation cursorLocation;

    // Holds the last colour the terminal has printed
    // Is updated when the terminal has printed something new
    LoadedColour loadedColour;
private:

    TerminalStatus();
};

};

