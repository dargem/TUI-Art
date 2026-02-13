#pragma once

#include <cstddef>
#include <vector>
#include <optional>
#include "core/types.hpp"
#include "utils/sparse_set.hpp"

namespace tui
{
    using utils::SparseSet;
    using utils::ID;

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

    // forward declare
    class TerminalDimensionToken;

    class TerminalStatus
    {
    public:
        TerminalStatus();

        // Add a listener to the size of the terminal
        // Listeners will be updated with the new terminal size if the terminal resized
        // It will only check for updates at the start of each frame
        [[nodiscard]]
        TerminalDimensionToken addDimensionListener(TerminalDimensionListener *);

        // Discard a dimension listener by its ID
        void removeDimensionListener(ID discarded);

        // Should be triggered at the start of each frame
        void publishTerminalSize();

        GridLocation cursorLocation;

        // Holds the last colour the terminal has printed
        // Is updated when the terminal has printed something new
        LoadedColour loadedColour;

    private:

        // check the dimension of the terminal
        TerminalDimension getTerminalDimension();

        // all the listeners for terminal size updates
        SparseSet<TerminalDimensionListener *> listeners;
    };

    // A token that manages the lifecycle of subscription to the terminal
    // Should only be issued by TerminalStatus
    class TerminalDimensionToken
    {
    public:
        void operator=(const TerminalDimensionToken&) = delete; // remove copy assignment
        TerminalDimensionToken(const TerminalDimensionToken&) = delete; // remove copy construction
        TerminalDimensionToken& operator=(TerminalDimensionToken&&) = delete; // remove move assignment

        TerminalDimensionToken(TerminalDimensionToken&& other)
            : publisher{other.publisher}, listenerID{other.listenerID}
        {
            // move construct and invalidate the old ID
            other.listenerID = std::nullopt;
        }

        ~TerminalDimensionToken() {
            if (listenerID.has_value()) {
                // this token is valid
                publisher.removeDimensionListener(listenerID.value());
            }
        }
    private:
        friend class TerminalStatus; 

        explicit TerminalDimensionToken(TerminalStatus& publisher, ID listenerID)
            : publisher{publisher}, listenerID{listenerID}
        {
        }

        TerminalStatus& publisher;
        std::optional<ID> listenerID;
    };

};
