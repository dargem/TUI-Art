#pragma once

#include <cstddef>

namespace ansi {

class TerminalStatus {
public:
    static TerminalStatus& getInstance();

    void operator=(const TerminalStatus& terminalStatus) = delete; // remove assignment
    TerminalStatus(const TerminalStatus& terminalStatus) = delete; // remove copy construction

    // The terminal will not automatically get the most up to date info on size requests
    void updateTerminalSize();

    size_t getTerminalXSize();
    size_t getTerminalYSize();
private:
    TerminalStatus();
};

};

