#include "tui/terminal_dimension_publishing.hpp"

namespace tui
{
    // lazy construct a publisher
    TerminalDimensionPublisher &TerminalDimensionPublisher::getInstance()
    {
        static TerminalDimensionPublisher instance;
        return instance;
    }

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