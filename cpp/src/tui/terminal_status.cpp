#include "tui/terminal_status.hpp"

namespace tui {

TerminalStatus& TerminalStatus::getInstance() {
    // lazy loaded static terminal status for sharing around
    static TerminalStatus terminal_status;
    return terminal_status;
}

TerminalStatus::TerminalStatus() 
    : cursorLocation(0, 0), 
      loadedColour(Colour(), Colour())
{
    // reset colours on startup
    loadedColour.bg.r = 0;
    loadedColour.bg.g = 0;
    loadedColour.bg.b = 0;

    loadedColour.fg.r = 0;
    loadedColour.fg.g = 0;
    loadedColour.fg.b = 0;
}

TerminalDimension TerminalStatus::getTerminalDimension() {
    return TerminalDimension(0, 0);
}

}