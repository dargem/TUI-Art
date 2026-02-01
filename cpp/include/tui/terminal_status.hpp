#pragma once

#include <cstddef>
#include <vector>
#include "core/types.hpp"

namespace tui
{

    struct TerminalDimension
    {

        TerminalDimension(size_t charWidth, size_t charHeight)
            : charWidth{charWidth}, charHeight{charHeight}
        {
        }

        size_t charWidth;
        size_t charHeight;
    };

    struct LoadedColour
    {

        LoadedColour(Colour fg, Colour bg)
            : fg{fg}, bg{bg}
        {
        }

        Colour fg;
        Colour bg;
    };

    class TerminalDimensionListener
    {
    public:
        virtual void receiveTerminalSize(TerminalDimension) = 0;
    };

    class TerminalStatus
    {
    public:
        static TerminalStatus &getInstance();

        void operator=(const TerminalStatus &) = delete; // remove assignment
        void operator=(TerminalStatus &&) = delete;      // remove move assignment
        TerminalStatus(const TerminalStatus &) = delete; // remove copy constructor
        TerminalStatus(TerminalStatus &&) = delete;      // remove move constructor

        // Add a listener to the size of the terminal
        // Listeners will be updated with the new terminal size if the terminal resized
        // It will only check for updates at the start of each frame
        void addDimensionListener(TerminalDimensionListener *);

        // Should be triggered at the start of each frame
        void publishTerminalSize();

        GridLocation cursorLocation;

        // Holds the last colour the terminal has printed
        // Is updated when the terminal has printed something new
        LoadedColour loadedColour;

    private:
        TerminalStatus();

        // check the dimension of the terminal
        TerminalDimension getTerminalDimension();

        // all the listeners for terminal size updates
        std::vector<TerminalDimensionListener *> listeners;
    };

};
