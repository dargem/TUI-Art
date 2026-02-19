#include "tui/terminal_status.hpp"

#include <cerrno>
#include <cstddef>
#include <cstring>
#include <format>
#include <stdexcept>

#if defined(__linux__) || defined(__unix__)
// linux or unix like machine
#include <stdio.h>
#include <sys/ioctl.h>
#include <unistd.h>

#elif defined(_WIN32) || defined(_WIN64)
// windows machine
#include "windows.h"

#endif

using utils::ID;

namespace tui {

TerminalDimensionToken TerminalStatus::addDimensionListener(
    TerminalDimensionListener* newListener) {
    ID id = listeners.push_back(newListener);
    return TerminalDimensionToken{*this, id};
}

void TerminalStatus::removeDimensionListener(ID discardedID) { listeners.erase(discardedID); }

TerminalDimension TerminalStatus::publishTerminalSize() {
    TerminalDimension terminalDimension{getTerminalDimension()};
    // iterate over the underlying sparse vector array
    for (TerminalDimensionListener* listener : listeners.getData()) {
        listener->receiveTerminalSize(terminalDimension);
    }

    return terminalDimension;
}

size_t TerminalStatus::getNumberListeners() const { return listeners.size(); }

TerminalDimension TerminalStatus::queryTerminalSize() { return getTerminalDimension(); }

// Checks the current size of the terminal
TerminalDimension TerminalStatus::getTerminalDimension() {
#if defined(__linux__) || defined(__unix__)
    struct winsize dimension;
    if (ioctl(STDOUT_FILENO, TIOCGWINSZ, &dimension) == -1) {
        throw std::runtime_error(std::format(
            "Failed ioctl querying for terminal size (errno {}: {})", errno, std::strerror(errno)));
    }
    return {dimension.ws_col, dimension.ws_row};
#elif defined(_WIN32) || defined(_WIN64)
    CONSOLE_SCREEN_BUFFER_INFO csbi;
    if (!GetConsoleScreenBufferInfo(GetStdHandle(STD_OUTPUT_HANDLE), &csbi)) {
        const DWORD error = GetLastError();
        throw std::runtime_error(
            std::format("Failed querying console screen size (GetLastError {})", error));
    }

    int columns = csbi.srWindow.Right - csbi.srWindow.Left + 1;
    int rows = csbi.srWindow.Bottom - csbi.srWindow.Top + 1;
    return TerminalDimension{columns, rows};

#else  // not supported systems
#error \
    "unsupported operating system, detected to not be linux or windows so the program is unable to query size of the terminal"

#endif
}

}  // namespace tui