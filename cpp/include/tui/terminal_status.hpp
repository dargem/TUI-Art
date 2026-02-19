#pragma once

#include <cstddef>
#include <optional>
#include <vector>

#include "core/types.hpp"
#include "utils/sparse_set.hpp"

namespace tui {
struct TerminalDimension {
    TerminalDimension(size_t charWidth, size_t charHeight) :
            charWidth{charWidth}, charHeight{charHeight} {}

    size_t charWidth;
    size_t charHeight;
};

class TerminalDimensionListener {
   public:
    // receive news on a new terminal size
    virtual void receiveTerminalSize(TerminalDimension) = 0;
};

// forward declare
class TerminalDimensionToken;

class TerminalStatus {
   public:
    // Add a listener to the size of the terminal
    // Listeners will be updated with the new terminal size if the terminal resized
    // It will only check for updates at the start of each frame
    [[nodiscard]]
    TerminalDimensionToken addDimensionListener(TerminalDimensionListener*);

    // Discard a dimension listener by its ID
    void removeDimensionListener(utils::ID discarded);

    // Should be triggered at the start of each frame
    // Updates each listener with a new update
    // Also returns an optionally usable terminal dimension it emitted
    TerminalDimension publishTerminalSize();

    // Returns the number of listeners terminal status has
    size_t getNumberListeners() const;

    // Query the current terminal size without notifying listeners
    [[nodiscard]]
    TerminalDimension queryTerminalSize();

   private:
    // check the dimension of the terminal
    [[nodiscard]]
    TerminalDimension getTerminalDimension();

    // all the listeners for terminal size updates
    utils::SparseSet<TerminalDimensionListener*> listeners;
};

// A token that manages the lifecycle of subscription to the terminal
// Should only be issued by TerminalStatus
class TerminalDimensionToken {
   public:
    void operator=(const TerminalDimensionToken&) = delete;          // remove copy assignment
    TerminalDimensionToken(const TerminalDimensionToken&) = delete;  // remove copy construction
    TerminalDimensionToken& operator=(TerminalDimensionToken&&) = delete;  // remove move assignment

    TerminalDimensionToken(TerminalDimensionToken&& other) :
            publisher{other.publisher}, listenerID{other.listenerID} {
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

    explicit TerminalDimensionToken(TerminalStatus& publisher, utils::ID listenerID) :
            publisher{publisher}, listenerID{listenerID} {}

    TerminalStatus& publisher;
    std::optional<utils::ID> listenerID;
};

};  // namespace tui
