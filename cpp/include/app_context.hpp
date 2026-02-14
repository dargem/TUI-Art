#pragma once

#include "tui/terminal_status.hpp"
#include "tui/ansi/ansi_printer.hpp"

using tui::ansi::Printer;
using tui::TerminalStatus;

class AppContext {
public:
    AppContext();

    [[nodiscard]]
    TerminalStatus& getTerminalStatus();

    [[nodiscard]]
    Printer& getPrinter();

private:
    TerminalStatus terminalStatus;
    Printer printer;
};