#pragma once

#include "tui/terminal_status.hpp"
#include "tui/ansi/ansi_printer.hpp"

using tui::TerminalStatus;
using tui::ansi::Printer;

class AppContext
{
public:
    AppContext();

    [[nodiscard]]
    TerminalStatus &getTerminalStatus();

    [[nodiscard]]
    Printer &getPrinter();

private:
    TerminalStatus terminalStatus;
    Printer printer;
};