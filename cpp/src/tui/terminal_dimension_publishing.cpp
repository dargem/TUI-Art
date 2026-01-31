#include "tui/terminal_dimension_publishing.hpp"

namespace tui
{
    

    void TerminalDimensionPublisher::addListener(TerminalDimensionListener *newListener)
    {
        listeners.push_back(newListener);
    }

    void TerminalDimensionPublisher::publishTerminalSize()
    {
        TerminalDimension terminalDimension{terminalStatus.getTerminalDimension()};
        for (TerminalDimensionListener *listener : listeners)
        {
            listener->receiveTerminalSize(terminalDimension);
        }
    }
}