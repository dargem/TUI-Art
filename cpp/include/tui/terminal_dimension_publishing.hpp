#pragma once

#include "tui/terminal_status.hpp"
#include <vector>

namespace tui
{

    class TerminalDimensionListener
    {
    public:
        virtual void receiveTerminalSize(TerminalDimension) = 0;
    };

    // Singleton publisher
    class TerminalDimensionPublisher
    {
    public:
        TerminalDimensionPublisher &getInstance();
        void operator=(const TerminalDimensionPublisher &) = delete;             // delete copy assignment
        void operator=(TerminalDimensionPublisher &&) = delete;                  // delete move assignment
        TerminalDimensionPublisher(const TerminalDimensionPublisher &) = delete; // delete copy constructor
        TerminalDimensionPublisher(TerminalDimensionPublisher &&) = delete;      // delete move constructor

        // Add a listener to the size of the terminal
        // Listeners will be updated with the new terminal size if the terminal resized
        // It will only check for updates at the start of each frame
        void addListener(TerminalDimensionListener *newListener);

        // Should be triggered at the start of each frame
        void publishTerminalSize();

    private:
        TerminalDimensionPublisher(); // private construct to prevent outside creation
        const TerminalStatus &terminalStatus;
        std::vector<TerminalDimensionListener *> listeners;
    };

}